package com.example.demo.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VerificationCode {
    private String email;
    private String verificationCode;
}
