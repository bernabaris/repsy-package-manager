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
2. Navigate to directory for PostgreSQL setup with Docker:
   ```sh
   cd repsy-package-manager/repsy-app/docker/postgresql
3. Start the PostgreSQL container by running:
   ```sh
   docker compose up -d


