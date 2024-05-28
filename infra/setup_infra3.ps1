# Переход на один уровень выше
Set-Location ..

Write-Host "Current directory: $(Get-Location)"

### ЗАПРОС IP #############################
Write-Host "Request IP"
$ips = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.IPAddress -ne '127.0.0.1' }).IPAddress
$ip = $ips[-1] #последнее значение сохраняем в переменную
Write-Host "Current IP: $ip"

# Установка текущей директории
$teamcity_tests_directory = Get-Location # Текущая папка
$workdir = "teamcity_tests_infrastructure" # Папка для хранения
$teamcity_server_workdir = "teamcity_server" # Название папки для запуска TeamCity сервера
$teamcity_agent_workdir = "teamcity_agent" # Название папки для запуска TeamCity agent сервера
$selenoid_workdir = "selenoid" # Название папки для запуска Selenoid
# Определение переменных с именами контейнеров
$teamcity_server_container_name = "teamcity_server_instance"
$teamcity_agent_container_name = "teamcity_agent_instance"
$selenoid_container_name = "selenoid_instance"
$selenoid_ui_container_name = "selenoid_ui_instance"

# Список переменных с папками
$workdir_names = @($teamcity_server_workdir, $teamcity_agent_workdir, $selenoid_workdir)

# Список переменных с контейнерами
$container_names = @($teamcity_server_container_name, $teamcity_agent_container_name, $selenoid_container_name, $selenoid_ui_container_name)


################################
# Очистка тестовых данных
Write-Host ""
Write-Host "* * *"
Write-Host "Delete previous run data"

# Остановка и удаление контейнеров
docker ps

# Проверка и остановка/удаление контейнеров
foreach ($container in $container_names) {
    # Получаем информацию о контейнере
    $container_info = docker inspect $container 2>&1

    # Проверяем, найден ли контейнер
    if ($container_info -like "*No such*") {
        Write-Host "Container '$container' not found."
    }
    else {
        # Проверяем статус контейнера
        $container_status = docker inspect --format='{{.State.Status}}' $container
        if ($container_status -eq "running") {
            # Останавливаем контейнер
            docker stop $container
        }

        # Удаляем контейнер
        docker rm $container
    }
}

Write-Host "Current directory: $(Get-Location)"
cd ..

Remove-Item -Path $workdir -Recurse -Force # Удалить директорию
New-Item -Path $workdir -ItemType Directory # Создать директорию
Set-Location $workdir # Перейти в директорию
Write-Host "Current directory: $(Get-Location)"

# Создаем папки, если они не существуют
foreach ($dir in $workdir_names) {
    # Проверяем, существует ли папка
    if (-not (Test-Path $dir)) {
        New-Item -Path $dir -ItemType Directory
        Write-Host "Created directory: $dir"
    }
}

Write-Host "Current directory: $(Get-Location)"

################################
Write-Host ""
Write-Host "* * *"
Write-Host "Start teamcity server"

Set-Location $teamcity_server_workdir # Переход в папку с TeamCity сервером
Write-Host "Current directory: $(Get-Location)"

# Запуск докера TeamCity сервер
docker run -d --name $teamcity_server_container_name  `
    -v "$(Get-Location)/logs:/opt/teamcity/logs"  `
    -p 8111:8111 `
    jetbrains/teamcity-server

Write-Host "TeamCity Server is running..."


################################
Write-Host ""
Write-Host "* * *"
Write-Host "Start selenoid"

# Выходим из папки TeamCity и переходим в папку Selenoid
Set-Location ..
Write-Host "Current directory: $(Get-Location)"
Set-Location $selenoid_workdir # Переходим в папку Selenoid
Write-Host "Current directory: $(Get-Location)"
New-Item -Path "config" -ItemType Directory # Создаем папку конфига

Write-Host "copy file config browsers"
Copy-Item "$teamcity_tests_directory/infra/browsers.json" "config/" # Копируем файл конфигурации браузеров в папку config
Write-Host "Current directory: $(Get-Location)"

# Pull all browser images
Write-Host "Pull all browser images: selenoid/vnc:firefox_89.0 selenoid/vnc:chrome_91.0 selenoid/vnc:opera_76.0"
docker pull selenoid/vnc:firefox_89.0
docker pull selenoid/vnc:chrome_91.0
docker pull selenoid/vnc:opera_76.0

# Установка переменных
$selenoid_container_name = "selenoid_instance"  # Имя контейнера
$selenoid_port = 4444                         # Порт, на котором будет доступен Selenoid
$selenoid_config_path = "$(Get-Location)/config"        # Путь к конфигурационным файлам Selenoid

# Запуск Selenoid
docker run -d `
--name $selenoid_container_name `
-p ${selenoid_port}:${selenoid_port} `
-v /var/run/docker.sock:/var/run/docker.sock `
-v ${selenoid_config_path}:/etc/selenoid/:ro `
aerokube/selenoid:latest-release

