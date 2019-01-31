# monitoring-microservice
A sample microservice which returns random numbers to represent the power consumption of devices.

## Description
* This application is a sample of microservice which returns random numbers as the power consumption of devices in JSON format. 
  The results will be exposed at a certain endpoint: 
  'http://localhost:8080/device-power-consumption'
* The application also exports a gauge metric in 'http://localhost:8082/metrics' to Prometheus which represents the total power consumptions of each device per day.
* The user can configure the ports using the configuration file 
  > src/main/resources/server-config.yml

## How to build and Run the application
* To run the application, please give the following command in the terminal
```
  mvn clean package
  java -jar target/monitoring-microservice-1.0-SNAPSHOT.jar server src/main/resources/server-config.yml
```