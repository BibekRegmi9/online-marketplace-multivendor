package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller_report")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport extends BaseEntity {
    @Id
    @SequenceGenerator(name = "seller_report_seq_gen", sequenceName = "seller_report_seq")
    @GeneratedValue(generator = "seller_report_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Seller seller;

    private Long totalEarnings = 0L;

    private Long totalSales = 0L;

    private Long totalRefunds = 0L;

    private Long totalTax = 0L;

    private Long netEarnings = 0L;

    private Long totalOrders = 0L;

    private Integer cancelledOrders = 0;

    private Integer totalTransactions = 0;
}
