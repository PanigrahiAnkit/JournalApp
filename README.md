# Secure Journal Application

A secure, end-to-end encrypted (E2EE) Journal App built using Spring Boot and MongoDB.

## Table of Contents

- [Features](#features)
- [Tech Stack & Versions](#tech-stack--versions)
- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [License](#license)

---

## Features

- End-to-end encryption for journal entries
- RESTful API with Spring Boot
- MongoDB support for flexible data storage
- Secure authentication and authorization (Spring Security with JWT)
- Easy extensibility and maintainability

## Tech Stack & Versions

This project uses the following main technologies and dependencies:

| Dependency / Program                        | Version     |
|---------------------------------------------|-------------|
| Java                                        | 17          |
| Spring Boot (Parent & Starters)             | 3.4.8       |
| Spring Framework (Core/Web/Beans/etc.)      | 6.2.9       |
| Spring Data MongoDB                         | 4.4.8       |
| MongoDB Java Driver                         | 5.2.1       |
| Lombok                                      | 1.18.38     |
| Logback Classic/Core                        | 1.5.18      |
| Log4j to SLF4J                              | 2.24.3      |
| SLF4J API                                   | 2.0.17      |
| SnakeYAML                                   | 2.3         |
| Jackson (Core/Databind/Annotations)         | 2.18.4, 2.18.4.1 |
| Tomcat Embedded                             | 10.1.43     |
| Micrometer (Observation/Commons)            | 1.14.9      |
| Spring Security                             | 6.4.8       |
| JUnit (Spring Boot Starter Test)            | 3.4.8       |
| Jakarta Annotation API                      | 2.1.1       |

> For the full dependency tree, see the `pom.xml`.

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MongoDB instance (local or cloud)

### Build & Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/PanigrahiAnkit/JournalApp.git
   cd JournalApp
   ```
2. **Configure MongoDB**
   - Edit `src/main/resources/application.properties` to set your MongoDB connection string.

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **API Endpoints**
   - By default, the application runs on `http://localhost:8080/`.
   - API documentation can be found at `/swagger-ui.html` (requires Swagger dependency).

## Project Structure

```
journalApp/
├── src/
│   ├── main/
│   │   ├── java/com/myproject/journalApp/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/myproject/journalApp/
├── pom.xml
└── README.md
```

## License

This project is licensed under the MIT License.

---

> For questions or contributions, please open an issue or pull request on [GitHub](https://github.com/PanigrahiAnkit/JournalApp).
