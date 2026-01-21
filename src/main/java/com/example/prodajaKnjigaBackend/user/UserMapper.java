package com.example.prodajaKnjigaBackend.user;

import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserDTO toDto(UserEntity entity) {
        if (entity == null) return null;

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setFirstname(entity.getFirstname());
        dto.setLastname(entity.getLastname());
        dto.setEmail(entity.getEmail());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setRole(entity.getRole());

        return dto;
    }
}