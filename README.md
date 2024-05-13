## Introduction    

---
This project is inspired by Instagram where we can create a post 
with uploaded photos and interact with each other's posts through comments.  
![Java](https://img.shields.io/badge/Java-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-green)
![JPA](https://img.shields.io/badge/JPA-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-gray)
![Docker](https://img.shields.io/badge/Docker-blue)
![Spring Security](https://img.shields.io/badge/Spring%20Security-gray)
![WebClient](https://img.shields.io/badge/WebClient-blue)
![Spring Boot Test](https://img.shields.io/badge/Spring%20Boot%20Test-gray)
![Postman](https://img.shields.io/badge/Postman-orange)

## Table of content

---

- [Technologies](#technologies)
- [Set up Docker](#set-up-docker)
- [Project's Endpoint](#projects-endpoint)

## Technologies 

---

- Java 
- Spring Web
- Spring Security
- WebClient
- JPA
- Postgres
- Docker
- Spring Boot Test

## Set up docker

---
In this project, Docker is used to set up the Postgres database and pgadmin.  
Please take a look at the below documents for reference.  
- [Run PostgreSQL on Docker and Setting Up pgAdmin](https://www.geeksforgeeks.org/run-postgresql-on-docker-and-setting-up-pgadmin/)  
- [How to set up Postgresql and Pgadmin with Docker](https://dev.to/steadylearner/how-to-set-up-postgresql-and-pgadmin-with-docker-51h)

## Project's endpoint

---
### Auth
| **Method** | **Urls**                  | **Action**                |
|------------|---------------------------|---------------------------|
| Post       | /api/v1/auth/register     | register a new account    |
| Post       | /api/v1/auth/login        | login an account          |
| Post       | /api/v1/auth/refreshToken | generate new access token |
| Post       | /api/v1/auth/logout       | logout the account        |

### File
| **Method** | **Urls**            | **Action**    |
|------------|---------------------|---------------|
| Post       | /api/v1/file/upload | upload images |

### Post
| **Method** | **Urls**          | **Action**                    |
|------------|-------------------|-------------------------------|
| Post       | /api/v1/posts     | create a new post             |
| Get        | /api/v1/posts/:id | retrieve a post by ```:id ``` |
| Get        | /api/v1/posts     | retrieve all posts            |
| Put        | /api/v1/posts/:id | update a post by ```:id ```   |
| Delete     | /api/v1/posts/:id | delete a post by ```:id ```   |

### Comment
| **Method** | **Urls**                       | **Action**                      |
|------------|--------------------------------|---------------------------------|
| Post       | /api/v1/posts/:postId/comments | create a new comment for a post |
| Get        | /api/v1/posts/:postId/comments | retrieve all comments of a post |
| Put        | /api/v1/comments/:id           | update a comment by ```:id ```  |
| Delete     | /api/v1/comments/:id           | delete a comment by ```:id ```  |



