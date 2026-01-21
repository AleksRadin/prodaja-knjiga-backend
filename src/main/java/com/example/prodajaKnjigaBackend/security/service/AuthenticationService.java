package com.example.prodajaKnjigaBackend.security.service;

import com.example.prodajaKnjigaBackend.report.domain.ReportEntity;
import com.example.prodajaKnjigaBackend.report.domain.ReportRepository;
import com.example.prodajaKnjigaBackend.security.DTO.ChangePasswordRequest;
import com.example.prodajaKnjigaBackend.security.model.UserDetailsServiceImpl;
import com.example.prodajaKnjigaBackend.security.DTO.AuthenticationResponse;
import com.example.prodajaKnjigaBackend.security.DTO.RegisterRequest;
import com.example.prodajaKnjigaBackend.user.UserRole;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ReportRepository reportRepository;

    public AuthenticationResponse register(RegisterRequest request){
        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new IllegalStateException("Email already in use");
        }

        var user = new UserEntity();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());

        user.setRole(UserRole.REGULAR);

        UserEntity savedUser = userRepository.save(user);


        String jwtToken = jwtService.generateToken(savedUser);

        return new AuthenticationResponse(
                jwtToken,
                savedUser.getId(),
                savedUser.getFirstname() + " " + savedUser.getLastname(),
                savedUser.getRole()
        );

    }

    public void changePassword(ChangePasswordRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Incorrect old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void createResetRequestReport(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with this email does not exist."));

        ReportEntity resetReport = new ReportEntity(
                user,
                "[PASSWORD RESET]",
                "Reset request for: " + email,
                LocalDateTime.now()
        );

        reportRepository.save(resetReport);
    }

}
