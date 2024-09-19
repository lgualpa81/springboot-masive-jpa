package com.code;

import com.code.entity.Customer;
import com.code.repository.CustomerRepository;
import com.monitorjbl.xlsx.StreamingReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class MassiveJpaApplication implements CommandLineRunner {

  private final CustomerRepository customerRepository;

  public static void main(String[] args) {
    SpringApplication.run(MassiveJpaApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    long startTimeRead = System.currentTimeMillis();
    log.info("-> Reading File");
    InputStream is = new FileInputStream("../customers.xlsx");
    Workbook workbook = StreamingReader.builder()
                                       .rowCacheSize(50000)    // number of rows to keep in memory (defaults to 10), el tamano debe coincidir con el definido en properties batch_size
                                       .bufferSize(65536)     // buffer size to use when reading InputStream to file (defaults to 1024)
                                       .open(is);            // InputStream or File for XLSX file (required)

    List<Customer> customers = StreamSupport.stream(workbook.spliterator(), false)
                                            .flatMap(sheet -> StreamSupport.stream(sheet.spliterator(), false))
                                            .skip(1) //excluyo la 1ra fila
                                            .map(row -> {
                                              Customer customer = new Customer();
                                              customer.setId((long) row.getCell(0)
                                                                       .getNumericCellValue());
                                              customer.setName(row.getCell(1)
                                                                  .getStringCellValue());
                                              customer.setLastName(row.getCell(2)
                                                                      .getStringCellValue());
                                              customer.setAddress(row.getCell(3)
                                                                     .getStringCellValue());
                                              customer.setEmail(row.getCell(4)
                                                                   .getStringCellValue());
                                              return customer;
                                            })
                                            .toList();
    long endTimeRead = System.currentTimeMillis();
    log.info("-> Reading finished, time " + (endTimeRead - startTimeRead) + " ms");

    long startTimeWrite = System.currentTimeMillis();
    customerRepository.saveAll(customers);
    long endTimeWrite = System.currentTimeMillis();
    log.info("-> Write finished, time " + (endTimeWrite - startTimeWrite) + " ms");
  }
}
