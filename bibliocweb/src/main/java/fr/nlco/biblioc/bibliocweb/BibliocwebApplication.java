package fr.nlco.biblioc.bibliocweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients("fr.nlco.biblioc")
public class BibliocwebApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliocwebApplication.class, args);
    }

}
