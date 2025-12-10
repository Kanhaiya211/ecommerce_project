package com.ecom.project.controller;

import com.ecom.project.config.JwtProvider;
import com.ecom.project.domain.AccountStatus;
import com.ecom.project.entity.Seller;
import com.ecom.project.entity.SellerReport;
import com.ecom.project.entity.VerificationCode;
import com.ecom.project.exceptions.SellerException;
import com.ecom.project.repository.VerificationCodeRepository;
import com.ecom.project.service.EmailService;
import com.ecom.project.service.SellerReportService;
import com.ecom.project.service.SellerService;
import com.ecom.project.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;
    private final EmailService emailService;
    private final VerificationCodeRepository verificationCodeRepository;
//    private final VerificationService verificationService;
    private final JwtProvider jwtProvider;
//    private final CustomeUserServiceImplementation customeUserServiceImplementation;



    @PatchMapping("/verify/{otp}")
    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception {
        VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

        if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
            throw new Exception("wrong otp...");
        }

        Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception{
        Seller savedSeller=sellerService.creteSeller(seller);

        VerificationCode verificationCode=new VerificationCode();
        String otp= OtpUtil.generateOtp();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);
        String subject="Ecom email verification code ";
        String text="Welcome to Ecom verify using this link";
        String frontend_url="http://localhost:3000/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),subject,text);
        return  new ResponseEntity<>(savedSeller,HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id)throws SellerException {

        Seller seller=sellerService.getSellerById(id);

        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerJwt(@RequestHeader ("Authorization") String jwt)throws Exception{
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        Seller seller=sellerService.getSellerByEmail(email);

        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @GetMapping("/report")
    public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt)throws Exception{

        Seller seller=sellerService.getSellerProfile(jwt);
        SellerReport report=sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report,HttpStatus.OK);
    }
@GetMapping
    public ResponseEntity<List<Seller>> getAllSellers(@RequestParam (required = false)AccountStatus status)throws Exception
    {
        List<Seller> sellers=sellerService.getAllSellers(status);
        return ResponseEntity.ok(sellers);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(
            @RequestHeader("Authorization") String jwt, @RequestBody Seller seller) throws Exception {

        Seller profile = sellerService.getSellerProfile(jwt);
        Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    public ResponseEntity<Void>deleteSeller(@PathVariable long id)throws Exception{
sellerService.deleteSeller(id);
        return ResponseEntity.noContent().build();
    }

}
