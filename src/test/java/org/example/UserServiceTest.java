package org.example;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private UserService userService;
    private User mockUser;

    @BeforeAll
    static void initAll() {
        System.out.println("Starting tests for UserService...");
    }

    @BeforeEach
    void setUp() {
        userService = new UserService();
        mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("JohnDoe");
        when(mockUser.getPassword()).thenReturn("password");
        when(mockUser.getEmail()).thenReturn("johndoe@example.com");
        userService.registerUser(mockUser);
    }

    @Test
    void testUpdateUserProfile_Success() {
        when(mockUser.getUsername()).thenReturn("JohnDoe");
        boolean updated = userService.updateUserProfile(mockUser, "NewJohnDoe", "newpassword", "newjohn@example.com");
        assertTrue(updated);
        // Verify that the methods to set new values were called
        verify(mockUser).setUsername("NewJohnDoe");
        verify(mockUser).setPassword("newpassword");
        verify(mockUser).setEmail("newjohn@example.com");
    }

    @Test
    void testUpdateUserProfile_Fail_UsernameExists() {
        User existingUser = new User("ExistingUser", "password123", "existinguser@example.com");
        userService.registerUser(existingUser);

        boolean updated = userService.updateUserProfile(mockUser, "ExistingUser", "newpassword", "newjohn@example.com");
        assertFalse(updated);
    }

    @Test
    void testUpdateUserProfile_NullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserProfile(null, "NewUser", "newpassword", "newemail@example.com");
        });
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testRegisterUser_NullUser() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(null);
        });
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testLoginUser_NullUsernameOrPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser(null, "password");
        });
        assertEquals("Username and password cannot be null", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.loginUser("JohnDoe", null);
        });
        assertEquals("Username and password cannot be null", exception.getMessage());
    }

    @Test
    void testLoginUser_Success() {
        User loggedInUser = userService.loginUser("JohnDoe", "password");
        assertNotNull(loggedInUser);
        assertEquals("JohnDoe", loggedInUser.getUsername());
    }

    @Test
    void testLoginUser_Fail_WrongPassword() {
        User loggedInUser = userService.loginUser("JohnDoe", "wrongpassword");
        assertNull(loggedInUser);
    }

    @Test
    void testLoginUser_Fail_UserNotFound() {
        User loggedInUser = userService.loginUser("NonExistentUser", "password");
        assertNull(loggedInUser);
    }

    @Test
    void testRegisterUser_Success() {
        User newUser = new User("JaneDoe", "password", "janedoe@example.com");
        boolean registered = userService.registerUser(newUser);
        assertTrue(registered);
    }

    @Test
    void testRegisterUser_Fail_UserAlreadyExists() {
        boolean registered = userService.registerUser(mockUser);
        assertFalse(registered);
    }

    @AfterEach
    void tearDown() {
        userService = null;
        mockUser = null;
        System.out.println("Cleaned up resources after each test.");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Completed all tests for UserService.");
    }
}
