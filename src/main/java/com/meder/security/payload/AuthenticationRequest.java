package com.meder.security.payload;

import com.meder.security.validation.Password;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
  @NotNull
  @Email
  private String email;
  @Password
  String password;
}
