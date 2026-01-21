package com.example.prodajaKnjigaBackend.report.domain;

import com.example.prodajaKnjigaBackend.report.ReportStatus;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "report")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 255)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private ReportStatus status;

    public ReportEntity(UserEntity user, String title, String message, LocalDateTime createdAt) {
        this.user = user;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.status = ReportStatus.OPEN;
    }
}
