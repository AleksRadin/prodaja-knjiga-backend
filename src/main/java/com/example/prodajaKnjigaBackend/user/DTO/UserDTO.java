package com.example.prodajaKnjigaBackend.user.DTO;

import com.example.prodajaKnjigaBackend.user.UserRole;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private UserRole role;
}
