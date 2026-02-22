# Music Library System

## Overview

A microservices-based system for uploading, storing, browsing, and deleting MP3 files with automatic metadata
extraction. The system consists of two independent services:

- **Resource Service**: Handles MP3 file storage and provides uploading, downloading, and deletion endpoints.
- **Song Service**: Manages song metadata and provides creation, retrieval, and deletion endpoints.

Each service uses its own PostgreSQL database. Services communicate via REST APIs.

## Quick Start

You can run the project in two ways:

### Option 1: Run everything with Docker Compose

1. **Clone the repository**
    ```bash
    git clone <repository-url>
    cd music-library
    ```
2. **Start all services and databases**
    ```bash
    docker compose up -d
    ```
    - Two databases will be started: one for resource-service and one for song-service.
    - Initialization scripts are located in `init-scripts/resource-db/init.sql` and `init-scripts/song-db/init.sql` and
      are applied automatically.
    - Both services will be built and started.
3. **Test endpoints**
    - Upload MP3: `POST /resources` (audio/mpeg)
    - Get metadata: `GET /songs/{id}`
    - Download MP3: `GET /resources/{id}`
    - Delete: `DELETE /resources?id=1,2,3`

### Option 2: Run databases with Docker Compose, services locally

1. **Clone the repository** (see above)
2. **Start PostgreSQL databases**
    ```bash
    docker compose up -d resource-db song-db
    ```
    - This will start both databases.
    - Initialization scripts for databases will be applied automatically.
3. **Build and run Resource Service**
    ```bash
    cd resource-service
    ./gradlew build
    ./gradlew bootRun
    ```
4. **Build and run Song Service (in a separate terminal)**
    ```bash
    cd song-service
    ./gradlew build
    ./gradlew bootRun
    ```
5. **Test endpoints** (see above)

#### Notes

- Java 21 must be installed locally for Option 2.
- To stop the services, use `Ctrl+C` in the terminal (for local runs) or `docker compose down` (for Docker Compose).
- Default environment variables and settings are defined in each service's `application.yml`.

## Endpoints

### Resource Service

- `POST /resources` — Upload MP3 file
- `GET /resources/{id}` — Download MP3 file
- `DELETE /resources?id=1,2,3` — Delete MP3 files and metadata

### Song Service

- `POST /songs` — Create song metadata
- `GET /songs/{id}` — Retrieve song metadata
- `DELETE /songs?id=1,2,3` — Delete song metadata

## Technologies

- Java 21, Spring Boot 4.0.2
- PostgreSQL 18 (Docker Compose)
- Apache Tika (metadata extraction)
- Gradle (multi-module)
- Lombok, JPA, Bean Validation
