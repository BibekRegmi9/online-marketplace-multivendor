package com.bibek.controller.seller;

import com.bibek.config.JwtProvider;
import com.bibek.configuration.CustomMessageSource;
import com.bibek.constants.MessageConstants;
import com.bibek.enums.AccountStatus;
import com.bibek.exception.CustomRunTimeException;
import com.bibek.generics.controller.BaseController;
import com.bibek.generics.pojo.GlobalApiResponse;
import com.bibek.model.Seller;
import com.bibek.model.SellerReport;
import com.bibek.model.VerificationCode;
import com.bibek.repository.VerificationCodeRepository;
import com.bibek.request.LoginRequest;
import com.bibek.service.auth.AuthService;
import com.bibek.service.email.EmailService;
import com.bibek.service.seller.SellerService;
import com.bibek.utils.OtpUtil;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seller")
public class SellerController extends BaseController {

    private final SellerService sellerService;
    private final AuthService authService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomMessageSource customMessageSource;
    private static final String SELLER_PREFIX = "seller_";
    private final EmailService emailService;
    private final JwtProvider jwtProvider;


    public SellerController(SellerService sellerService, AuthService authService, VerificationCodeRepository verificationCodeRepository, CustomMessageSource customMessageSource, EmailService emailService, JwtProvider jwtProvider) {
        this.sellerService = sellerService;
        this.authService = authService;
        this.verificationCodeRepository = verificationCodeRepository;
        this.customMessageSource = customMessageSource;
        this.emailService = emailService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<GlobalApiResponse> sellerLogin(@RequestBody LoginRequest request){
        if(request.getOtp() == null){
            throw new CustomRunTimeException("Otp cannot be empty");
        }
        String email = request.getEmail();
//        String otp = request.getOtp();

        request.setEmail("seller_" + email);
        String token = authService.login(request);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.USER, customMessageSource.get(MessageConstants.CRUD_GET)), token));
    }

    @PatchMapping("/verify/{otp}")
    public ResponseEntity<GlobalApiResponse> verifySellerEmail(@PathVariable String otp){
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if(verificationCode == null || !verificationCode.getOtp().equals(otp)){
            throw new CustomRunTimeException(customMessageSource.get(MessageConstants.WRONG_OTP));
        }

        sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.VERIFY_OTP), null));
    }

    @PostMapping
    public ResponseEntity<GlobalApiResponse> createSeller(@RequestBody Seller seller) throws MessagingException {
        Seller createSeller = sellerService.createSeller(seller);
        String otp = OtpUtil.generateOtp();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject = "Mero Pasal Verification Code";
        String text = "Welcome to mero pasal, Verify your account using this link";
        String frontend_url = "http://localhost:3000/verify-seller/";

        emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);

        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_CREATE, customMessageSource.get(MessageConstants.Seller)), createSeller.getId()));

    }

    @GetMapping("/{id}")
    public ResponseEntity<GlobalApiResponse> getSellerById(@PathVariable Long id){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.Seller)), sellerService.getSellerById(id)));
    }

    @GetMapping("/profile")
    public ResponseEntity<GlobalApiResponse> getSellerByJwtToken(@RequestHeader("Authorization") String jwt) {
        Seller seller = sellerService.getSellerByEmail(jwtProvider.getEmailFromToken(jwt));
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET, customMessageSource.get(MessageConstants.Seller)), seller));
    }

//    @GetMapping("/report")
//    public ResponseEntity<GlobalApiResponse> getSellerReport(@RequestHeader("Authorization") String jwt){
//        Seller seller = sellerService.getSellerByEmail(jwtProvider.getEmailFromToken(jwt));
//        SellerReport sellerReport =
//    }

    @GetMapping
    public ResponseEntity<GlobalApiResponse> getAllSeller(@RequestParam(required = false)AccountStatus accountStatus){
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_GET_ALL, customMessageSource.get(MessageConstants.Seller)), sellerService.getAllSellerByAccountStatus(accountStatus)));
    }

    @PatchMapping
    public ResponseEntity<GlobalApiResponse> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller){
        Seller seller1 = sellerService.getSellerProfile(jwt);
        sellerService.updateSeller(seller1.getId(), seller);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_UPDATE, customMessageSource.get(MessageConstants.Seller)), null));
    }

    @DeleteMapping
    public ResponseEntity<GlobalApiResponse> deleteSeller(@PathVariable Long id){
        sellerService.deleteSeller(id);
        return ResponseEntity.ok(successResponse(customMessageSource.get(MessageConstants.CRUD_DELETE, customMessageSource.get(MessageConstants.Seller)), null));
    }

}
