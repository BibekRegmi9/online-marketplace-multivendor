package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coupon extends BaseEntity {
    @Id
    @SequenceGenerator(name = "coupons_seq_gen", sequenceName = "coupons_seq")
    @GeneratedValue(generator = "coupons_seq_gen", strategy = GenerationType.AUTO)
    private Long id;


}
