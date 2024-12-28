package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cart")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Cart  extends BaseEntity {

    @Id
    @SequenceGenerator(name = "cart_seq_gen", sequenceName = "cart_seq")
    @GeneratedValue(generator = "cart_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItems> cartItems = new HashSet<>();

    private double totalSellingPrice;

    private int totalItems;

    private int totalMrpPrice;

    private int discount;

    private String couponCode;
}
