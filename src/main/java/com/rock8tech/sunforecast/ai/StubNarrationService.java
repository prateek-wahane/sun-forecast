package com.rock8tech.sunforecast.ai;

public class StubNarrationService implements NarrationService {
@Override
public String narrate(String userPrompt) {
    return userPrompt.trim();
}
}
