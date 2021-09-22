package org.acme;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Entity
public class Movie extends PanacheEntity {

    @Column(unique = true)
    @NotBlank
    public String title;
    @Min(1) @Max(5)
    public int rating;
    public String cover;

}
