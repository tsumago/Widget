
# Widget API
This is the Widget API coding exercise documentation.
# Installation
## 1. Install Postgres
Required:
- JDK 11 or more [here](https://docs.aws.amazon.com/corretto/latest/corretto-11-ug/downloads-list.html) (Add it to the system path)
- Maven 10 or more [here](https://maven.apache.org/download.cgi) (Add it to the system path)
- Postgresql database 12 or more [here]([https://www.postgresql.org/download/) (Add it to the system path).
**NB:** Note the admin user (postgres by default) password 
## 2. Create Database
- In command line terminal:
```
psql -U postgres (you will be then prompted to enter the password)
postgres=# CREATE DATABASE widget
```
## 3. Clone the repository
Clone the code source repository:
```
cd <YOUR_WORK_FOLDER>
git clone https://github.com/tsumago/Widget.git
```
## 4. Update database username and password
In the project folder, open **<PROJECT_FOLDER>\src\main\resources\application-database.properties**
and update the postgres database parameters:
```
# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/widget
spring.datasource.username=<USERNAME>
spring.datasource.password=<PASSWORD>
``` 
# Run application
To run the application, open a command line terminal in the project root:
```bash
mvn spring-boot:run
```
# Covered complications
All the complications were addressed except the Filtering by area for lack of time
The in memory and database are set as Spring profiles, in memory mode is the default. To activate database mode you will need to run the application using the 'database' profile
```bash
mvn spring-boot:run -Dspring.profiles.active=database
```
# Postman Collection
a postman collection, is available in the root poject folder to test the API manually. To use it download Postman [here](https://www.postman.com/downloads/) and import the json collection file.
# Improvement
- Use Spring WebFlux
- Improve testing coverage (I am aware that my coverage does not match the criteria, I did some of each type of test to showcase my abilities only)
- Implement Area filtering 