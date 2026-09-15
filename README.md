# Spring AI Demo - Intelligent Agent

A demonstration project showcasing Spring AI capabilities with LLM integration, featuring an intelligent agent with tool support.

## Features

- **Spring AI Integration**: Leverages Spring AI for LLM integration
- **Intelligent Agent**: AI-powered agent that can reason and use tools
- **Tool Registry**: Extensible tool system for agent capabilities
- **REST API**: Easy-to-use HTTP endpoints for agent interaction
- **Multiple Tools**: Pre-built tools including:
  - `get_current_time`: Get current date and time
  - `calculate`: Perform mathematical calculations
  - `get_weather`: Get weather information
  - `to_uppercase`: Convert text to uppercase
  - `to_lowercase`: Convert text to lowercase
  - `translate_english_to_chinese`: Translate English to Chinese

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- OpenAI API key

## Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd spring-ai-demo
```

### 2. Configure OpenAI API Key
Set the environment variable with your OpenAI API key:

```bash
# Linux/Mac
export OPENAI_API_KEY=sk-your-api-key-here

# Windows (PowerShell)
$env:OPENAI_API_KEY='sk-your-api-key-here'
```

Alternatively, add to `application.yml`:
```yaml
spring:
  ai:
    openai:
      api-key: your-api-key-here
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Usage

### 1. Ask the Agent (Basic)
Send a request to the agent:

```bash
curl -X POST http://localhost:8080/api/agent/ask \
  -H "Content-Type: application/json" \
  -d '{"message":"What is the current time?"}'
```

### 2. Ask the Agent with Tools (Advanced)
The agent will execute tools and return results:

```bash
curl -X POST http://localhost:8080/api/agent/ask-with-tools \
  -H "Content-Type: application/json" \
  -d '{"message":"What time is it? And calculate 10 + 5 for me."}'
```

### 3. Health Check
```bash
curl http://localhost:8080/api/agent/health
```

## Example Requests

### Simple Question
```json
{
  "message": "Hello, what can you do?"
}
```

### Math Calculation
```json
{
  "message": "What is 2 times 3 plus 4?"
}
```

### Weather Query
```json
{
  "message": "What's the weather in Beijing?"
}
```

### Text Transformation
```json
{
  "message": "Convert 'hello world' to uppercase"
}
```

### Translation
```json
{
  "message": "How do you say 'hello' in Chinese?"
}
```

## Project Structure

```
spring-ai-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/springaidemo/
│   │   │       ├── Application.java              # Entry point
│   │   │       ├── agent/
│   │   │       │   ├── Agent.java               # Main agent service
│   │   │       │   └── ToolRegistry.java        # Tool management
│   │   │       ├── controller/
│   │   │       │   └── AgentController.java     # REST API endpoints
│   │   │       └── dto/
│   │   │           ├── AgentRequest.java        # Request DTO
│   │   │           └── AgentResponse.java       # Response DTO
│   │   └── resources/
│   │       └── application.yml                   # Application config
│   └── test/
│       └── java/
└── pom.xml                                       # Maven config
```

## Architecture

### Agent Component
The `Agent` class orchestrates AI interactions:
- Takes user requests
- Communicates with LLM (via Spring AI)
- Parses tool calls from responses
- Executes tools and provides results

### Tool Registry
The `ToolRegistry` class manages available tools:
- Registers tools with their implementations
- Executes tools on demand
- Handles tool parameters and results

### REST Controller
The `AgentController` exposes functionality:
- `/api/agent/ask` - Basic agent interaction
- `/api/agent/ask-with-tools` - Agent with tool execution
- `/api/agent/health` - Health check

## Extending the Agent

### Adding a New Tool

1. Add a tool function to `ToolRegistry.registerTools()`:
```java
tools.put("new_tool_name", input -> {
    // Implement your tool logic here
    String result = doSomething(input);
    log.info("Tool called: new_tool_name");
    return result;
});
```

2. Update the system prompt in `Agent.buildSystemPrompt()` to include the new tool

3. Use the tool by referencing it in requests:
```json
{
  "message": "Please use the new_tool_name to process something"
}
```

## Configuration

### application.yml Settings

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      base-url: https://api.openai.com/v1
      model: gpt-4

logging:
  level:
    com.example: DEBUG
    org.springframework.ai: DEBUG
```

## Technologies Used

- **Spring Boot 3.3.0** - Application framework
- **Spring AI 1.0.0-M1** - AI/LLM integration
- **OpenAI API** - LLM provider
- **Maven** - Build tool
- **Lombok** - Reduce boilerplate
- **SLF4J** - Logging

## Troubleshooting

### API Key Not Found
```
Ensure OPENAI_API_KEY environment variable is set or add it to application.yml
```

### Connection Error
```
Check your network connection and OpenAI API endpoint
```

### Tool Execution Failed
```
Check logs for specific tool error messages
Verify tool parameters are correctly formatted
```

## Future Enhancements

- [ ] Persistent conversation history
- [ ] Vector database integration for embeddings
- [ ] Function calling with structured outputs
- [ ] Multi-turn conversation support
- [ ] Tool authentication and security
- [ ] Performance monitoring and metrics
- [ ] Advanced error handling
- [ ] Custom model selection

## License

This project is open source and available under the MIT License.

## Support

For issues and questions, please open an issue on GitHub or contact the maintainers.