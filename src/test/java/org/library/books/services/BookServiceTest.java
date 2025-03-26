package org.library.books.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.books.Services.BookService;
import org.library.books.entities.Book;
import org.library.books.repositories.BookRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() {
        book1 = new Book(1L, "Book One", "Author One", 2020);
        book2 = new Book(2L, "Book Two", "Author Two", 2021);
    }

    @Test
    public void testGetAllBooks_Success() {
        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertThat(books).hasSize(2);
        assertThat(books.get(0).getTitle()).isEqualTo("Book One");
        assertThat(books.get(1).getAuthor()).isEqualTo("Author Two");
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    public void testGetBookById_Found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));

        Optional<Book> result = bookService.getBookById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Book One");
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(999L);

        assertThat(result).isEmpty();
        verify(bookRepository, times(1)).findById(999L);
    }

    @Test
    public void testAddBook_Success() {
        bookService.addBook(book1);

        verify(bookRepository, times(1)).save(book1);
    }

    @Test
    public void testUpdateBook_Success() {
        bookService.updateBook(book2);

        verify(bookRepository, times(1)).update(book2);
    }

    @Test
    public void testDeleteBook_Success() {
        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).delete(1L);
    }
}
