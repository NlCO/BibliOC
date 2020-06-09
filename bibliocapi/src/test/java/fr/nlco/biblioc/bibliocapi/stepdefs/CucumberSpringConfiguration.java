package fr.nlco.biblioc.bibliocapi.stepdefs;


import fr.nlco.biblioc.bibliocapi.BibliocapiApplication;
import io.cucumber.java.Before;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = BibliocapiApplication.class, loader = SpringBootContextLoader.class)
@ActiveProfiles("test")
public class CucumberSpringConfiguration {
    @Before
    public void contextLoad() {
        HttpServletRequest httpServletRequestMock = new MockHttpServletRequest();
        ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(httpServletRequestMock);
        RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    }
}
