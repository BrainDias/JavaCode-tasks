package org.pageablelibrary.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;

    @NotEmpty(message = "Title cannot be empty")
    private String title;
    private String genre;

    @NotNull(message = "Publication year cannot be null")
    private Integer publicationYear;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

}

