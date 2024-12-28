package com.bibek.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NotNull
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}
