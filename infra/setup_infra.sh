cd ..

teamcity_tests_directory=$(pwd) #текущая папка
workdir="teamcity_tests_infrastructure" #папка для хранения
teamcity_server_workdir="teamcity_server" #Название папки для запуска ТС сервера
teamcity_agent_workdir="teamcity_agent" #Название папки для запуска TC agent сервера
selenoid_workdir="selenoid" #Название папки для запуска Селеноид
teamcity_server_container_name="teamcity_server_instance" #Имя контейнера
teamcity_agent_container_name="teamcity_agent_instance" #Имя контейнера
selenoid_container_name="selenoid_instance" #Имя контейнера
selenoid_ui_container_name="selenoid_ui_instance" #Имя контейнера

#список переменных с папками
workdir_names=($teamcity_server_workdir $teamcity_agent_workdir $selenoid_workdir)
#список переменных с контейнерами
container_names=($teamcity_server_container_name $teamcity_agent_container_name $selenoid_container_name $selenoid_ui_container_name)

###ЗАПРОС IP#############################
echo "Request IP"
export ips=$(ifconfig | grep -Eo 'inet (addr:)?([0-9]*\.){3}[0-9]*' | grep -Eo '([0-9]*\.){3}[0-9]*' | grep -v '127.0.0.1')
export ip=${ips%%$'\n'*}
echo "Current IP: $ip"

################################
#очистка тестовых данных
echo "Delete previous run data"

rm -rf $workdir #удалить директорию
mkdir $workdir #создать директорию
cd $workdir #перейти в директорию

#для каждого имени директории создать директорию
for dir in "${workdir_names[@]}"; do
  mkdir $dir
done

#для всего списка контейнеров остановка и удаление
for container in "${container_names[@]}"; do
  docker stop $container
  docker rm $container
done

################################
echo "Start teamcity server"

cd $teamcity_server_workdir #переход в папку с ТС-сервером

#запуск докера ТС-сервер
docker run -d --name $teamcity_server_container_name  \
    -v $(pwd)/logs:/opt/teamcity/logs  \
    -p 8111:8111 \
    jetbrains/teamcity-server

echo "Teamcity Server is running..."

################################
echo "Start selenoid"

#выходим из папки ТС и переходим в папку Селеноида
cd .. && cd $selenoid_workdir
mkdir config #создадим папку конфига
#и передадим туда файл конфиг браузеров (файл предварительно добавлен в папку infra)
cp $teamcity_tests_directory/infra/browsers.json config/ #здесь в конспекте опечатка

#Запуск Селеноида
docker run -d                                   \
            --name $selenoid_container_name                                 \
            -p 4444:4444                                    \
            -v /var/run/docker.sock:/var/run/docker.sock    \
            -v $(pwd)/config/:/etc/selenoid/:ro   #передаем текущую папку           \
    aerokube/selenoid:latest-release #поднимается последняя персия

#парсим браузеры из файла конфига по регулярному выражению (команда из GPT)
image_names=($(awk -F'"' '/"image": "/{print $4}' "$(pwd)/config/browsers.json"))

#подтягиваем имэйджи для брауеров селеноида
echo "Pull all browser images: $image_names"

#для каждого имейджа делаем пулл
for image in "${image_names[@]}"; do
  docker pull $image
done

################################
echo "Start selenoid-ui"
#Находимся внутри папки Селеноида
#Запускаем
docker run -d--name $selenoid_ui_container_name                                 \
            -p 80:8080 aerokube/selenoid-ui:latest-release --selenoid-uri "http://$ip:4444"

################################
echo "Setup teamcity server"
cd .. && cd .. #ПРОВЕРИТЬ
#запускаем стартовый тест (который создаем предварительно) и конкретный метод в нем
mvn clean test -Dtest=SetupTest#startUpTest


################################
echo "Parse superuser token"
#После прохождения SetupTest достаем токен из логов (ищем грепом после выраражения пока идут цифры)
superuser_token=$(grep -o 'Super user authentication token: [0-9]*' $teamcity_tests_directory/infra/$workdir/$teamcity_server_workdir/logs/teamcity-server.log | awk '{print $NF}')
echo "Super user token: $superuser_token"

################################
echo "Run agent"

cd .. && cd $teamcity_agent_workdir

docker run -e SERVER_URL="http://$ip:8111" -v $PWD/conf:/data/teamcity_agent/conf jetbrains/teamcity-agent

################################
echo "Connect agent"
mvn clean test -Dtest=SetupTest#setupTeamCityAgentTest

################################запускаем ТС
echo "Run system tests"
#Формируем урл и передаем его в config.properties проекта
# /n -- новая строка
echo "host=$ip:8111\nsuperUserToken=$superuser_token\nremote=http://$ip:4444/wd/hub\nbrowser=firefox" > $teamcity_tests_directory/src/main/resources/config.properties
#распечатываем для проверки
cat $teamcity_tests_directory/src/main/resources/config.properties

#Для запуска тестов, разметим их по сьютам, создав testng-suites/api-suite.xml и testng-suites/ui-suite.xml
#Обязательно указав их в pom:
##    <build>
  #        <plugins>
  #            <plugin>
  #                <configuration>
  #                    <suiteXmlFiles>
  #                        <!-- Specify the path to your XML file here -->
  #                        <suiteXmlFile>${suiteXmlFileApi}</suiteXmlFile>
  #                        <suiteXmlFile>${suiteXmlFileUi}</suiteXmlFile>
  #                    </suiteXmlFiles>
  #               </configuration>
##              </plugin>
#             </plugins>
#           </build>

echo "Run API tests"
mvn test -DsuiteXmlFile=testng-suites/api-suite.xml

echo "Run UI tests"
mvn test -DsuiteXmlFile=testng-suites/ui-suite.xml

# Add a pause at the end
echo "Press Enter to continue..."
read