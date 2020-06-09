package fr.nlco.biblioc.bibliocweb.service;

import fr.nlco.biblioc.bibliocweb.model.Member;
import fr.nlco.biblioc.bibliocweb.proxies.BibliocapiProxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    CustomUserDetailsService customUserDetailsService;
    @Mock
    BibliocapiProxy bibliocapiProxy;

    @BeforeEach
    void initTest() {
        customUserDetailsService = new CustomUserDetailsService(bibliocapiProxy);
    }

    @Test
    void loadUserByUsername() {
        //Arrange
        Member member = new Member();
        member.setMemberNumber("12345678");
        member.setPassword("azertyui");
        doReturn(member).when(bibliocapiProxy).getMemberAuthByMemberNumber("12345678");

        //Act
        UserDetails userDetails = customUserDetailsService.loadUserByUsername("12345678");

        //Assert
        assertNotNull(userDetails);
    }
}