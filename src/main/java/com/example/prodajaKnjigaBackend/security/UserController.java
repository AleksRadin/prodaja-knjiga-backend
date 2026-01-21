package com.example.prodajaKnjigaBackend.security;

import com.example.prodajaKnjigaBackend.security.DTO.*;
import com.example.prodajaKnjigaBackend.security.service.AuthenticationService;
import com.example.prodajaKnjigaBackend.security.service.JWTService;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/auth")
@AllArgsConstructor
public class UserController {
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (!(userDetails instanceof UserEntity authenticatedUser)) {
            throw new RuntimeException("Principal is not an instance of User entity!");
        }
        String jwtToken = jwtService.generateToken(authenticatedUser);

        AuthenticationResponse response = new AuthenticationResponse(
                jwtToken,
                authenticatedUser.getId(),
                authenticatedUser.getFirstname() + " " + authenticatedUser.getLastname(),
                authenticatedUser.getRole()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserDetails(@PathVariable Long userId) {
        UserResource userResource = userService.findUserResourceDtoById(userId);
        return ResponseEntity.ok(userResource);
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request) {
        authenticationService.changePassword(request);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/request-reset/{email}")
    public ResponseEntity<String> requestPasswordReset(@PathVariable String email) {
        authenticationService.createResetRequestReport(email);

        return ResponseEntity.ok("Reset request submitted to Admin.");
    }

}
