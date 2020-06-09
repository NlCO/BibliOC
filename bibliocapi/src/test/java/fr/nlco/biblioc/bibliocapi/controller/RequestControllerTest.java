package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.MemberRequestDto;
import fr.nlco.biblioc.bibliocapi.service.BatchService;
import fr.nlco.biblioc.bibliocapi.service.RequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class RequestControllerTest {

    RequestController requestController;

    @Mock
    RequestService requestService;

    @Mock
    BatchService batchService;

    @BeforeEach
    public void initTests() {
        requestController = new RequestController(requestService, batchService);
    }

    @Test
    void cancelRequest_ReturnBadRequest_WhenRequestNotExist() {
        //Arrange
        doThrow(NullPointerException.class).when(requestService).cancelRequest(anyInt(), anyBoolean());

        //Act
        ResponseEntity<Void> response = requestController.cancelRequest(1);

        //Assert
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    void refreshRequest_ReturnBadRequest_WhenRefreshRequestFail() {
        //Arrange
        doThrow(NullPointerException.class).when(batchService).refreshBookRequests();

        //Act
        ResponseEntity<Void> response = requestController.refreshRequests();

        //Assert
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    void getMemberRequests_ReturnNoContent_WhenNoResquestForUser() {
        //Arrange
        doReturn(null).when(requestService).getMemberRequests(anyString());

        //Act
        ResponseEntity<List<MemberRequestDto>> response = requestController.getMemberRequests("12345678");

        //Assert
        assertEquals(ResponseEntity.noContent().build().getStatusCode(), response.getStatusCode());
    }
}