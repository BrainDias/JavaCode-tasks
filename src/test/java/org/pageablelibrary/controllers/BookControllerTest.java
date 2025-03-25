package org.pageablelibrary.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.pageablelibrary.entities.Author;
import org.pageablelibrary.entities.Book;
import org.pageablelibrary.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    private Author author;
    private Book book1;
    private Book book2;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "John", "Doe", new ArrayList<>());
        book1 = new Book(1L, "Book One", "Fiction", 2000, author);
        book2 = new Book(2L, "Book Two", "Science", 2015, author);
    }

    @Test
    public void testGetAllBooks_Success() throws Exception {
        List<Book> books = Arrays.asList(book1, book2);
        Page<Book> page = new PageImpl<>(books, PageRequest.of(0, 10), books.size());

        given(bookService.getAllBooks(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].title").value("Book One"))
                .andExpect(jsonPath("$.content[1].author.firstName").value("John"));
    }

    @Test
    public void testGetBookById_Success() throws Exception {
        given(bookService.getBookById(1L)).willReturn(book1);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book One"))
                .andExpect(jsonPath("$.author.lastName").value("Doe"));
    }

    @Test
    public void testGetBookById_NotFound() throws Exception {
        given(bookService.getBookById(999L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

        mockMvc.perform(get("/books/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddBook_Success() throws Exception {
        Book newBook = new Book(null, "New Book", "History", 2021, author);
        Book savedBook = new Book(3L, "New Book", "History", 2021, author);

        given(bookService.addBook(any(Book.class))).willReturn(savedBook);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Book"))
                .andExpect(jsonPath("$.author.firstName").value("John"));
    }

    @Test
    public void testAddBook_ValidationError() throws Exception {
        Book invalidBook = new Book(null, "", "History", null, author);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").value("Title cannot be empty"))
                .andExpect(jsonPath("$.errors.publicationYear").value("Publication year cannot be null"));
    }

    @Test
    public void testUpdateBook_Success() throws Exception {
        Book updatedBook = new Book(1L, "Updated Book", "Science Fiction", 2022, author);
        given(bookService.updateBook(eq(1L), any(Book.class))).willReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author.firstName").value("John"));
    }

    @Test
    public void testDeleteBook_Success() throws Exception {
        doNothing().when(bookService).deleteBook(1L);

        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteBook_NotFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"))
                .when(bookService).deleteBook(999L);

        mockMvc.perform(delete("/books/999"))
                .andExpect(status().isNotFound());
    }
}