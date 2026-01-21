package com.example.prodajaKnjigaBackend.review.DTO;

import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDTO {
    private Long id;
    private UserDTO reviewer;
    private UserDTO reviewed;
    private String comment;
    private LocalDateTime createdAt;
}
