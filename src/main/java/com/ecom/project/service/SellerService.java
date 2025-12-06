package com.ecom.project.service;

import com.ecom.project.domain.AccountStatus;
import com.ecom.project.entity.Seller;
import com.ecom.project.exceptions.SellerException;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    Seller creteSeller(Seller seller) throws Exception;
    Seller getSellerById(long id)throws SellerException;
    Seller getSellerByEmail(String email) throws Exception;
    List<Seller> getAllSellers(AccountStatus status) throws Exception;
    Seller updateSeller(long id,Seller seller) throws Exception;
    void deleteSeller(long id)throws Exception;
    Seller verifySeller(String email,String otp) throws Exception;
    Seller verifyEmail(String email,String otp) throws Exception;
    Seller updateSellerAccountStatus(long sellerid, AccountStatus status) throws Exception;
}
