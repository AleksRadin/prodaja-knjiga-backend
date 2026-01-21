package com.example.prodajaKnjigaBackend.user.service;

import com.example.prodajaKnjigaBackend.security.DTO.UserResource;
import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;

public interface UserService {
    UserResource findUserResourceDtoById(Long userId);
    UserDTO findUserDtoById(Long userId);
}
