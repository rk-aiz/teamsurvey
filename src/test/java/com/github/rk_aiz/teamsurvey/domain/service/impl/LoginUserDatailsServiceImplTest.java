package com.github.rk_aiz.teamsurvey.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.rk_aiz.teamsurvey.domain.model.LoginUser;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class LoginUserDatailsServiceImplTest {

    @Mock
    private AccountRepository loginUserRepository;

    @InjectMocks
    private LoginUserDatailsServiceImpl loginUserDatailsService;

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
        // Arrange
        String username = "testuser";
        LoginUser mockUser = mock(LoginUser.class);
        when(loginUserRepository.findByUsername(username)).thenReturn(mockUser);

        // Act
        UserDetails result = loginUserDatailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(mockUser, result);
        verify(loginUserRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_NonExistingUser_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "unknown";
        when(loginUserRepository.findByUsername(username)).thenReturn(null);

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            loginUserDatailsService.loadUserByUsername(username);
        });

        assertEquals(username + " => 指定しているユーザー名は存在しません", exception.getMessage());
        verify(loginUserRepository).findByUsername(username);
    }
}