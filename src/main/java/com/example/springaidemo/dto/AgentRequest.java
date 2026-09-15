package com.example.springaidemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Agent Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequest {
    private String message;
}