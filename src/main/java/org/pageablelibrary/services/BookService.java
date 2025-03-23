package org.pageablelibrary.services;

import lombok.RequiredArgsConstructor;
import org.pageablelibrary.entities.Book;
import org.pageablelibrary.repositories.AuthorRepository;
import org.pageablelibrary.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Книга с ID " + bookId + " не найдена"));
    }

    public Book updateBook(Long bookId, Book book) {
        if (!bookRepository.existsById(bookId)) {
            throw new NoSuchElementException("Книга с ID " + bookId + " не найдена");
        }
        book.setBookId(bookId);
        return bookRepository.save(book);
    }

    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new NoSuchElementException("Книга с ID " + bookId + " не найдена");
        }
        bookRepository.deleteById(bookId);
    }

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }
}

