package org.example;

import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    private BookService bookService;
    private User mockUser;
    private Book mockBook;

    @BeforeAll
    static void initAll() {
        System.out.println("Starting tests for BookService...");
    }

    @BeforeEach
    void setUp() {
        bookService = new BookService();
        mockUser = mock(User.class);
        mockBook = mock(Book.class);
        when(mockBook.getTitle()).thenReturn("1984");
        when(mockBook.getAuthor()).thenReturn("George Orwell");
        when(mockBook.getGenre()).thenReturn("Dystopian");
        bookService.addBook(mockBook);
    }

    @Test
    void testSearchBook_Success() {
        List<Book> foundBooks = bookService.searchBook("1984");
        assertEquals(1, foundBooks.size());
        assertEquals("1984", foundBooks.get(0).getTitle());
    }

    @Test
    void testSearchBook_NoResults() {
        List<Book> foundBooks = bookService.searchBook("Nonexistent");
        assertTrue(foundBooks.isEmpty());
    }

    @Test
    void testSearchBook_EmptyKeyword() {
        List<Book> foundBooks = bookService.searchBook("");
        assertEquals(1, foundBooks.size()); // Should return all books
    }

    @Test
    void testPurchaseBook_Success() {
        boolean purchaseSuccessful = bookService.purchaseBook(mockUser, mockBook);
        assertTrue(purchaseSuccessful);
    }

    @Test
    void testPurchaseBook_Fail_BookNotAvailable() {
        Book nonExistentBook = new Book("Nonexistent", "Unknown", "Unknown", 0);
        boolean purchaseSuccessful = bookService.purchaseBook(mockUser, nonExistentBook);
        assertFalse(purchaseSuccessful);
    }

    @Test
    void testPurchaseBook_ThrowsException_NullBook() {
        assertThrows(IllegalArgumentException.class, () -> bookService.purchaseBook(mockUser, null));
    }

    @Test
    void testAddBookReview_Success() {
        when(mockUser.getPurchasedBooks()).thenReturn(List.of(mockBook));
        boolean reviewAdded = bookService.addBookReview(mockUser, mockBook, "Great book!");
        assertTrue(reviewAdded);
    }

    @Test
    void testAddBookReview_Fail_NotPurchased() {
        when(mockUser.getPurchasedBooks()).thenReturn(List.of());
        boolean reviewAdded = bookService.addBookReview(mockUser, mockBook, "Great book!");
        assertFalse(reviewAdded);
    }

    @AfterEach
    void tearDown() {
        bookService = null;
        mockUser = null;
        mockBook = null;
        System.out.println("Cleaned up resources after each test.");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("Completed all tests for BookService.");
    }
}

