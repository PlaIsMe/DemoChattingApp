package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private Long id;

    @NotBlank(message = "User name must not be blank")
    @Size(min = 3, message = "User name must at least 3 characters")
    private String username;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, message = "Password must be at least 6 characters")
    @Size(max = 8, message = "Password must be no more than 8 characters")
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
