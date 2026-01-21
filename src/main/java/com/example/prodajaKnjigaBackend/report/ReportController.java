package com.example.prodajaKnjigaBackend.report;

import com.example.prodajaKnjigaBackend.report.DTO.ReportRequest;
import com.example.prodajaKnjigaBackend.report.domain.ReportEntity;
import com.example.prodajaKnjigaBackend.report.service.ReportService;
import com.example.prodajaKnjigaBackend.report.service.ReportServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/report")
@AllArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createReport(@RequestBody ReportRequest request) {
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }

            var newReport = reportService.createReport(request.getTitle(), request.getMessage());

            return ResponseEntity.ok(Map.of(
                    "message", "Success: Report submitted successfully!",
                    "data", newReport
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Could not submit report.");
        }
    }

    @PutMapping("/update/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateReport(@PathVariable Long reportId) {
        try {
//            if (true) {
//                throw new RuntimeException("Testiranje catch bloka");
//            }

            var updatedReport = reportService.updateReport(reportId);

            return ResponseEntity.ok(Map.of(
                    "message", "Success: Report marked as resolved!",
                    "data", updatedReport
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: Failed to update report status.");
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }
}
