package com.rock8tech.sunforecast.ai;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${app.ai.enabled:true}")
    private boolean enabled;

    @Value("${app.ai.model:gpt-4o-mini}")
    private String model;

    @Bean
    public NarrationService narrationService() {
        if (!enabled) {
            return new StubNarrationService();
        }
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return new StubNarrationService();
        }
        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName(model)
                .temperature(0.2)
                .build();

        return AiServices.create(NarrationService.class, chatModel);
    }
}
