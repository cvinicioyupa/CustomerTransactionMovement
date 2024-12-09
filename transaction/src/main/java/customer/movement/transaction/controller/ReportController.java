
package customer.movement.transaction.controller;

import org.springframework.web.bind.annotation.*;

import customer.movement.transaction.service.ReportService;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;

import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;


@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;


    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{client-id}")
    public Mono<ResponseEntity<?>> getAccountReport(
            @PathVariable("client-id") int clientId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "format", defaultValue = "json") String format) {
        return reportService.generateReport(clientId, startDate, endDate, format)
            .map(report -> {
                if ("excel".equalsIgnoreCase(format)) {
                    // Lógica para retornar el archivo Excel
                    byte[] excelData = reportService.convertToExcel(report);
                    return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=report.xlsx") 
                        .body(excelData); 
                } else { 
                    return ResponseEntity.ok().body(report); 
                } 
            })
            .defaultIfEmpty(ResponseEntity.notFound().build()); 
            }

}