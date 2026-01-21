package com.example.prodajaKnjigaBackend.report.service;

import com.example.prodajaKnjigaBackend.report.DTO.ReportDTO;

import java.util.List;

public interface ReportService {
    ReportDTO createReport(String title, String message);
    ReportDTO updateReport(Long reportId);
    List<ReportDTO> getAllReports();
}
