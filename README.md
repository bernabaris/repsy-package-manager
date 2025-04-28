# Repsy Package Manager

## Overview

Repsy Package Manager is a Spring Boot web application providing REST APIs for deploying and downloading Repsy packages (`package.rep`) and metadata (`meta.json`).  
It supports two storage strategies (**file-system** and **object-storage**) and uses PostgreSQL to track packages.  
The application is dockerized, deployed to a public Repsy repository, and includes two storage libraries published to Repsy's private Maven repository.  
It follows REST best practices and ensures robust input validation.

---

## Getting Started

### Prerequisites

- Docker

#### For Development

- Java 21 
- Maven

---

## Setup and Test

1. Clone the repository:

    ```bash
    git clone https://github.com/bernabaris/repsy-package-manager.git
    ```

2. Navigate to the Docker setup directory depending on the storage type you want to use:

   - **For File System Storage:**

     ```bash
     cd repsy-package-manager/repsy-app/docker/file-system
     ```

   - **For Object Storage:**

     ```bash
     cd repsy-package-manager/repsy-app/docker/object-storage
     ```
   
Both docker compose files use same repsy-app docker image.

3. Start related containers using Docker Compose:

     ```bash
     docker compose up -d
     ```

## API Guideline

## API Documentation

The Repsy Package Manager provides a comprehensive API for interacting with Repsy packages. You can explore and test the API using the following resources:

- **Postman Collection**: [repsy.postman_collection.json](repsy.postman_collection.json)  
  Import this collection into Postman to easily test and interact with the API.

- **OpenAPI Documentation**: [openapi_doc.json](openapi_doc.json)  
  Use this OpenAPI specification to integrate the API with your applications or tools.




## For Development

1. *File System Storage Library*
   ```bash
   cd file-system-storage
   mvn clean deploy
   ```
2. *Object Storage Library*
   ```bash
   cd object-storage
   mvn clean deploy
   ```
3. *Repsy App*
   ```bash
   cd repsy-app
   ./docker_build_and_push.sh
   ```