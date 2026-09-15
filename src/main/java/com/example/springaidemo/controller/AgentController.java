package com.example.springaidemo.controller;

import com.example.springaidemo.agent.Agent;
import com.example.springaidemo.dto.AgentRequest;
import com.example.springaidemo.dto.AgentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST API Controller for Spring AI Agent
 * Exposes agent functionality through HTTP endpoints
 */
@Slf4j
@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final Agent agent;

    /**
     * Send a request to the agent
     * POST /api/agent/ask
     */
    @PostMapping("/ask")
    public ResponseEntity<AgentResponse> ask(@RequestBody AgentRequest request) {
        log.info("Received agent request: {}", request.getMessage());

        try {
            String response = agent.processRequest(request.getMessage());
            return ResponseEntity.ok(new AgentResponse(response, "success"));
        } catch (Exception e) {
            log.error("Error processing agent request", e);
            return ResponseEntity.status(500)
                    .body(new AgentResponse("Error: " + e.getMessage(), "error"));
        }
    }

    /**
     * Send a request to the agent with tool execution
     * POST /api/agent/ask-with-tools
     */
    @PostMapping("/ask-with-tools")
    public ResponseEntity<AgentResponse> askWithTools(@RequestBody AgentRequest request) {
        log.info("Received agent request with tools: {}", request.getMessage());

        try {
            String response = agent.processRequestWithToolExecution(request.getMessage());
            return ResponseEntity.ok(new AgentResponse(response, "success"));
        } catch (Exception e) {
            log.error("Error processing agent request with tools", e);
            return ResponseEntity.status(500)
                    .body(new AgentResponse("Error: " + e.getMessage(), "error"));
        }
    }

    /**
     * Health check endpoint
     * GET /api/agent/health
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Agent is running!");
    }
}