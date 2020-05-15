package fr.nlco.biblioc.bibliocapi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BibliocapiApplication.class, loader = SpringBootContextLoader.class)
@ActiveProfiles("test")
public class BibliocapiApplicationTests {

    @Test
    void contextLoads() {
    }

}
