package org.objectmapper.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer {
    @Id
    private Long customerId;
    private String firstName;
    private String lastName;
    private String email;
    private String contactNumber;

}

