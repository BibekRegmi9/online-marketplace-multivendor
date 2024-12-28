package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "wish_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WishList extends BaseEntity {
    @Id
    @SequenceGenerator(name = "wish_list_seq_gen", sequenceName = "wish_list_seq")
    @GeneratedValue(generator = "wish_list_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @ManyToMany
    private Set<Product> products = new HashSet<>();
}
