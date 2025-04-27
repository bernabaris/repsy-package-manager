# Repsy Package Manager 

## Overview


Repsy Package Manager is a Spring Boot web application providing REST APIs for deploying and downloading Repsy packages (package.rep) and metadata (meta.json). It supports two storage strategies (file-system and object-storage) and uses PostgreSQL to track packages. The application is dockerized, deployed to a public Repsy repository, and includes two storage libraries published to Repsy's private Maven repository. It follows REST best practices and ensures robust input validation.


## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker Desktop


### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/bernabaris/repsy-package-manager.git
2. Navigate to the Docker setup directory for file system or object storage:
   ```sh
   cd repsy-package-manager/repsy-app/docker/file-system
   cd repsy-package-manager/repsy-app/docker/object-storage
3. Start related containers using Docker Compose:
   ```sh
   docker compose up -d
4. Go back to the main project directory:
   ```sh
   cd ..
5. Build the Docker image:
   ```sh
   docker build -f docker/Dockerfile -t repsy-app:1.0.0 .
   ```

