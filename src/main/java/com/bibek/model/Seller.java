package com.bibek.model;

import com.bibek.enums.AccountStatus;
import com.bibek.enums.USER_ROLE;
import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seller")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seller_seq_gen", sequenceName = "seller_seq")
    @GeneratedValue(generator = "seller_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private String sellerName;

    private String mobile;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Embedded
    private BusinessDetails businessDetails = new BusinessDetails();

    @Embedded
    private BankDetails bankDetails = new BankDetails();

    @OneToOne(cascade = CascadeType.ALL)
    private Address pickupAddress = new Address();

    private String GST;

    private USER_ROLE role = USER_ROLE.ROLE_SELLER;

    private boolean isEmailVerified = false;

    private AccountStatus accountStatus = AccountStatus.PENDING_VERIFICATION;
}
