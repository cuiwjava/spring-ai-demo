package com.example.springaidemo.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

/**
 * Spring AI Agent Service
 * Orchestrates AI-powered agent interactions with tool support
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class Agent {

    private final ChatModel chatModel;
    private final ToolRegistry toolRegistry;

    /**
     * Process user request with agent
     * The agent can use tools to accomplish the task
     */
    public String processRequest(String userRequest) {
        log.info("Processing user request: {}", userRequest);

        try {
            ChatClient chatClient = ChatClient.create(chatModel);

            // Build prompt with available tools
            String systemPrompt = buildSystemPrompt();

            String response = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(userRequest)
                    .call()
                    .content();

            log.info("Agent response: {}", response);
            return response;

        } catch (Exception e) {
            log.error("Error processing request", e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Build system prompt for the agent
     */
    private String buildSystemPrompt() {
        return """
                You are a helpful AI assistant that can use various tools to help users.
                
                Available tools:
                1. get_current_time: Get the current date and time. Usage: [get_current_time]
                2. calculate: Perform mathematical calculations. Usage: [calculate: 2+3]
                3. get_weather: Get weather information for a location. Usage: [get_weather: Beijing]
                4. to_uppercase: Convert text to uppercase. Usage: [to_uppercase: hello]
                5. to_lowercase: Convert text to lowercase. Usage: [to_lowercase: HELLO]
                6. translate_english_to_chinese: Translate English to Chinese. Usage: [translate_english_to_chinese: hello]
                
                When you need to use a tool, format it as [tool_name] or [tool_name: parameter].
                Always explain what you did and the result.
                Be helpful, accurate, and concise.
                """;
    }

    /**
     * Advanced agent processing with tool execution
     * This method manually parses tool calls from the response
     */
    public String processRequestWithToolExecution(String userRequest) {
        log.info("Processing request with tool execution: {}", userRequest);

        String response = processRequest(userRequest);

        // Parse and execute tools from response
        String result = parseAndExecuteTools(response);

        log.info("Final result with tool execution: {}", result);
        return result;
    }

    /**
     * Parse and execute tools from agent response
     */
    private String parseAndExecuteTools(String response) {
        String result = response;

        // Pattern: [tool_name: parameter] or [tool_name]
        String pattern = "\\[([a-z_]+)(?:\\:\\s*([^\\]]+))?\\]";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(response);

        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String toolName = m.group(1);
            String parameter = m.group(2) != null ? m.group(2).trim() : "";

            log.info("Executing tool: {} with parameter: {}", toolName, parameter);
            String toolResult = toolRegistry.executeTool(toolName, parameter);

            m.appendReplacement(sb, java.util.regex.Matcher.quoteReplacement(toolResult));
        }
        m.appendTail(sb);

        return sb.toString();
    }
}