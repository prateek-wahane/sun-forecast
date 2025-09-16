# Sunrise & Sunset Forecast (Spring Boot + LangChain4j)

This project implements the **Generative AI Task: Sunrise and Sunset Forecast Application**. It exposes
a REST API that accepts a `city` name, calls **Open-Meteo** geocoding + forecast APIs, and then uses
**LangChain4j** to produce a friendly, human-readable explanation.

> Covered: input validation, Open-Meteo integration, LangChain4j narration with safe fallback, JSON output,
> unit tests, caching via Caffeine, Dockerfile, GitHub Actions CI, OpenAPI, and Postman.

---

## Quickstart

### Prereqs
- Java 17+
- Maven 3.9+
- (Optional) `OPENAI_API_KEY` env var to enable AI narration (fallback works without it).

### Run
```bash
mvn spring-boot:run
# or
make run
