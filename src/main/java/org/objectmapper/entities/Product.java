package org.objectmapper.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
    @Id
    private Long productId;
    @NotEmpty
    private String name;
    private String description;
    @Positive
    private Double price;
    @Positive
    private Integer quantityInStock;
}

