package fr.nlco.biblioc.bibliocbatch.steps;

import fr.nlco.biblioc.bibliocbatch.proxies.BibliocapiProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshBooksRequestsTaskletTest {

    RefreshBooksRequestsTasklet refreshBooksRequestsTasklet;

    @Mock
    BibliocapiProxy bibliocapiProxy;

    @BeforeEach()
    public void initTest() {
        refreshBooksRequestsTasklet = new RefreshBooksRequestsTasklet(bibliocapiProxy);
    }

    @Test
    void execute() {
        //Arrange
        doReturn(ResponseEntity.ok().build()).when(bibliocapiProxy).refreshRequests();

        //Act
        RepeatStatus result = refreshBooksRequestsTasklet.execute(null, null);

        //Assert
        verify(bibliocapiProxy, times(1)).refreshRequests();
        assertEquals(RepeatStatus.FINISHED, result);
    }
}