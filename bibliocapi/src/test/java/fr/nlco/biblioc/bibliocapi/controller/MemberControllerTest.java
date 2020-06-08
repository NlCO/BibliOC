package fr.nlco.biblioc.bibliocapi.controller;

import fr.nlco.biblioc.bibliocapi.dto.MemberCredDto;
import fr.nlco.biblioc.bibliocapi.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    private final String testeur = "12345678";
    MemberController memberController;
    @Mock
    MemberService memberService;

    @BeforeEach
    void initTests() {
        memberController = new MemberController(memberService);
    }

    @Test
    void getMemberCred_OK() {
        //Arrange
        MemberCredDto memberCredDto = new MemberCredDto();
        memberCredDto.setMemberNumber(testeur);
        memberCredDto.setPassword("azertyui");
        doReturn(memberCredDto).when(memberService).getMemberCred(testeur);

        //Act
        ResponseEntity<MemberCredDto> result = memberController.getMemberCred(testeur);

        //Assert
        assertEquals(ResponseEntity.ok().build().getStatusCode(), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(testeur, result.getBody().getMemberNumber());
        assertEquals("azertyui", result.getBody().getPassword());
    }

    @Test
    void getMemberCred_Failed() {
        //Arrange
        doReturn(null).when(memberService).getMemberCred(testeur);

        //Act
        ResponseEntity<MemberCredDto> result = memberController.getMemberCred(testeur);

        //Assert
        assertEquals(ResponseEntity.badRequest().build().getStatusCode(), result.getStatusCode());
        assertNull(result.getBody());
    }
}