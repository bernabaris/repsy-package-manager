# Repsy Package Manager

## Overview

Repsy Package Manager is a Spring Boot web application providing REST APIs for deploying and downloading Repsy packages (`package.rep`) and metadata (`meta.json`).  
It supports two storage strategies (**file-system** and **object-storage**) and uses PostgreSQL to track packages.  
The application is dockerized, deployed to a public Repsy repository, and includes two storage libraries published to Repsy's private Maven repository.  
It follows REST best practices and ensures robust input validation.

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker Desktop

---

## Installation

1. Clone the repository:

    ```bash
    git clone https://github.com/bernabaris/repsy-package-manager.git
    ```

2. Navigate to the Docker setup directory depending on the storage type you want to use:

   - **For File System Storage:**

     ```bash
     cd repsy-package-manager/repsy-app/docker/repsy-app
     ```

   - **For Object Storage:**

     ```bash
     cd repsy-package-manager/repsy-app/docker/minio
     ```

3. Start related containers using Docker Compose:

   - **If using File System Storage:**

     ```bash
     docker compose up -d
     ```

   - **If using Object Storage:**

     ```bash
     # Start MinIO
     docker compose up -d

     # Then start repsy-app
     cd ../repsy-app
     docker compose up -d
     ```

4. Go back to the main project directory:

    ```bash
    cd ../../../..
    ```

5. Build the Docker image:

    ```bash
    docker build -f docker/Dockerfile -t repsy-app:1.0.0 .
    ```

---

## Important Notes

- If you are using **File System Storage**, only the `repsy-app` container needs to be up.
- If you are using **Object Storage**, you must first run the `minio` container and then the `repsy-app`.
- `MinIO` is **only required** for Object Storage setup.
