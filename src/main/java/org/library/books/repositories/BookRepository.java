package org.library.books.repositories;

import org.library.books.entities.Book;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class BookRepository {
    private final JdbcTemplate jdbcTemplate;

    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Book> bookRowMapper = (rs, rowNum) ->
            new Book(rs.getLong("id"), rs.getString("title"), rs.getString("author"), rs.getInt("publication_year"));

    public List<Book> findAll() {
        return jdbcTemplate.query("SELECT * FROM books", bookRowMapper);
    }

    public Optional<Book> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM books WHERE id = ?", bookRowMapper, id)
                .stream().findFirst();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO books (title, author, publication_year) VALUES (?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getPublicationYear());
    }

    public void update(Book book) {
        jdbcTemplate.update("UPDATE books SET title = ?, author = ?, publication_year = ? WHERE id = ?",
                book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getId());
    }

    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }
}

