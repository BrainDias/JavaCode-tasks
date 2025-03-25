package org.pageablelibrary.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pageablelibrary.entities.Author;
import org.pageablelibrary.entities.Book;
import org.pageablelibrary.repositories.AuthorRepository;
import org.pageablelibrary.repositories.BookRepository;
import org.pageablelibrary.services.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;

    @BeforeEach
    public void setup() {
        author = new Author(1L, "John", "Doe", new ArrayList<>());
        book = new Book(1L, "Test Book", "Fiction", 2020, author);
    }

    @Test
    public void testGetAllBooks_Success() {
        List<Book> books = Arrays.asList(book, new Book(2L, "Another Book", "Science", 2015, author));
        Page<Book> bookPage = new PageImpl<>(books, PageRequest.of(0, 10), books.size());

        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);

        Page<Book> result = bookService.getAllBooks(PageRequest.of(0, 10));

        assertEquals(2, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getTitle());
        verify(bookRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    public void testGetBookById_Success() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book foundBook = bookService.getBookById(1L);

        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> bookService.getBookById(999L));
        verify(bookRepository, times(1)).findById(999L);
    }

    @Test
    public void testAddBook_Success() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book newBook = new Book(null, "New Book", "History", 2021, author);
        Book savedBook = bookService.addBook(newBook);

        assertNotNull(savedBook);
        assertEquals("Test Book", savedBook.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = bookService.updateBook(1L, book);

        assertNotNull(updatedBook);
        assertEquals("Test Book", updatedBook.getTitle());
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    public void testUpdateBook_NotFound() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> bookService.updateBook(999L, book));
        verify(bookRepository, times(1)).existsById(999L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    public void testDeleteBook_Success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteBook_NotFound() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> bookService.deleteBook(999L));
        verify(bookRepository, times(1)).existsById(999L);
        verify(bookRepository, never()).deleteById(999L);
    }
}
