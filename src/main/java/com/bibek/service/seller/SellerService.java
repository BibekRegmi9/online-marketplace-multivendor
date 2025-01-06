package com.bibek.service.seller;

import com.bibek.enums.AccountStatus;
import com.bibek.model.Seller;

import java.util.List;

public interface SellerService {
    Seller getSellerProfile(String jwt);
    Seller createSeller(Seller seller);
    Seller getSellerById(Long id);
    Seller getSellerByEmail(String email);
    List<Seller> getAllSellerByAccountStatus(AccountStatus status);
    Seller updateSeller(Long id, Seller seller);
    void deleteSeller(Long id);
    Seller verifyEmail(String email, String otp);
    Seller updateSellerAccountStatus(Long sellerId, AccountStatus status);
}
