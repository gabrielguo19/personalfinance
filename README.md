
# Personal Finance Application(Spring Boot Backend)

## Introduction
This is a Spring Boot application for personal finance management. It includes an OAuth2 endpoint that uses the Google Gmail API to send emails. As well as comprehensive CRUD and Advanced operations


## Prerequisites
1. Java 17 or higher
2. Maven 3.6.0 or higher
3. MongoDB (for the database)

## Setup Instructions

## 1. Clone the Repository
```sh
git clone https://github.com/yourusername/your-repo-name.git
cd your-repo-name
```

## 2. Google API Credentials
- Go to the Google Cloud Console. 
- Create a new project or select an existing project. 
- Navigate to the API & Services > Credentials. 
- Create OAuth 2.0 Client IDs with the following settings: 
- Application type: Web application 
- Authorized redirect URIs: http://localhost:8080/oauth2/callback 
- Download the credentials.json file and place it in the src/main/resources directory of the project. 
- Make sure to edit the GmailService class gmail sender adress with your own for it to send emails as intended:
```
email.setFrom(new InternetAddress("yourgmail@gmail.com"))) 
```

## 3. MongoDB Setup
### 1. Install MongoDB 
if you already haven't by following the guide on [MongoDB Download](https://www.mongodb.com/try/download/community) \ 
### 2. Setup MongoDB Atlas
Go to [MongoDB Atlas](https://www.mongodb.com/atlas) and sign in or create an account. \
Create a new cluster (you can name it as you like). \
Click on Connect and choose a connection method (e.g., MongoDB Compass, Shell, or VSCode). \
Copy the connection string provided. \
### 3. Add the following configuration to your application.properties file: 
```
spring.application.name=personalfinance
spring.data.mongodb.database=YourDatabaseName
spring.data.mongodb.uri=mongodb+srv://username:<password>@clustername.smth.mongodb.net/
```
Replace username, <password>, YourDatabaseName, and clustername with your actual MongoDB credentials and database name. \

### 4. Disable Security Auto-Configuration (For Testing)
Add the folling line to your application.properties
```
spring.autoconfigure.exclude[0]=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
```


## 4. Install MAVEN dependencies
```
mvn clean install
```
Alternatively, you can use the provided **pom.xml** file to install all necessary dependencies. 

## 5. Test Endpoints
- Use [Postman](https://www.postman.com/) or another API testing tool to test the endpoints. 
- Verify that you see JSON updates in MongoDB Compass or your preferred MongoDB tool. 

## 6. API Documentation
Access the RESTful API documentation through Swagger/OpenAPI at:
```
http://localhost:8080/swagger-ui/index.html#/ 
```





