package fr.nlco.biblioc.bibliocapi.service;

import fr.nlco.biblioc.bibliocapi.dto.MemberCredDto;
import fr.nlco.biblioc.bibliocapi.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    void getMemberCred() {
        //Arrange
        memberService = new MemberServiceImpl(memberRepository);
        doReturn(Optional.empty()).when(memberRepository).findByMemberNumber(anyString());

        //Act
        MemberCredDto memberCredDto = memberService.getMemberCred("12345678");

        //Assert
        assertNull(memberCredDto);
    }
}