package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transactions extends BaseEntity {
    @Id
    @SequenceGenerator(name = "transactions_seq_gen", sequenceName = "transactions_seq")
    @GeneratedValue(generator = "transactions_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @OneToOne
    private Order order;

    @ManyToOne
    private Seller seller;

    private LocalDateTime transactionDate = LocalDateTime.now();
}
