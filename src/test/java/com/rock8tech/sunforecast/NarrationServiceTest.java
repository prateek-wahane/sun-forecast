package com.rock8tech.sunforecast;

import com.rock8tech.sunforecast.ai.StubNarrationService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class NarrationServiceTest {
    @Test
    void stubNarratesPromptVerbatim() {
        StubNarrationService svc = new StubNarrationService();
        String out = svc.narrate("Tomorrow in Berlin, the sun will rise at 7:12 AM IST and set at 5:49 PM IST.");
        assertThat(out).contains("Berlin").contains("7:12").contains("5:49");
    }
}
