# Read Me First
Interview Task - Java Developer

# Getting Started

### Reference Documentation
#### Description
We want to build a service that will provide us with all the necessary tools to be able to flag to a Business
Operator when a customer, playing a game, stake more than £X in a given Ys time window.
Once we accumulate for the same account more than 100 pounds, in 1h, we want a message to be
published and stored.

#### Examples use cases:

Given X = 100 and Y = 60 min
* Incoming message 1 : {accountId:123, stake:40} – 00:00 – Do nothing
* Incoming message 2 : {accountId:456, stake:90} – 00:10 – Do nothing
* Incoming message 3 : {accountId:123, stake:40} – 00:25 – Do nothing
* Incoming message 4 : {accountId:789, stake:110} – 00:25 – Alert, publish a message and store
* Incoming message 5 : {accountId:123, stake:10} – 00:30 – Do nothing
* Incoming message 6 : {accountId:123, stake:40} – 00:45 – Alert, publish a message and store
* Incoming message 7 : {accountId: 456, stake:50} – 01:01 – Alert, publish a message and store

# Guides 
The following guides illustrate how to use application:

### How run 
Application required: JAVA 11, RabbitMQ 3.8.7.
For persist data it use internally H2 database in memory, but it might be configured in **application.properties**.

RabbitMQ should be run on default port **[5672]**, you don't need configure any additional queues, becase application will create nessesery queses on the firtst start. 

First you have to compile and package source code by Maven version (3.6.3). It will generate **jar** file which you have to run by JAVA 11. 

To run application you should run command:
**java -jar interview-task-0.9.0-SNAPSHOT.jar**

Check console is there no errors. Then you can run application in web browser with URL:
**http://localhost:8080/swagger-ui.html**
All REST method are well documented in swagger-ui. Enjoy

More information will be added here in the future.