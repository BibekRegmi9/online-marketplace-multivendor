package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "deals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Deal extends BaseEntity {
    @Id
    @SequenceGenerator(name = "deals_seq_gen", sequenceName = "deals_seq")
    @GeneratedValue(generator = "deals_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private Integer discount;

    @OneToOne
    private HomeCategory category;
}
