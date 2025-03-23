package org.pageablelibrary.controllers;

import lombok.RequiredArgsConstructor;
import org.pageablelibrary.entities.Book;
import org.pageablelibrary.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private BookService bookService;

    // Получить все книги с пагинацией и сортировкой
    @GetMapping
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    // Получить книгу по ID
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // Добавить новую книгу
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book addBook(@RequestBody Book book) {
        return bookService.addBook(book);
    }

    // Обновить информацию о книге
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    // Удалить книгу
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }
}
