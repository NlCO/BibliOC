package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    RequestController requestController;

    @Mock
    RequestService requestService;

    @BeforeEach
    public void initTests() {
        requestController = new RequestController(requestService);
    }

    @Test
    void cancelRequest_ReturnBadRequest_WhenRequestNotExist() {
        //Arrange
        doThrow(NullPointerException.class).when(requestService).cancelRequest(anyInt());

        //Act
        ResponseEntity<Void> response = requestController.cancelRequest(1);

        //Assert
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }
}