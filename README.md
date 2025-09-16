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

### Generative AI enhancement (LangChain4j)

**What we do**: after fetching sunrise/sunset from Open-Meteo, we ask an LLM (via LangChain4j) to turn the raw times into a short, friendly sentence. If AI is unavailable, we fall back to a safe stub so the API still works deterministically

**Where it lives**:
-`ai/NarrationService.java` – the L4J interface with system/user prompts:

```bash
public interface NarrationService {
    @SystemMessage("""
    You produce short, friendly, factual explanations of sunrise and sunset times.
    Keep it under 1–2 sentences. Include the city and IST, and an inviting tone.
    """)
    String narrate(@UserMessage String userPrompt);
}
```


- `ai/AiConfig.java` – wires LangChain4j’s `OpenAiChatModel` when `OPENAI_API_KEY` is present and `app.ai.enabled=true;` otherwise uses `StubNarrationService`:


```bash

@Bean
public NarrationService narrationService() {
    String apiKey = System.getenv("OPENAI_API_KEY");
    boolean enabled = /* from app.ai.enabled (true by default) */;
    if (!enabled || apiKey == null || apiKey.isBlank()) return new StubNarrationService();
    OpenAiChatModel chatModel = OpenAiChatModel.builder()
            .apiKey(apiKey).modelName("gpt-4o-mini").temperature(0.2).build();
    NarrationService real = AiServices.create(NarrationService.class, chatModel);
    // fail-safe wrapper so quota/network issues don’t break the endpoint
    return prompt -> {
        try { return real.narrate(prompt); } catch (Exception e) { return prompt; }
    };
}

```

- `service/ForecastService.java` – builds a clear prompt and calls the narrator:

```bash

String prompt = String.format("%s in %s, the sun will rise at %s and set at %s IST.",
    (idx==1?"Tomorrow":"Today"), city, prettyTime(sunrise), prettyTime(sunset));
String enhanced = narrationService.narrate(prompt);

```





### How to run with/without AI:

With AI (requires active quota):
`OPENAI_API_KEY=sk-... mvn spring-boot:run`

Without AI (stub fallback):
`APP_AI_ENABLED=false mvn spring-boot:run`


### Unit test coverage :

**Input validation**
`ForecastServiceTest.invalidCityRejected()` – rejects bad city names (400 via handler).

**API integration (mocked)**
`ForecastServiceTest.validFlowReturnsResponse()` – mocks geocode/forecast clients and verifies fields + narration present.

**Generative AI formatting**
`NarrationServiceTest.stubNarratesPromptVerbatim()` – confirms stub behavior is deterministic.
`PrettyTimeFormattingTest.formatsTo12HourAmPm()` – end-to-end check that the narration contains 12-hour AM/PM times (using “tomorrow” index).

**Controller behavior (reactive)**
`CityForecastControllerTest.badRequestWhenCityMissing()` – 400 on missing param.
`CityForecastControllerTest.returnsOkWhenCityValid()` – 200 with expected JSON fields.



### Run tests: 
```bash
mvn -U clean test