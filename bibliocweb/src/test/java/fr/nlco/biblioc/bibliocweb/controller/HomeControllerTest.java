package fr.nlco.biblioc.bibliocweb.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Spy
    HomeController homeController;

    @Mock
    Model model;

    @Test
    void getHomePage() {
        //Arrange
        String expected = "home";

        //Act
        String result = homeController.getHomePage();

        //Assert
        assertEquals(expected, result);
    }

    @Test
    void login() {
        //Arrange
        String expected = "login";

        //Act
        String result = homeController.login(model, null);

        //assert
        assertEquals(expected, result);
        verify(model, times(0)).addAttribute(anyString(), anyString());
    }

    @Test
    void login_fail() {
        //Arrange
        String expected = "login";
        doReturn(model).when(model).addAttribute("error", "Saisie incorrecte de vos identifiants");

        //Act
        String result = homeController.login(model, "fail");

        //assert
        assertEquals(expected, result);
        verify(model, times(1)).addAttribute(anyString(), anyString());
    }
}