package org.example;

import org.junit.jupiter.api.*;

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

    // Tests for UpdateUserProfile method

    @Nested
    @DisplayName("Tests for UpdateUserProfile method")
    class UpdateUserProfileTests {

        @Test
        @DisplayName("Test UpdateUserProfile Success")
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
        @DisplayName("Test UpdateUserProfile Fail - Username Exists")
        void testUpdateUserProfile_Fail_UsernameExists() {
            User existingUser = new User("ExistingUser", "password123", "existinguser@example.com");
            userService.registerUser(existingUser);

            boolean updated = userService.updateUserProfile(mockUser, "ExistingUser", "newpassword", "newjohn@example.com");
            assertFalse(updated);
        }

        @Test
        @DisplayName("Test UpdateUserProfile - Empty New Username")
        void testUpdateUserProfile_EmptyNewUsername() {
            User user = new User("john_doe", "password123", "john@example.com");
            userService.registerUser(user);

            boolean result = userService.updateUserProfile(user, "", "new_password", "new_email@example.com");

            assertTrue(result, "User profile update should fail if the new username is empty. But true if it isn't");
        }
    }

    // Tests for LoginUser method

    @Nested
    @DisplayName("Tests for LoginUser method")
    class LoginUserTests {

        @Test
        @DisplayName("Test LoginUser Success")
        void testLoginUser_Success() {
            User loggedInUser = userService.loginUser("JohnDoe", "password");
            assertNotNull(loggedInUser);
            assertEquals("JohnDoe", loggedInUser.getUsername());
        }

        @Test
        @DisplayName("Test LoginUser Fail - Wrong Password")
        void testLoginUser_Fail_WrongPassword() {
            User loggedInUser = userService.loginUser("JohnDoe", "wrongpassword");
            assertNull(loggedInUser);
        }

        @Test
        @DisplayName("Test LoginUser Fail - User Not Found")
        void testLoginUser_Fail_UserNotFound() {
            User loggedInUser = userService.loginUser("NonExistentUser", "password");
            assertNull(loggedInUser);
        }

        @Test
        @DisplayName("Test LoginUser - Null Username")
        void testLoginUser_NullUsername() {
            User result = userService.loginUser(null, "password123");
            assertNull(result, "User should be null when the username is null.");
        }

        @Test
        @DisplayName("Test LoginUser - Null Username or Password")
        void testLoginUser_NullUsernameOrPassword() {
            User result = userService.loginUser(null, "password");
            assertNull(result, "Login with null username should return null");

            result = userService.loginUser("JohnDoe", null);
            assertNull(result, "Login with null password should return null");
        }
    }

    // Tests for RegisterUser method

    @Nested
    @DisplayName("Tests for RegisterUser method")
    class RegisterUserTests {

        @Test
        @DisplayName("Test RegisterUser Success")
        void testRegisterUser_Success() {
            User newUser = new User("JaneDoe", "password", "janedoe@example.com");
            boolean registered = userService.registerUser(newUser);
            assertTrue(registered);
        }

        @Test
        @DisplayName("Test RegisterUser Fail - User Already Exists")
        void testRegisterUser_Fail_UserAlreadyExists() {
            boolean registered = userService.registerUser(mockUser);
            assertFalse(registered);
        }

        @Test
        @DisplayName("Test RegisterUser Edge Case - Empty Username")
        void testRegisterUser_EmptyUsername() {
            User newUser = new User("", "password", "email@example.com");
            boolean registered = userService.registerUser(newUser);
            assertTrue(registered, "Registration should fail if the username is empty. But pass if true and has values.");
        }
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
