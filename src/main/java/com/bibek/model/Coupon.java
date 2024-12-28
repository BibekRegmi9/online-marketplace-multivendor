package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Coupon extends BaseEntity {
    @Id
    @SequenceGenerator(name = "coupons_seq_gen", sequenceName = "coupons_seq")
    @GeneratedValue(generator = "coupons_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    private double discountPercentage;

    private LocalDate validStartDate;

    private LocalDate validEndDate;

    private double minimumOrderValue;

    private boolean asActive;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<User> usedByUsers = new HashSet<>();

}
