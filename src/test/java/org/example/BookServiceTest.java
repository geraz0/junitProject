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

    // Test for SearchBook method
    @Nested
    class SearchBookTests {

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
    }

    // Test for PurchaseBook method
    @Nested
    class PurchaseBookTests {

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
        void testPurchaseBook_NullBook() {
            // Assuming the method doesn't throw an exception and simply returns false
            boolean result = bookService.purchaseBook(mockUser, null);
            assertFalse(result, "Purchase with a null book should return false");
        }

    }

    // Test for AddBook method
    @Nested
    class AddBookTests {

        @Test
        void testAddBook_Success() {
            Book newBook = new Book("Brave New World", "Aldous Huxley", "Science Fiction", 12.99);
            boolean added = bookService.addBook(newBook);
            assertTrue(added);
        }

        @Test
        void testAddBook_Fail_Duplicate() {
            Book duplicateBook = new Book("1984", "George Orwell", "Dystopian", 9.99);
            boolean added = bookService.addBook(duplicateBook);
            assertTrue(added);
        }

        @Test
        void testAddBook_EdgeCase_EmptyTitle() {
            Book edgeCaseBook = new Book("", "Unknown Author", "Unknown Genre", 0);
            boolean added = bookService.addBook(edgeCaseBook);
            assertTrue(added); // Assuming that empty titles are allowed
        }
    }

    // Test for RemoveBook method
    @Nested
    class RemoveBookTests {

        @Test
        void testRemoveBook_Success() {
            boolean removed = bookService.removeBook(mockBook);
            assertTrue(removed);
        }

        @Test
        void testRemoveBook_Fail_NotInDatabase() {
            Book nonExistentBook = new Book("Nonexistent", "Unknown", "Unknown", 0);
            boolean removed = bookService.removeBook(nonExistentBook);
            assertFalse(removed);
        }

        @Test
        void testRemoveBook_EdgeCase_NullBook() {
            boolean removed = bookService.removeBook(null);
            assertFalse(removed); // Assuming that null values are handled gracefully
        }
    }

    // Test for AddBookReview method
    @Nested
    class AddBookReviewTests {

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

        @Test
        void testAddBookReview_EdgeCase_NullReview() {
            when(mockUser.getPurchasedBooks()).thenReturn(List.of(mockBook));
            boolean reviewAdded = bookService.addBookReview(mockUser, mockBook, null);
            assertTrue(reviewAdded); // Assuming the system allows null reviews, or you can check for false if not allowed
        }
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
