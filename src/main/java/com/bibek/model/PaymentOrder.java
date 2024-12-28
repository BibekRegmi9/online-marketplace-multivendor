package com.bibek.model;

import com.bibek.enums.PaymentMethod;
import com.bibek.enums.PaymentOrderStatus;
import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_order")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PaymentOrder extends BaseEntity {
    @Id
    @SequenceGenerator(name = "payment_order_seq_gen", sequenceName = "payment_order_seq")
    @GeneratedValue(generator = "payment_order_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private Long amount;

    private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    private String paymentLinkId;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Order> orders = new HashSet<>();
}