# Запускаем Selenoid UI на 8888 так как 8080 занят у меня
Write-Host "Run Selenoid UI"
docker run -d --name $selenoid_ui_container_name -p 8888:8080 aerokube/selenoid-ui:latest-release --selenoid-uri "http://${ip}:${selenoid_port}"

Write-Output "Setup teamcity server"
Set-Location ..
Write-Output "Current directory: $(Get-Location)"
Set-Location $teamcity_tests_directory  # Возвращаемся в корневую папку проекта
# Запускаем стартовый тест и конкретный метод в нем
mvn clean test -Dtest=SetupTest#StartUpPage

# Ожидание нажатия Enter с отображением запроса для мануальных действий по принятию лицензии и т.д. (использовал до mvn clean test -Dtest=SetupTest#StartUpPage)
#$prompt = "Нажмите Enter для продолжения..."
#Read-Host $prompt -Keypress
# Сообщение после нажатия Enter
#Write-Host "ПРОДОЛЖАЕМ!

# Parse superuser token
Write-Host "Parse superuser token"
$teamcity_tests_directory = "C:\Users\afedorov\AquaProjects\teamcity_tests_infrastructure"
$teamcity_server_log = "$teamcity_tests_directory\teamcity_server\logs\teamcity-server.log"
$config_file_path = "C:\Users\afedorov\AquaProjects\teamcity-testing-framework\src\main\resources\config.properties"

if (Test-Path $teamcity_server_log) {
    # Извлечь только цифровой токен
    $super_user_token = (Get-Content $teamcity_server_log | Select-String -Pattern "Super user authentication token:") | ForEach-Object { $_.Line.Split(':')[-1].Trim() -replace '\D', '' }

    Write-Host "+++++++++++++++++++++++++++++++++++"
    Write-Host "Super user token: $super_user_token"
    Write-Host "+++++++++++++++++++++++++++++++++++"
} else {
    Write-Host "-----------------------------------"
    Write-Host "Log file not found: $teamcity_server_log"
    Write-Host "-----------------------------------"
}

# Извлечь только цифровой токен
$super_user_token = (Get-Content $teamcity_server_log | Select-String -Pattern "Super user authentication token:") | ForEach-Object { $_.Line.Split(':')[-1].Trim() -replace '\D', '' }

# Проверка наличия токена
if ($super_user_token -ne "") {
    # Обновить значение ключа "superUserToken" в файле конфигурации
    (Get-Content $config_file_path) -replace "superUserToken=.*", "superUserToken=$super_user_token" | Set-Content -Path $config_file_path

    # Вывести сообщение об успехе
    Write-Host "+++++++++++++++++++++++++++++++++++"
    Write-Host "Super user token saved to config file: $config_file_path"
    Write-Host "+++++++++++++++++++++++++++++++++++"
} else {
    # Предупреждение о ненайденном токене
    Write-Host "-----------------------------------"
    Write-Host "Token not found. Skipping config file update."
    Write-Host "-----------------------------------"
}

Write-Output "Current directory: $(Get-Location)"
#Adding Agent
#Переход в $teamcity_agent_workdir
#Запуск докера агента
# docker run -e SERVER_URL="http://192.168.200.253:8111" -v $PWD/conf:/data/teamcity_agent/conf jetbrains/teamcity-agent
# Авторизация агента на клиенте
#mvn clean test -Dtest=SetupTest#setupTeamCityAgentTest



# Define the project root directory
$project_root = "C:\Users\afedorov\AquaProjects\teamcity-testing-framework"

# Run system tests
Write-Host "Run system tests"
cd $project_root
$env:host = $ip
$env:superUserToken = $super_user_token
$env:remote = "http://$ip/wd/hub"
$env:browser = "firefox"


# Запуск тестов

Write-Output "Run API tests"
mvn clean test -DsuiteXmlFile=C:\Users\afedorov\AquaProjects\teamcity-testing-framework\testng-suites\api-suite.xml

Write-Output "Run UI tests"
mvn clean test -DsuiteXmlFile=C:\Users\afedorov\AquaProjects\teamcity-testing-framework\testng-suites\ui-suite.xml

