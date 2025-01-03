package com.bibek.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotNull
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}
