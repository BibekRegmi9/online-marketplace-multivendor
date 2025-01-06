package com.bibek.service.seller.impl;

import com.bibek.config.JwtProvider;
import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.AccountStatus;
import com.bibek.enums.USER_ROLE;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.model.Address;
import com.bibek.model.BankDetails;
import com.bibek.model.BusinessDetails;
import com.bibek.model.Seller;
import com.bibek.repository.AddressRepository;
import com.bibek.repository.SellerRepository;
import com.bibek.service.seller.SellerService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerServiceImpl implements SellerService {
    private final CustomMessageSource customMessageSource;
    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;

    public SellerServiceImpl(CustomMessageSource customMessageSource, SellerRepository sellerRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder, AddressRepository addressRepository) {
        this.customMessageSource = customMessageSource;
        this.sellerRepository = sellerRepository;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;

        this.addressRepository = addressRepository;
    }

    @Override
    public Seller getSellerProfile(String jwt) {
        String email = jwtProvider.getEmailFromToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller request) {
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
    public Seller updateSeller(Long id, Seller sellerRequest) {
        Seller existingSeller = getSellerById(id);
        if(existingSeller != null && sellerRequest!= null){

            String accountHolderName = sellerRequest.getBankDetails().getAccountHolderName();
            String accountNumber = sellerRequest.getBankDetails().getAccountNumber();
            String ifscCode = sellerRequest.getBankDetails().getIfscCode();

            Seller.SellerBuilder updatedSeller = Seller.builder()
                    .sellerName(sellerRequest.getSellerName())
                    .email(sellerRequest.getEmail())
                    .password(passwordEncoder.encode(sellerRequest.getPassword()))
                    .pickupAddress(sellerRequest.getPickupAddress())
                    .role(sellerRequest.getRole())
                    .mobile(sellerRequest.getMobile());

            if (sellerRequest.getGST() != null && !sellerRequest.getGST().isEmpty()) {
                updatedSeller.GST(sellerRequest.getGST());
            }

            // Assign bank details only if they are present and valid
            if (sellerRequest.getBankDetails() != null &&
                    sellerRequest.getBankDetails().getAccountHolderName() != null &&
                    !sellerRequest.getBankDetails().getAccountHolderName().isEmpty() &&
                    sellerRequest.getBankDetails().getAccountNumber() != null &&
                    !sellerRequest.getBankDetails().getAccountNumber().isEmpty() &&
                    sellerRequest.getBankDetails().getIfscCode() != null &&
                    !sellerRequest.getBankDetails().getIfscCode().isEmpty()) {
                updatedSeller.bankDetails(BankDetails.builder()
                                .accountHolderName(accountHolderName)
                                .accountNumber(accountNumber)
                                .ifscCode(ifscCode)
                        .build());

            }

            if(sellerRequest.getBusinessDetails() != null && sellerRequest.getBusinessDetails().getBusinessName() != null){
                BusinessDetails updatedBusinessDetails = sellerRequest.getBusinessDetails();
                updatedBusinessDetails.setBusinessName(sellerRequest.getBusinessDetails().getBusinessName());
                updatedSeller.businessDetails(updatedBusinessDetails);
            }

            if(sellerRequest.getPickupAddress() != null &&
                    sellerRequest.getPickupAddress().getAddress() != null &&
                    sellerRequest.getPickupAddress().getMobile() != null &&
                    sellerRequest.getPickupAddress().getCity() != null &&
                    sellerRequest.getPickupAddress().getState() != null
            ) {
                updatedSeller.pickupAddress(Address.builder()
                                .address(sellerRequest.getPickupAddress().getAddress())
                                .mobile(sellerRequest.getPickupAddress().getMobile())
                                .city(sellerRequest.getPickupAddress().getCity())
                                .state(sellerRequest.getPickupAddress().getState())
                        .build());
            }

            // Build the seller object
            Seller finalUpdatedSeller = updatedSeller.build();
            return sellerRepository.save(finalUpdatedSeller);
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
