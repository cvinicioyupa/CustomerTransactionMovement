package customer.movement.transaction.domain.ports.in;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

public interface ReportUseCase {
    Mono<Map<String, Object>> generateReport(int clientId, LocalDate startDate, LocalDate endDate, String format);
    byte[] convertToExcel(Map<String, Object> reportData);
}
