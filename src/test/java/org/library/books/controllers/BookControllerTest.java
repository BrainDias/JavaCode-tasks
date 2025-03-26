package org.library.books.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.library.books.Services.BookService;
import org.library.books.entities.Book;
import org.library.books.controllers.BookController;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void testGetAllBooks_Success() {
        Book book1 = new Book(1L, "Title 1", "Author 1", 2020);
        Book book2 = new Book(2L, "Title 2", "Author 2", 2021);

        when(bookService.getAllBooks()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookController.getAllBooks();

        assertThat(books).hasSize(2);
        assertThat(books.get(0).getTitle()).isEqualTo("Title 1");
        assertThat(books.get(1).getAuthor()).isEqualTo("Author 2");
        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    public void testGetBookById_Success() {
        Book book = new Book(1L, "Title 1", "Author 1", 2020);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBookById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getTitle()).isEqualTo("Title 1");
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    public void testGetBookById_NotFound() {
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Book> response = bookController.getBookById(999L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(bookService, times(1)).getBookById(999L);
    }

    @Test
    public void testAddBook_Success() {
        Book book = new Book(null, "New Book", "New Author", 2023);

        doNothing().when(bookService).addBook(book);

        bookController.addBook(book);

        verify(bookService, times(1)).addBook(book);
    }

    @Test
    public void testUpdateBook_Success() {
        Book existingBook = new Book(1L, "Old Title", "Old Author", 2020);
        Book updatedBook = new Book(1L, "Updated Title", "Updated Author", 2021);

        when(bookService.getBookById(1L)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookService).updateBook(updatedBook);

        ResponseEntity<Void> response = bookController.updateBook(1L, updatedBook);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookService, times(1)).updateBook(updatedBook);
    }

    @Test
    public void testUpdateBook_NotFound() {
        Book book = new Book(999L, "Non-Existent Book", "Unknown", 2022);

        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = bookController.updateBook(999L, book);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(bookService, never()).updateBook(book);
    }

    @Test
    public void testDeleteBook_Success() {
        when(bookService.getBookById(1L)).thenReturn(Optional.of(new Book(1L, "Title 1", "Author 1", 2020)));

        ResponseEntity<Void> response = bookController.deleteBook(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    public void testDeleteBook_NotFound() {
        when(bookService.getBookById(999L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = bookController.deleteBook(999L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(bookService, never()).deleteBook(999L);
    }
}
