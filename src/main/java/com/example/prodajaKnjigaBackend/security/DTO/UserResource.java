package com.example.prodajaKnjigaBackend.security.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResource {
    private Long id;
    private String firstName;
    private String lastName;
}
