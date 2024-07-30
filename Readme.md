# :bank: Three-tier Java client/server web application :bank:

![Java 1.8.0](https://img.shields.io/badge/Java-1.8.0-blue)
![MS SQL Server 2008 R2](https://img.shields.io/badge/MS%20SQL%20Server-2008%20R2-blue)
![Jetty 9.4.44](https://img.shields.io/badge/Jetty-9.4.44.v20210927-blue)
![Bootstrap 4.5.2](https://img.shields.io/badge/Bootstrap-4.5.2-blue)
![Maven 3.6.3](https://img.shields.io/badge/Maven-3.6.3-blue)
![Tomcat 9.0.88](https://img.shields.io/badge/Tomcat-9.0.88-blue)
![POI 5.2.3](https://img.shields.io/badge/POI-5.2.3-blue)
![JasperReports 6.16.0](https://img.shields.io/badge/JasperReports-6.16.0-blue)
![JSTL 1.2](https://img.shields.io/badge/JSTL-1.2-blue)

# :mag_right: Project overview :mag_right:
This project is a three-tier client/server application designed to enhance the management and interaction capabilities of a museum's digital infrastructure. The application is structured into three primary layers: Presentation, Business Logic, and Data Storage.

## :gear: Technology stack :gear:
- **Frontend:** HTML, CSS, JavaScript (Bootstrap)
- **Backend:** Java, Jetty, Tomcat
- **Database:** MS SQL Server 2008 R2
- **Build tool:** Maven

## :cd: Installation and Setup :cd:

## Prerequisites

- Java 1.8.0
- Maven 3.6.3
- MS SQL Server 2008 R2
- Apache Tomcat 9.0.88 (optional if using Jetty)
- Microsoft .NET Framework 4.5 or higher
- Windows Management Framework 5.1 or higher
- Windows PowerShell 5.1 or higher

### Steps
1. Clone the current repository:
   ```bash
   git clone https://github.com/SayMyName1337/Three-tier-museum-web-application.git
   ```
2. Configure **MS SQL Server**. The most convenient way to do this is through [SQL Server Management Studio (SSMS)](https://learn.microsoft.com/ru-ru/sql/ssms/download-sql-server-management-studio-ssms?view=sql-server-ver16).
3. Create a "museums" database:
   ```sql
   CREATE DATABASE museums
   ```
4. After this step, run the script to create tables ["create_museums_db.sql"](https://github.com/SayMyName1337/Three-tier-museum-web-application/blob/master/DB/create_museums_db.sql) in the manager, and then run the script ["fill_tables_db.sql"](https://github.com/SayMyName1337/Three-tier-museum-web-application/blob/master/DB/fill_tables_db.sql) to fill them with test data. As a result, we get the following model:
   
   ![db-model](https://private-user-images.githubusercontent.com/113287758/353525422-cbeb60cb-2ca9-4481-89ac-2808b9875aab.png?jwt=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJnaXRodWIuY29tIiwiYXVkIjoicmF3LmdpdGh1YnVzZXJjb250ZW50LmNvbSIsImtleSI6ImtleTUiLCJleHAiOjE3MjIzNTQ0MjMsIm5iZiI6MTcyMjM1NDEyMywicGF0aCI6Ii8xMTMyODc3NTgvMzUzNTI1NDIyLWNiZWI2MGNiLTJjYTktNDQ4MS04OWFjLTI4MDhiOTg3NWFhYi5wbmc_WC1BbXotQWxnb3JpdGhtPUFXUzQtSE1BQy1TSEEyNTYmWC1BbXotQ3JlZGVudGlhbD1BS0lBVkNPRFlMU0E1M1BRSzRaQSUyRjIwMjQwNzMwJTJGdXMtZWFzdC0xJTJGczMlMkZhd3M0X3JlcXVlc3QmWC1BbXotRGF0ZT0yMDI0MDczMFQxNTQyMDNaJlgtQW16LUV4cGlyZXM9MzAwJlgtQW16LVNpZ25hdHVyZT1hNjM1MWYyN2M3MzQ2ZTE0YzZhMWRmNTc3NmRmZDE4ZWRmYzMzYmU1YWFmNDQ0MWNlNzcxZjA1OGUxY2ZkN2FjJlgtQW16LVNpZ25lZEhlYWRlcnM9aG9zdCZhY3Rvcl9pZD0wJmtleV9pZD0wJnJlcG9faWQ9MCJ9.VN-B9_uY4nlHMoVLTtbkvQuEgHtcqpFHLFzixRmg79s)

5. After completing the above steps, go to the project folder
   ```bash
   cd Three-tier-museum-web-application
   ```
6. Establish a correct connection to your database in the file ["web.xml"](https://github.com/SayMyName1337/Three-tier-museum-web-application/blob/master/WebContent/WEB-INF/web.xml)
   ```xml
   <context-param>
		<param-name>jdbcURL</param-name>
		<!-- <param-value>jdbc:sqlserver://10.4.130.105:1433;database=B2;trustServerCertificate=true;</param-value> -->
		<param-value>jdbc:sqlserver://localhost:1433;database=museums;trustServerCertificate=true;</param-value>
	</context-param>

	<context-param>
		<param-name>jdbcUsername</param-name>
		<param-value>PutYourUserNameHere</param-value>
	</context-param>

	<context-param>
		<param-name>jdbcPassword</param-name>
		<param-value>PutYourPasswordHere</param-value>
	</context-param>
    ```
7. Build and run the project via the command:
   ```bash
   mvnw jetty:run-war
   ```
8. Open any browser (preferably Google Chrome) and go to this address:
   ```search
   localhost:8080/museums/
   ```
# Example of work
After all the necessary settings and launching the projects it should look like this:

![example](https://github.com/user-attachments/assets/6e15d300-f70c-4b7d-bf4c-94466e9b7e28)

# :sparkles: Features and capabilities of the application :sparkles:
- Basic data management operations (CRUD)
- Export simple report to Excel
- Export simple report to jasper reports
- Loading images and the ability to draw on them
