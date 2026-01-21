package prodajaKnjigaBackend.service;

import com.example.prodajaKnjigaBackend.exception.ResourceNotFoundException;
import com.example.prodajaKnjigaBackend.report.DTO.ReportDTO;
import com.example.prodajaKnjigaBackend.report.ReportStatus;
import com.example.prodajaKnjigaBackend.report.domain.ReportEntity;
import com.example.prodajaKnjigaBackend.report.domain.ReportRepository;
import com.example.prodajaKnjigaBackend.report.service.ReportServiceImpl;
import com.example.prodajaKnjigaBackend.security.util.SecurityUtils;
import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import com.example.prodajaKnjigaBackend.user.domain.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock private ReportRepository reportRepository;
    @Mock private SecurityUtils securityUtils;
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private ReportServiceImpl reportService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
    }

    @Test
    void createReport_success() {
        when(securityUtils.getAuthenticatedUser()).thenReturn(mockUser);

        ReportEntity savedReport = new ReportEntity(mockUser, "Naslov", "Poruka", LocalDateTime.now());
        savedReport.setId(100L);

        when(reportRepository.save(any(ReportEntity.class))).thenReturn(savedReport);

        ReportDTO result = reportService.createReport("Naslov", "Poruka");

        assertNotNull(result);
        assertEquals("Naslov", result.getTitle());
        verify(reportRepository).save(any(ReportEntity.class));
    }

    @Test
    void updateReport_closesReportSuccessfully() {
        Long reportId = 10L;
        ReportEntity existingReport = new ReportEntity();
        existingReport.setId(reportId);
        existingReport.setTitle("Običan report");
        existingReport.setStatus(ReportStatus.OPEN);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(existingReport));
        when(reportRepository.save(any(ReportEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        ReportDTO result = reportService.updateReport(reportId);

        assertNotNull(result);
        assertEquals(ReportStatus.CLOSED, result.getStatus());
        verify(reportRepository).save(existingReport);
    }

    @Test
    void updateReport_passwordResetLogic_success() {
        Long reportId = 20L;
        String targetEmail = "reset@user.com";
        ReportEntity resetReport = new ReportEntity();
        resetReport.setId(reportId);
        resetReport.setTitle("[PASSWORD RESET]");
        resetReport.setMessage("Reset request for: " + targetEmail);

        UserEntity userToReset = new UserEntity();
        userToReset.setEmail(targetEmail);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(resetReport));
        when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(userToReset));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(reportRepository.save(any(ReportEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        reportService.updateReport(reportId);

        verify(passwordEncoder).encode("reset");
        verify(userRepository).save(userToReset);
        assertEquals(ReportStatus.CLOSED, resetReport.getStatus());
    }

    @Test
    void getAllReports_returnsList() {
        ReportEntity r1 = new ReportEntity();
        when(reportRepository.findAll()).thenReturn(List.of(r1));

        List<ReportDTO> result = reportService.getAllReports();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(reportRepository).findAll();
    }

    @Test
    void updateReport_notFound_throwsException() {
        when(reportRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reportService.updateReport(1L));
    }
}