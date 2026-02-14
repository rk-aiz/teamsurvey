package com.github.rk_aiz.teamsurvey.domain.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.rk_aiz.teamsurvey.domain.model.UserAccount;
import com.github.rk_aiz.teamsurvey.infrastructure.repository.AccountRepository;

@ExtendWith(MockitoExtension.class)
class LoginUserDatailsServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private LoginUserDatailsServiceImpl loginUserDatailsService;

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUserDetails() {
        // Arrange
        String username = "testuser";
        
        // Mockではなく実インスタンスを使用する
        // UserAccountはデータ保持が責務のRecord(値オブジェクト)であり、振る舞いを持たないためMock化するメリットが薄い
        UserAccount user = new UserAccount(
                username,
                "password",
                "test@example.com",
                "Test User",
                null, // createdAt (テストに関係ないためnull)
                null, // updatedAt
                true, // enabled
                List.of() // assignedGroups
        );

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(user));
        
        // Act
        UserDetails result = loginUserDatailsService.loadUserByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(accountRepository).findByUsername(username);
    }

    @Test
    void loadUserByUsername_NonExistingUser_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "unknown";
        when(accountRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> {
            loginUserDatailsService.loadUserByUsername(username);
        });

        assertEquals(username + " => 指定しているユーザー名は存在しません", exception.getMessage());
        verify(accountRepository).findByUsername(username);
    }
}