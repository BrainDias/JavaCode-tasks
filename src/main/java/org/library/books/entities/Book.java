package org.library.books.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
public class Book {
    @Id
    Long id;

    String title;
    String author;
    Integer publicationYear;
}
