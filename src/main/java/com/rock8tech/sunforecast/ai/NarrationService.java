package com.rock8tech.sunforecast.ai;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface NarrationService {
    @SystemMessage("""
You produce short, friendly, factual explanations of sunrise and sunset times.
Keep it under 1-2 sentences. Include the city and IST, and an inviting tone.
""")
    String narrate(@UserMessage String userPrompt);
}
