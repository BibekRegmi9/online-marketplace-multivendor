package com.bibek.repository;

import com.bibek.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository  extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByEmail(String email);
}
