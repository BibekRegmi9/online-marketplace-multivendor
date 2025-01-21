package com.bibek.service.seller.impl;

import com.bibek.config.JwtProvider;
import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.AccountStatus;
import com.bibek.enums.USER_ROLE;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.*;
import com.bibek.repository.AddressRepository;
import com.bibek.repository.SellerRepository;
import com.bibek.repository.VerificationCodeRepository;
import com.bibek.service.seller.SellerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private final CustomMessageSource customMessageSource;
    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    public SellerServiceImpl(CustomMessageSource customMessageSource, SellerRepository sellerRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AddressRepository addressRepository, VerificationCodeRepository verificationCodeRepository) {
        this.customMessageSource = customMessageSource;
        this.sellerRepository = sellerRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;

        this.addressRepository = addressRepository;
        this.verificationCodeRepository = verificationCodeRepository;
    }

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller request) {
        Optional<VerificationCode> verificationCode = Optional.ofNullable(verificationCodeRepository.findByEmail(request.getEmail()));
        if(verificationCode.isPresent()){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.DUPLICATE_EMAIL_VALIDATION));
        }

        Seller oldSeller = sellerRepository.findByEmail(request.getEmail());
        if(oldSeller != null){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.Seller, customMessageSource.get(MessageConstants.ERROR_ALREADY_EXIST)));
        }

        Address address = addressRepository.save(request.getPickupAddress());

        Seller seller =  Seller.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .sellerName(request.getSellerName())
                .pickupAddress(address)
                .GST(request.getGST())
                .role(USER_ROLE.ROLE_SELLER)
                .mobile(request.getMobile())
                .bankDetails(request.getBankDetails())
                .businessDetails(request.getBusinessDetails())
                .build();

        return sellerRepository.save(seller);
    }

    @Override
    public Seller getSellerById(Long id) {
        return sellerRepository.findById(id).orElseThrow(() -> new CustomRunTimeException(customMessageSource.get(MessageConstants.ERROR_DOESNT_EXIST)));
    }

    @Override
    public Seller getSellerByEmail(String email) {
        Seller seller = sellerRepository.findByEmail(email);
        if(seller == null){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.Seller, customMessageSource.get(MessageConstants.CRUD_NOT_EXIST)));
        }

        return  seller;
    }

    @Override
    public List<Seller> getAllSellerByAccountStatus(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    @Transactional
    public Seller updateSeller(Long id, Seller sellerRequest) {
        Seller existingSeller = getSellerById(id);
        if (existingSeller != null && sellerRequest != null) {

            // Update fields only if they are not null or empty
            if (sellerRequest.getSellerName() != null) {
                existingSeller.setSellerName(sellerRequest.getSellerName());
            }
            if (sellerRequest.getEmail() != null) {
                existingSeller.setEmail(sellerRequest.getEmail());
            }
            if(sellerRequest.getPassword() != null){
                existingSeller.setPassword(passwordEncoder.encode(sellerRequest.getPassword()));
            }
            if (sellerRequest.getMobile() != null) {
                existingSeller.setMobile(sellerRequest.getMobile());
            }
            if (sellerRequest.getRole() != null) {
                existingSeller.setRole(sellerRequest.getRole());
            }
            if (sellerRequest.getGST() != null && !sellerRequest.getGST().isEmpty()) {
                existingSeller.setGST(sellerRequest.getGST());
            }

            // Update Bank Details
            if (sellerRequest.getBankDetails() != null) {
                BankDetails bankDetails = existingSeller.getBankDetails();
                if (bankDetails == null) {
                    bankDetails = new BankDetails();
                }
                if (sellerRequest.getBankDetails().getBankName() != null) {
                    bankDetails.setBankName(sellerRequest.getBankDetails().getBankName());
                }
                if (sellerRequest.getBankDetails().getAccountHolderName() != null) {
                    bankDetails.setAccountHolderName(sellerRequest.getBankDetails().getAccountHolderName());
                }
                if (sellerRequest.getBankDetails().getAccountNumber() != null) {
                    bankDetails.setAccountNumber(sellerRequest.getBankDetails().getAccountNumber());
                }
                if (sellerRequest.getBankDetails().getIfscCode() != null) {
                    bankDetails.setIfscCode(sellerRequest.getBankDetails().getIfscCode());
                }
                existingSeller.setBankDetails(bankDetails);
            }

            // Update Pickup Address
            if (sellerRequest.getPickupAddress() != null) {
                Address pickupAddress = existingSeller.getPickupAddress();
                if (pickupAddress == null) {
                    pickupAddress = new Address();
                }
                if (sellerRequest.getPickupAddress().getAddress() != null) {
                    pickupAddress.setAddress(sellerRequest.getPickupAddress().getAddress());
                }
                if (sellerRequest.getPickupAddress().getMobile() != null) {
                    pickupAddress.setMobile(sellerRequest.getPickupAddress().getMobile());
                }
                if (sellerRequest.getPickupAddress().getCity() != null) {
                    pickupAddress.setCity(sellerRequest.getPickupAddress().getCity());
                }
                if (sellerRequest.getPickupAddress().getState() != null) {
                    pickupAddress.setState(sellerRequest.getPickupAddress().getState());
                }
                existingSeller.setPickupAddress(pickupAddress);
            }

            return sellerRepository.save(existingSeller);
        } else {
            throw new EntityNotFoundException("Seller not found with id: " + id);
        }
    }


    @Override
    public void deleteSeller(Long id) {
        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);

        return sellerRepository.save(seller);

    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);

        return sellerRepository.save(seller);
    }
}
