package com.example.preordering.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String firstname;
    @NotBlank
    @Size(min = 4, max = 20)
    private String lastname;
    @NotBlank
    @Size(min = 4, max = 30)
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
    @NotBlank
    private String phoneNumber;
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;
    @NotBlank
    private String role;
}