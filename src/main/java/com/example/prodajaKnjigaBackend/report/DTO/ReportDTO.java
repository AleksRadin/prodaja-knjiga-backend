package com.example.prodajaKnjigaBackend.report.DTO;

import com.example.prodajaKnjigaBackend.report.ReportStatus;
import com.example.prodajaKnjigaBackend.user.DTO.UserDTO;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDTO {
    private Long id;
    private UserDTO user;
    private String title;
    private String message;
    private LocalDateTime createdAt;
    private ReportStatus status;
}