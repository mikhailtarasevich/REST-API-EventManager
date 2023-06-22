# EVENT-MANAGER - rest-api project

REST API that allows users to create accounts (event manager/participant), create events, send requests
to participate in an event, and review and accept/reject these requests after consideration.

## Run the application

To run the application (you need to have Docker installed on your computer),
please [download the docker-compose](https://drive.google.com/drive/folders/1sGr6TYYKUlZxwRvHvLytXcQxFhQp832u?usp=sharing)
file. Open a terminal in the folder containing the file and
execute the command:

    docker-compose up -d

Open the link [http://localhost:8888/swagger-ui/](http://localhost:8888/swagger-ui/) in a web browser.

## How to use the application.

To access the application, you need to go through authentication:

Go to the page [http://localhost:8888/swagger-ui/](http://localhost:8888/swagger-ui/):

- Find AuthController
- Send data to /api/v1/auth/login (email: admin@example.com - Role_Admin/manager1@example.com -
  Role_Manager/participant1@example.com - Role_Participant/password for all: 1111)
- Receive a token in the response
- Authorize Swagger by inserting the value "Bearer your-token" into the value field

Authenticated users can only access endpoints that are accessible according to their authorization privileges.

# Libraries and Frameworks

- Spring Boot
- Spring Security
- JWT token
- Spring Data JPA
- Postgres
- Junit jupiter
- Mockito
- Lombok
- Docker
- Swagger