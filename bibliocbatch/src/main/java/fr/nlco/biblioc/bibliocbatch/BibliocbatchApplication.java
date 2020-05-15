package fr.nlco.biblioc.bibliocbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableFeignClients("fr.nlco.biblioc.bibliocbatch")
public class BibliocbatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliocbatchApplication.class, args);
    }
}
