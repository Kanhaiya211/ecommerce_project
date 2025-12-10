package com.ecom.project.service;

import com.ecom.project.entity.Seller;
import com.ecom.project.entity.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport( Seller seller)throws Exception;
    SellerReport updateSellerReport(SellerReport sellerReport)throws Exception;
}
