package customer.movement.transaction.infrastructure.web.controller;

import customer.movement.transaction.domain.ports.in.ReportUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    
    private final ReportUseCase reportUseCase;

    @GetMapping
    public Mono<ResponseEntity<byte[]>> generateReport(
            @RequestParam int clientId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "json") String format) {
        
        return reportUseCase.generateReport(clientId, startDate, endDate, format)
                .map(reportData -> {
                    if ("excel".equalsIgnoreCase(format)) {
                        byte[] excelData = reportUseCase.convertToExcel(reportData);
                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                        headers.setContentDispositionFormData("attachment", "report.xlsx");
                        return new ResponseEntity<>(excelData, headers, HttpStatus.OK);
                    } else {
                        return ResponseEntity.ok().body(reportData.toString().getBytes());
                    }
                });
    }
}
