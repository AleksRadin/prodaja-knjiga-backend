package com.example.prodajaKnjigaBackend.user.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.security.DTO.UserResource;
import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import com.example.prodajaKnjigaBackend.user.UserMapper;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public UserResource findUserResourceDtoById(Long userId) {
        UserEntity user =  userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik s ID-jem " + userId + " nije pronađen."));
        return new UserResource(
                user.getId(),
                user.getFirstname(),
                user.getLastname());

    }

    @Override
    public UserDTO findUserDtoById(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Korisnik nije pronađen."));
        return UserMapper.toDto(user);
    }
}
