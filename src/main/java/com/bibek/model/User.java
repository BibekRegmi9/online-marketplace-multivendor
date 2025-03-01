package com.bibek.model;

import com.bibek.enums.USER_ROLE;
import com.bibek.response.UserResponse;
import com.bibek.utils.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "users_seq_gen", sequenceName = "users_seq")
    @GeneratedValue(generator = "users_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String email;

    private String fullName;

    private String mobile;

    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    @OneToMany
    private Set<Address> address = new HashSet<>();

    @ManyToMany
    @JsonIgnore
    private Set<Coupon> usedCoupons = new HashSet<>();

    public User(UserResponse userResponse){
        this.fullName = userResponse.getFullName();
        this.email = userResponse.getEmail();
        this.mobile = userResponse.getPhoneNo();

    }
}
