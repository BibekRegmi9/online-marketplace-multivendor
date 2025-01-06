package com.bibek.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NotNull
@Builder
public class BankDetails {
    private String accountNumber;
    private String accountHolderName;
    private String ifscCode;
}
