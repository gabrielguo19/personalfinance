
# Personal Finance Application - Gabriel Guo

## Introduction
This **Spring Boot** application is designed to be a comprehensive backend solution for personal finance management. It provides an extensive set of features aimed at helping individuals effectively manage their finances. The inspiration came from a lack of cohesive budgeting from my own family. The application includes:

- **OAuth2 Integration:** Utilizes the Google Gmail API to enable seamless email communication. This feature allows users to send and receive emails securely as part of their financial management.

- **CRUD Operations:** Supports full Create, Read, Update, and Delete operations for key financial entities such as budgets, expenses, incomes, and transactions.

- **Advanced Analytical Insights:** Offers a range of analytical trends and insights that are valuable for effective budgeting and spending. The application provides various endpoints to analyze financial data, including:

  - **Expense Trends:** Track and visualize spending patterns over time.
  - **Income Trends:** Monitor income changes and sources.
  - **Budget Analysis:** Evaluate budget performance and effectiveness.
  - **Category Spending:** Understand spending habits across different categories.
  - **Financial Health:** Assess overall financial well-being by comparing total income against expenses.
    
***These analytical insights are designed to help users plan their financial future by providing a clearer understanding of their financial behavior. The application enables users to make informed decisions about budgeting, saving, and investing, ultimately supporting better financial planning and management.***
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
Open it using your text editor/IDE of choice(IntelliJ, VSCode, Vim, etc) \


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
### Install MongoDB 
if you already haven't by following the guide on [MongoDB Download](https://www.mongodb.com/try/download/community) 
### Setup MongoDB Atlas
Go to [MongoDB Atlas](https://www.mongodb.com/atlas) and sign in or create an account. \
Create a new cluster (you can name it as you like). \
Click on Connect and choose a connection method (e.g., MongoDB Compass, Shell, or VSCode). \
Copy the connection string provided. 
### Add the following configuration to your application.properties file: 
```
spring.application.name=personalfinance
spring.data.mongodb.database=YourDatabaseName
spring.data.mongodb.uri=mongodb+srv://username:password@clustername.smth.mongodb.net/
```
Replace username, password, YourDatabaseName, and clustername with your actual MongoDB credentials and database name. 

### Disable Security Auto-Configuration (For Testing)
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
After running the project, access the RESTful API documentation through Swagger/OpenAPI at:
```
http://localhost:8080/swagger-ui/index.html#/ 
```





