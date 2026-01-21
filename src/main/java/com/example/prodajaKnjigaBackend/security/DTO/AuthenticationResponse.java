package com.example.prodajaKnjigaBackend.security.DTO;

import com.example.prodajaKnjigaBackend.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Long id;
    private String name;
    private UserRole role;
}
