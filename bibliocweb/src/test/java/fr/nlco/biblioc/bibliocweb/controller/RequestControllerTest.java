package fr.nlco.biblioc.bibliocweb.controller;

import fr.nlco.biblioc.bibliocweb.model.Request;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    private final String testeur = "TU";
    private final Integer requestId = 1;
    RequestController requestController;
    @Mock
    BibliocapiProxy bibliocapiProxy;
    @Mock
    Model model;

    @BeforeEach
    void initTest() {
        requestController = new RequestController(bibliocapiProxy);
    }

    @Test
    void getMemberRequests() throws ParseException {
        //Arrange
        List<Request> requests = new ArrayList<>();
        requests.add(createRequest());
        requests.add(createRequestFromRequest(requests.get(0)));
        Authentication a = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(a);
        doReturn(testeur).when(a).getName();
        SecurityContextHolder.setContext(securityContext);
        doReturn(requests).when(bibliocapiProxy).getMemberRequests(testeur);
        doReturn(model).when(model).addAttribute("requests", requests);
        String expected = "requests";

        //Act
        String result = requestController.getMemberRequests(model);

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void cancelRequest() {
        //Arrange
        doAnswer(invocationOnMock -> null).when(bibliocapiProxy).cancelRequest(requestId);
        String expected = "redirect:/requests";

        //Act
        String result = requestController.cancelRequest(requestId);

        //Assert
        assertEquals(expected, result);
    }

    private Request createRequest() throws ParseException {
        Request request = new Request();
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        request.setRequestId(requestId);
        request.setTitle("title");
        request.setAuthor(testeur);
        request.setType("type");
        request.setReturnFirstDate(f.parse("01/06/2020"));
        request.setRank(1);
        request.setAlertDate(f.parse("01/06/2020"));
        return request;
    }

    private Request createRequestFromRequest(Request request) {
        Request request2 = new Request();
        request2.setRequestId(request.getRequestId());
        request2.setTitle(request.getTitle());
        request2.setAuthor(request.getAuthor());
        request2.setType(request.getType());
        request2.setReturnFirstDate(request.getReturnFirstDate());
        request2.setRank(request.getRank());
        request2.setAlertDate(request.getAlertDate());
        return request2;
    }
}