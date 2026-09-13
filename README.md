# Spring MongoDB REST API

A small Java 21 REST API built with Spring Boot 4 and Spring Data MongoDB. It demonstrates a conventional Product CRUD workflow with request validation, service-layer mapping, consistent errors, Docker Compose for local MongoDB, and a Testcontainers integration test.

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker Desktop, required for the integration test and the local MongoDB Compose service

## Run locally

Start MongoDB:

```bash
docker compose up -d mongodb
```

Start the application:

```bash
mvn spring-boot:run
```

The API listens on `http://localhost:8080`. The default connection is `mongodb://localhost:27017/products`.

Open Swagger UI at `http://localhost:8080/swagger-ui.html` to explore and execute the API endpoints. The generated OpenAPI document is available at `http://localhost:8080/v3/api-docs`.

Configuration can be overridden with environment variables:

| Variable | Default | Purpose |
| --- | --- | --- |
| `MONGODB_URI` | `mongodb://localhost:27017/products` | MongoDB connection string |
| `SERVER_PORT` | `8080` | HTTP port |

## API examples

Create a product:

```bash
curl -i -X POST http://localhost:8080/api/products ^
	-H "Content-Type: application/json" ^
	-d "{\"name\":\"Mechanical Keyboard\",\"description\":\"Compact layout\",\"price\":99.99}"
```

List products, optionally filtering by name:

```bash
curl "http://localhost:8080/api/products?name=keyboard"
```

The remaining endpoints are:

- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

Product requests require a non-blank `name`, a `price`, and a price greater than zero. Missing products return `404`; invalid requests return `400` with field-level messages.

## Tests

Run unit and integration tests:

```bash
mvn test
```

The repository integration test starts MongoDB through Testcontainers, so Docker must be running. Stop the local database when finished:

```bash
docker compose down
```

## Project layout

```text
src/main/java/com/example/springmongodb/
	error/       shared API error handling
	product/     document, repository, service, controller, and DTOs
src/main/resources/application.yml
src/test/java/                unit and MongoDB integration tests
compose.yaml                  local MongoDB service
```