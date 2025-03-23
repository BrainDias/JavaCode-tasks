package org.pageablelibrary.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
@Getter
@Setter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    private String firstName;
    private String lastName;

    @OneToMany(mappedBy = "author")
    private List<Book> books;

}

