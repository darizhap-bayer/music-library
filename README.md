
# Music Library System

## Overview
A microservices-based system for uploading, storing, browsing, and deleting MP3 files with automatic metadata extraction. The system consists of two independent services:
- **Resource Service**: Handles MP3 file storage and provides uploading, downloading, and deletion endpoints.
- **Song Service**: Manages song metadata and provides creation, retrieval, and deletion endpoints.

Each service uses its own PostgreSQL database. Services communicate via REST APIs.

## Quick Start

1. **Clone the repository**
	```bash
	git clone <repository-url>
	cd music-library
	```
2. **Start PostgreSQL databases**
	```bash
	docker compose up -d
	```
3. **Build the services**
	```bash
	./gradlew build
	```
4. **Start both services**
	```bash
	./gradlew :resource-service:bootRun
	./gradlew :song-service:bootRun
	```
5. **Test endpoints**
	- Upload MP3: `POST /resources` (audio/mpeg)
	- Get metadata: `GET /songs/{id}`
	- Download MP3: `GET /resources/{id}`
	- Delete: `DELETE /resources?id=1,2,3`

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
