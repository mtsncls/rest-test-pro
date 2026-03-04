# RestAssured Pro API Automation Framework

A robust, enterprise-grade API test automation framework built to test the [Swagger Petstore API](https://petstore.swagger.io/).

## 🚀 Tech Stack

- **Language:** Java 17
- **Build Tool:** Maven
- **Core Engine:** Rest-Assured 6.0.0
- **Test Runner:** TestNG
- **Reporting:** Allure Reports
- **Data Generation:** JavaFaker
- **Serialization:** Jackson
- **Boilerplate Reduction:** Lombok
- **Logging:** SLF4J + Logback

## 📂 Project Structure

```text
src/
├── main/java/com/quality/api/
│   ├── clients/        # Specific API clients (PetClient, UserClient, StoreClient, BaseClient)
│   ├── filters/        # RestAssured filters (CustomLoggingFilter)
│   ├── models/         # POJOs for serialization/deserialization (Pet, User, Order)
│   ├── specs/          # RestAssured Request/Response specifications
│   └── utils/          # Configs, Providers, Assertions, Data Generators
└── test/java/com/quality/api/tests/
    ├── AuthTests.java          # Security & Authentication flow tests
    ├── IntegrationTests.java   # End-To-End Business Workflows
    ├── NegativeTests.java      # Error handling & Negative Scenarios
    ├── PetTests.java           # Pet Management endpoints
    └── UserTests.java          # User Management endpoints
```

## ⚙️ Prerequisites

- **Java Development Kit (JDK) 17+**
- **Apache Maven 3.8+**
- **Allure Commandline** (optional, to serve reports directly from terminal)

## 🛠️ Configuration

The framework supports multiple environments out-of-the-box. Environment configurations are located under `src/main/resources/` (e.g., `config.qa.properties`, `config.dev.properties`).

You can select the environment using the `env` system property. Defaults to `qa`.

## 🏃 Running the Tests

To execute the full test suite, simply run:
```bash
mvn clean test
```

To run against a specific environment:
```bash
mvn clean test -Denv=dev
```

To run a specific test suite or class from CLI:
```bash
mvn clean test -Dtest=PetTests
```

## 📊 Allure Reporting

This framework is integrated with Allure to provide rich and interactive test reports.

1. Generate the report after running tests:
```bash
mvn allure:report
```

2. Serve and open the report directly in your default browser:
```bash
mvn allure:serve
```

Reports will be generated in `target/site/allure-maven-plugin/`.

## 🧬 Design Patterns & Principles

- **Controller/Client Pattern:** Endpoints and specifications are abstracted into specific Client classes (`PetClient`, `UserClient`).
- **Builder Pattern:** Used heavily for POJO instantiation via Lombok (`@Builder`).
- **Data-Driven Testing:** Using JavaFaker to create random data to avoid static data collision.
- **SOLID & DRY:** Clean architecture where shared specs go to `BaseSpec` and `BaseTest`.
- **Custom Logging:** Using `CustomLoggingFilter` to log complete payloads, headers, methods and response times seamlessly.

## 🤝 Contribution Guidelines

1. Make sure all tests pass before committing.
2. Keep assertions abstracted inside `CommonAssertions`.
3. Add `Allure` annotations (`@Epic`, `@Feature`, `@Story`, `@Step`) to any new test implementation for better traceability.
