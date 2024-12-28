package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Review extends BaseEntity {
    @Id
    @SequenceGenerator(name = "reviews_seq_gen", sequenceName = "reviews_seq")
    @GeneratedValue(generator = "reviews_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private String reviewText;

    private double rating;

    @ElementCollection
    private List<String> productImages;

    @JsonIgnore
    @ManyToOne
//    @Column( nullable = false)
    private Product product;

    @ManyToOne
//    @Column( nullable = false)
    private User user;
}
