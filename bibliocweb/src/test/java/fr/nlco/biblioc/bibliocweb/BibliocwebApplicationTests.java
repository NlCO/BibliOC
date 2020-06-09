package fr.nlco.biblioc.bibliocweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class BibliocwebApplicationTests {

    @Test
    void contextLoads(@Autowired ConfigurableApplicationContext env) {
        assertEquals(1, env.getEnvironment().getActiveProfiles().length);
        assertEquals("test", env.getEnvironment().getActiveProfiles()[0]);
    }
}
