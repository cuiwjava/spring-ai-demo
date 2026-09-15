package com.example.springaidemo.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Tool registry for Spring AI Agent
 * Manages available tools that the agent can use
 */
@Slf4j
@Component
public class ToolRegistry {

    private final Map<String, Function<String, String>> tools = new HashMap<>();

    public ToolRegistry() {
        registerTools();
    }

    private void registerTools() {
        // Tool 1: Get current time
        tools.put("get_current_time", input -> {
            String currentTime = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            log.info("Tool called: get_current_time, result: {}", currentTime);
            return currentTime;
        });

        // Tool 2: Calculator
        tools.put("calculate", input -> {
            try {
                // Simple calculation: supports format like "2+3", "10-5", "3*4", "20/5"
                String result = evaluateExpression(input);
                log.info("Tool called: calculate, input: {}, result: {}", input, result);
                return result;
            } catch (Exception e) {
                log.error("Calculation error", e);
                return "Error: Invalid expression";
            }
        });

        // Tool 3: Get weather (mock)
        tools.put("get_weather", input -> {
            String weather = "Weather in " + input + ": Sunny, Temperature: 25°C, Humidity: 60%";
            log.info("Tool called: get_weather, location: {}, result: {}", input, weather);
            return weather;
        });

        // Tool 4: String conversion to uppercase
        tools.put("to_uppercase", input -> {
            String result = input.toUpperCase();
            log.info("Tool called: to_uppercase, input: {}, result: {}", input, result);
            return result;
        });

        // Tool 5: String conversion to lowercase
        tools.put("to_lowercase", input -> {
            String result = input.toLowerCase();
            log.info("Tool called: to_lowercase, input: {}, result: {}", input, result);
            return result;
        });

        // Tool 6: Translate (mock simple translation)
        tools.put("translate_english_to_chinese", input -> {
            Map<String, String> dictionary = new HashMap<>();
            dictionary.put("hello", "你好");
            dictionary.put("world", "世界");
            dictionary.put("thank you", "谢谢");
            dictionary.put("good morning", "早上好");
            
            String result = dictionary.getOrDefault(input.toLowerCase(), "未知单词");
            log.info("Tool called: translate_english_to_chinese, input: {}, result: {}", input, result);
            return result;
        });
    }

    public Map<String, Function<String, String>> getTools() {
        return tools;
    }

    public String executeTool(String toolName, String input) {
        Function<String, String> tool = tools.get(toolName);
        if (tool != null) {
            return tool.apply(input);
        }
        return "Tool not found: " + toolName;
    }

    private String evaluateExpression(String expression) throws Exception {
        // Simple expression evaluator (supports +, -, *, /)
        expression = expression.replaceAll("\\s+", "");
        
        // Validate input
        if (!expression.matches("[0-9+\\-*/().]+")) {
            throw new IllegalArgumentException("Invalid characters in expression");
        }

        // Use JavaScript engine or simple parsing
        try {
            // For simplicity, using a simple evaluation
            double result = evaluateSimple(expression);
            if (result == (long) result) {
                return String.valueOf((long) result);
            } else {
                return String.format("%.2f", result);
            }
        } catch (Exception e) {
            throw new Exception("Failed to evaluate expression: " + expression, e);
        }
    }

    private double evaluateSimple(String expr) throws Exception {
        // Simple calculator for basic operations
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < expr.length()) ? expr.charAt(pos) : -1;
            }

            boolean eat(char c) {
                while (ch == ' ') nextChar();
                if (ch == c) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < expr.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                while (true) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                while (true) {
                    if (eat('*')) x *= parseFactor();
                    else if (eat('/')) x /= parseFactor();
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') {
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(expr.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                return x;
            }
        }.parse();
    }
}