package com.example.prodajaKnjigaBackend.report.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.report.DTO.ReportDTO;
import com.example.prodajaKnjigaBackend.report.ReportMapper;
import com.example.prodajaKnjigaBackend.report.ReportStatus;
import com.example.prodajaKnjigaBackend.report.domain.ReportEntity;
import com.example.prodajaKnjigaBackend.report.domain.ReportRepository;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReportServiceImpl implements ReportService{
    private final ReportRepository reportRepository;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ReportDTO createReport(String title, String message){
        UserEntity user = securityUtils.getAuthenticatedUser();

        ReportEntity reportEntity = new ReportEntity(user, title, message, LocalDateTime.now());
        return ReportMapper.toDto(reportRepository.save(reportEntity));
    }

    @Override
    public ReportDTO updateReport(Long reportId){
        ReportEntity reportEntity = reportRepository
                .findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        if (reportEntity.getTitle().equals("[PASSWORD RESET]")) {
            String email = reportEntity.getMessage().replace("Reset request for: ", "").trim();

            UserEntity userToReset = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found for reset"));

            String defaultPassword = email.split("@")[0];
            userToReset.setPassword(passwordEncoder.encode(defaultPassword));

            userRepository.save(userToReset);
        }

        reportEntity.setStatus(ReportStatus.CLOSED);
        reportEntity.setCreatedAt(LocalDateTime.now());
        return ReportMapper.toDto(reportRepository.save(reportEntity));
    }

    @Override
    public List<ReportDTO> getAllReports() {
        return reportRepository.findAll().stream()
                .map(ReportMapper::toDto)
                .collect(Collectors.toList());
    }

}
