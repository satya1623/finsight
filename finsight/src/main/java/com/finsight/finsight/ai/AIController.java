package com.finsight.finsight.ai;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class AIController {

    private final AiServices aiService;

    public AIController(AiServices aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public AiResponse askQuestion(@RequestBody AiReq request) throws Exception {

        return aiService.askAI(request);

    }

}