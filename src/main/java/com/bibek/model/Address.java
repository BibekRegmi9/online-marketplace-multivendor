package com.bibek.model;

import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Address  extends BaseEntity {

    @Id
    @SequenceGenerator(name = "address_seq_gen", sequenceName = "address_seq")
    @GeneratedValue(generator = "address_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String locality;

    private String address;

    private String city;

    private String state;

    private String pinCode;

    private String mobile;
}
