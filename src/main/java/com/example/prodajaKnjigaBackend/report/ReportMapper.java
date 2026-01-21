package com.example.prodajaKnjigaBackend.report;

import com.example.prodajaKnjigaBackend.report.DTO.ReportDTO;
import com.example.prodajaKnjigaBackend.report.domain.ReportEntity;
import com.example.prodajaKnjigaBackend.user.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public static ReportDTO toDto(ReportEntity entity) {
        if (entity == null) return null;
        return ReportDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .createdAt(entity.getCreatedAt())
                .status(entity.getStatus())
                .user(UserMapper.toDto(entity.getUser()))
                .build();
    }
}