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



### Docker File
```bash

docker build -t sun-forecast .
docker run -p 8080:8080 -e OPENAI_API_KEY=sk-xxxx sun-forecast

```
### Run
```bash

mvn -U clean package && mvn spring-boot:run

```

### local host url and JSON Sample :
```bash
http://localhost:8080/api/sun-forecast?city=Mumbai


{
  "city": "Mumbai",
  "sunrise": "2025-09-17T06:26",
  "sunset": "2025-09-17T18:39",
  "enhanced_message": "Tomorrow in Mumbai, the sun will rise at 6:26 AM and set at 6:39 PM IST. Don't miss the beautiful golden hour!"
}

```

### health checks :
```bash
http://localhost:8080/actuator/health

http://localhost:8080/actuator/info

http://localhost:8080/actuator/health/liveness

http://localhost:8080/actuator/health/readiness
```