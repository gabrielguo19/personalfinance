
# Personal Finance Application(Spring Boot Backend)

## Introduction
This is a Spring Boot application for personal finance management. It includes an OAuth2 endpoint that uses the Google Gmail API to send emails. As well as comprehensive CRUD and Advanced operations


## Prerequisites
1. Java 17 or higher
2. Maven 3.6.0 or higher
3. MongoDB (for the database)

## Setup Instructions

### 1. Clone the Repository
```sh
git clone https://github.com/yourusername/your-repo-name.git
cd your-repo-name
```

### 2. Google API Credentials
Go to the Google Cloud Console. \
Create a new project or select an existing project. \
Navigate to the API & Services > Credentials. \
Create OAuth 2.0 Client IDs with the following settings: \
Application type: Web application \
Authorized redirect URIs: http://localhost:8080/oauth2/callback \
Download the credentials.json file and place it in the src/main/resources directory of the project. \
Make sure to edit the GmailService class gmail sender adress with your own for it to send emails as intended. (under this line  email.setFrom(new InternetAddress("yourgmail@gmail.com"))) \

### 3. MongoDB Setup
1. Install MongoDB if you already haven't \
2. Start the MongoDB service by heading to their website using MongoDB atlas https://www.mongodb.com/atlas \
3. Create a Cluster(Name it however) and click connect. Use MongoDB Compass or other connection tools (Such as Shell or MongoDB for VSCode) to connect and then copy the link you are given \
the copied link you will then put in an application.properties file in your project \
mongodb+srv://username:<password>@cluster1finance.smth.mongodb.net/ \

it will be something like this: \
spring.application.name=personalfinance \
spring.data.mongodb.database=Your Database Name \
spring.data.mongodb.uri=mongodb+srv://username:<password>@cluster1finance.smth.mongodb.net/ \
Just to disable security servlet for now - for testing purposes... put the following line in your application.properties file \
spring.autoconfigure.exclude[0]=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration \


### 4. Install MAVEN dependencies
```
mvn clean install
```
or use my pom.xml file to install all maven dependencies \

When you use Postman to test endpoints, you should see JSON updates in your MongoDB compass \

### 5. API Documentation
You can access all the RESTful Api Documentation through Swagger/OpenAPI at \
```
http://localhost:8080/swagger-ui/index.html#/ 
```





