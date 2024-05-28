
# Переход на один уровень выше
cd ..

echo "Current directory: $(pwd)"

# Установка текущей директории
teamcity_tests_directory=$(pwd) # Текущая папка
workdir="teamcity_tests_infrastructure" # Папка для хранения
teamcity_server_workdir="teamcity_server" # Название папки для запуска TeamCity сервера
teamcity_agent_workdir="teamcity_agent" # Название папки для запуска TeamCity agent сервера
selenoid_workdir="selenoid" # Название папки для запуска Selenoid
teamcity_server_container_name="teamcity_server_instance" # Имя контейнера
teamcity_agent_container_name="teamcity_agent_instance" # Имя контейнера
selenoid_container_name="selenoid_instance" # Имя контейнера
selenoid_ui_container_name="selenoid_ui_instance" # Имя контейнера

# Список переменных с папками
workdir_names=($teamcity_server_workdir $teamcity_agent_workdir $selenoid_workdir)
# Список переменных с контейнерами
container_names=($teamcity_server_container_name $teamcity_agent_container_name $selenoid_container_name $selenoid_ui_container_name)

### ЗАПРОС IP #############################
echo "Request IP"
ip=$(ipconfig | sed -n -e 's/.*IPv4 Address[^:]*: *\([0-9.]*\).*/\1/p' | awk 'END{print $1}')
echo "Current IP: $ip"

################################
# Очистка тестовых данных
echo "Delete previous run data"

rm -rf $workdir # Удалить директорию
mkdir $workdir # Создать директорию
cd $workdir # Перейти в директорию
echo "Current directory: $(pwd)"
# Создаем папки, если они не существуют
for dir in "${workdir_names[@]}"; do
  # Проверяем, существует ли папка
  if [ ! -d "$dir" ]; then
    mkdir $dir
    echo "Created directory: $dir"
  fi
done

echo "Current directory: $(pwd)"

# Для всего списка контейнеров остановка и удаление
for container in "${container_names[@]}"; do
  docker stop $container || true
  docker rm $container || true
done

################################
echo "Start teamcity server"

cd $teamcity_server_workdir # Переход в папку с TeamCity сервером
echo "Current directory: $(pwd)"

# Запуск докера TeamCity сервер
docker run -d --name $teamcity_server_container_name  \
    -v $(pwd)/logs:/opt/teamcity/logs  \
    -p 8111:8111 \
    jetbrains/teamcity-server

echo "TeamCity Server is running..."

################################
echo "Start selenoid"

# Выходим из папки TeamCity и переходим в папку Selenoid
cd ..
echo "Current directory: $(pwd)"
cd $selenoid_workdir # Переходим в папку Selenoid
echo "Current directory: $(pwd)"
mkdir config # Создаем папку конфига

echo "copy file config browsers"
cp $teamcity_tests_directory/infra/browsers.json config/ # Копируем файл конфигурации браузеров в папку config
echo "Current directory: $(pwd)"




#ДАЛЬШЕ НЕ РАБОТАЕТ НА ВИНДЕ + BASH Error response from daemon: mkdir C:\Program Files\Git\var: Access is denied.
echo ""
echo ""
echo ""
echo ""
# Запуск Selenoid
docker run -d \
            --name $selenoid_container_name \
            -p 4444:4444 \
            -v /var/run/docker.sock:/var/run/docker.sock \
            -v /c/Users/afedorov/AquaProjects/teamcity-testing-framework/teamcity_tests_infrastructure/selenoid/config/:/etc/selenoid/:ro \
            aerokube/selenoid:latest-release



# Добавляем паузу в конце
echo "Press Enter to continue..."
read


# Парсим браузеры из файла конфига по регулярному выражению
image_names=($(awk -F'"' '/"image": "/{print $4}' "$(pwd)/config/browsers.json"))

# Подтягиваем образы для браузеров Selenoid
echo "Pull all browser images: $image_names"

# Для каждого образа делаем pull
for image in "${image_names[@]}"; do
  docker pull $image
done

################################
echo "Start selenoid-ui"
# Находимся внутри папки Selenoid
echo "Current directory: $(pwd)"
# Запускаем Selenoid UI
docker run -d \
            --name $selenoid_ui_container_name \
            -p 8080:8080 \
            aerokube/selenoid-ui:latest-release --selenoid-uri "http://$ip:4444"

################################
echo "Setup teamcity server"
cd ..
echo "Current directory: $(pwd)"
cd $teamcity_tests_directory # Возвращаемся в корневую папку проекта
# Запускаем стартовый тест (который создаем предварительно) и конкретный метод в нем
mvn clean test -Dtest=SetupTest#startUpTest

################################
echo "Parse superuser token"
# После прохождения SetupTest достаем токен из логов
superuser_token=$(grep -o 'Super user authentication token: [0-9]*' $workdir/$teamcity_server_workdir/logs/teamcity-server.log | awk '{print $NF}')
echo "Super user token: $superuser_token"

################################
echo "Run agent"
echo "Current directory: $(pwd)"
cd ..
cd $workdir/$teamcity_agent_workdir
echo "Current directory: $(pwd)"

docker run -d \
    -e SERVER_URL="http://$ip:8111" \
    -v $PWD/conf:/data/teamcity_agent/conf \
    --name $teamcity_agent_container_name \
    jetbrains/teamcity-agent

################################
echo "Connect agent"
echo "Current directory: $(pwd)"
cd ..
cd $teamcity_tests_directory
mvn clean test -Dtest=SetupTest#setupTeamCityAgentTest

################################
echo "Run system tests"
# Формируем URL и передаем его в config.properties проекта
# \n -- новая строка
echo -e "host=$ip:8111\nsuperUserToken=$superuser_token\nremote=http://$ip:4444/wd/hub\nbrowser=firefox" > $teamcity_tests_directory/src/main/resources/config.properties
# Распечатываем для проверки
cat $teamcity_tests_directory/src/main/resources/config.properties

# Для запуска тестов, разметим их по сьютам, создав testng-suites/api-suite.xml и testng-suites/ui-suite.xml
# Обязательно указав их в pom:
# <build>
#     <plugins>
#         <plugin>
#             <configuration>
#                 <suiteXmlFiles>
#                     <!-- Specify the path to your XML file here -->
#                     <suiteXmlFile>${suiteXmlFileApi}</suiteXmlFile>
#                     <suiteXmlFile>${suiteXmlFileUi}</suiteXmlFile>
#                 </suiteXmlFiles>
#             </configuration>
#         </plugin>
#     </plugins>
# </build>

echo "Run API tests"
mvn test -DsuiteXmlFile=testng-suites/api-suite.xml

echo "Run UI tests"
mvn test -DsuiteXmlFile=testng-suites/ui-suite.xml

# Добавляем паузу в конце
echo "Press Enter to continue..."
read
