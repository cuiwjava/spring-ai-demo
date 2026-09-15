package com.example.springaidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentResponse {
    private String message;
    private String status;
}