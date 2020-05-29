package fr.nlco.biblioc.bibliocapi;

import org.junit.jupiter.api.Test;
import org.omg.CORBA.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BibliocapiApplication.class, loader = SpringBootContextLoader.class)
@ActiveProfiles("test")
public class BibliocapiApplicationTests {

    @Test
    void contextLoads(@Autowired ConfigurableApplicationContext env) {
        assertEquals(1, env.getEnvironment().getActiveProfiles().length);
        assertEquals("test", env.getEnvironment().getActiveProfiles()[0]);
    }

}
