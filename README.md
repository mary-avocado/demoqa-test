# demoqa tests

Проект для тестирования сайта https://demoqa.com с помощью Selenide и JUnit 5.  
Есть локальный и удалённый запуск (через Selenoid в Docker), сбор метрик Prometheus + Grafana.

## структура

```
untitled/
├── pom.xml
├── src/test/java/org/example/
│   ├── ElementsPageTest.java
│   ├── FormsPageTest.java
│   └── SelenideConfig.java
├── infrastructure/
│   ├── docker-compose.yml
│   ├── browsers.json
│   └── prometheus.yml
└── docs/
    └── grafana-dashboard.json
```

## локальный запуск

нужно java 17+, maven, chrome

```
mvn -Dtest="*Test" test
```

тесты проверяют наличие элементов на страницах Elements и Forms.

## запуск через selenoid

требуется docker

```
cd infrastructure
docker compose up -d
```

откроются сервисы:  
- selenoid (4444)  
- selenoid-ui (8080)  
- prometheus (9090)  
- pushgateway (9091)  
- grafana (3000)

проверка: http://localhost:8080

запуск тестов:
```
cd ..
mvn -Dtest="*Test" -Dselenide.remote=http://localhost:4444/wd/hub -Dpushgateway.url=localhost:9091 test
```

## мониторинг

prometheus: http://localhost:9090  
grafana: http://localhost:3000 (admin/admin)

дашборд импортировать из docs/grafana-dashboard.json

## очистка

```
cd infrastructure
docker compose down
curl -X DELETE http://localhost:9091/metrics/job/ui_test_run
```
