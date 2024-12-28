package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "verification_code")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class VerificationCode extends BaseEntity {
    @Id
    @SequenceGenerator(name = "verification_code_seq_gen", sequenceName = "verification_code_seq")
    @GeneratedValue(generator = "verification_code_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private String otp;

    private String email;

    @OneToOne
    private User user;

    @OneToOne
    private Seller seller;
}
