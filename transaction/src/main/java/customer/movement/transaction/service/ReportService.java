
package customer.movement.transaction.service;

import org.springframework.stereotype.Service;

import customer.movement.transaction.model.Account;
import customer.movement.transaction.model.Movement;
import customer.movement.transaction.repository.AccountRepository;
import customer.movement.transaction.repository.MovementRepository;


import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;

@Service
@Slf4j
public class ReportService {
    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

  
    public ReportService(AccountRepository accountRepository, MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    public Mono<Map<String, Object>> generateReport(int clientId, LocalDate startDate, LocalDate endDate, String format) {
        log.info("Generating report for client id: {} from {} to {}", clientId, startDate, endDate);

        List<Account> accounts = accountRepository.findByClientIdentification(clientId);
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("accounts", accounts.stream().map(account -> {
            Map<String, Object> accountData = new HashMap<>();
            accountData.put("account", account);
            accountData.put("movements", movementRepository.findByAccountNumberAndDateBetween(account.getNumber(), Timestamp.valueOf(startDate.atStartOfDay()), Timestamp.valueOf(endDate.atStartOfDay())));
            return accountData;
        }).collect(Collectors.toList()));

        return Mono.just(reportData);
    }

    public byte[] convertToExcel(Map<String, Object> reportData) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report");

            // Crear encabezados
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Account Number");
            headerRow.createCell(1).setCellValue("Account Type");
            headerRow.createCell(2).setCellValue("Initial Balance");
            headerRow.createCell(3).setCellValue("Movement Date");
            headerRow.createCell(4).setCellValue("Movement Type");
            headerRow.createCell(5).setCellValue("Movement Value");
            headerRow.createCell(6).setCellValue("Balance");

            // Llenar datos
            int rowNum = 1;
            List<Map<String, Object>> accounts = (List<Map<String, Object>>) reportData.get("accounts");
            for (Map<String, Object> accountData : accounts) {
                Account account = (Account) accountData.get("account");
                List<Movement> movements = (List<Movement>) accountData.get("movements");

                for (Movement movement : movements) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(account.getNumber());
                    row.createCell(1).setCellValue(account.getType());
                    row.createCell(2).setCellValue(account.getInitialBalance());
                    row.createCell(3).setCellValue(movement.getDate().toString());
                    row.createCell(4).setCellValue(movement.getType());
                    row.createCell(5).setCellValue(movement.getValue());
                    row.createCell(6).setCellValue(movement.getBalance());
                }
            }

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (IOException e) {
            log.error("Error generating Excel report", e);
            return new byte[0];
        }
    }
}
