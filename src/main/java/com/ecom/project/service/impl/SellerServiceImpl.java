package com.ecom.project.service.impl;

import com.ecom.project.config.JwtProvider;
import com.ecom.project.domain.AccountStatus;
import com.ecom.project.domain.USER_ROLE;
import com.ecom.project.entity.Address;
import com.ecom.project.entity.Seller;
import com.ecom.project.exceptions.SellerException;
import com.ecom.project.repository.AddressRepository;
import com.ecom.project.repository.SellerRepository;
import com.ecom.project.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
//    private final Seller seller;

    private final SellerRepository sellerRepository;

    private final AddressRepository addressRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;
    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller creteSeller(Seller seller) throws Exception {
        Seller sellerExist=sellerRepository.findByEmail(seller.getEmail());
        if (sellerExist!=null){
            throw new Exception("Seller Already Exist , Use Different email"+sellerExist);
        }
        Address address=addressRepository.save(seller.getPickupAddress());

        Seller newSeller=new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(address);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(USER_ROLE.ROLE_SELLER);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());


        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(long id) throws SellerException {
        Seller seller=sellerRepository.findById(id).orElseThrow(()-> new SellerException
                ("seller not found with id"+id));
        return seller ;
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
    Seller seller=sellerRepository.findByEmail(email);
    if (seller==null){
        throw new Exception("Seller not found");
    }
        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) throws Exception {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(long id, Seller seller) throws Exception {
        Seller existSeller=this.getSellerById(id);

        if (seller.getSellerName()!=null){
            existSeller.setSellerName(seller.getSellerName());
        }

        if (seller.getMobile()!=null){
            existSeller.setMobile(seller.getMobile());
        }

        if (seller.getEmail()!=null){
            existSeller.setEmail(seller.getEmail());
        }

        if (seller.getBusinessDetails()!=null  &&
            seller.getBusinessDetails().getBusinessName()!=null){
            existSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }

        if (seller.getBankDetails()!=null
            && seller.getBankDetails().getAccountHolderName()!=null
                && seller.getBankDetails().getIfscCode()!=null
                && seller.getBankDetails().getAccountNumber()!=null
        )
        {
            existSeller.getBankDetails().setAccountHolderName(seller.getBankDetails().getAccountHolderName());
            existSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
            existSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());

        }

        if (seller.getPickupAddress()!=null
            && seller.getPickupAddress().getAddress()!=null
            && seller.getPickupAddress().getMobile()!=null
            && seller.getPickupAddress().getCity()!=null
            && seller.getPickupAddress().getState()!=null
        ){
            existSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }

        if (seller.getGSTIN()!=null){
            existSeller.setGSTIN(seller.getGSTIN());
        }


        return sellerRepository.save(existSeller);
    }

    @Override
    public void deleteSeller(long id) throws Exception {

        Seller seller=getSellerById(id);
        sellerRepository.delete(seller);

    }

    @Override
    public Seller verifySeller(String email, String otp) throws Exception {
Seller seller=getSellerByEmail(email);
seller.setEmailVerified(true);

        return sellerRepository.save(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = this.getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(long sellerid, AccountStatus status) throws Exception {
        Seller seller=getSellerById(sellerid);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
