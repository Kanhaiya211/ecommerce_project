package com.ecom.project.service.impl;

import com.ecom.project.entity.Seller;
import com.ecom.project.entity.SellerReport;
import com.ecom.project.repository.SellerReportRepository;
import com.ecom.project.service.SellerReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {

    private final SellerReportRepository sellerReportRepository;

    @Override
    public SellerReport getSellerReport(Seller seller) throws Exception {
        SellerReport sellerReport=sellerReportRepository.findBySellerId(seller.getId());
        if (sellerReport==null){
            SellerReport newReport=new SellerReport();
            newReport.setSeller(seller);
        }
        return sellerReport;
    }

    @Override
    public SellerReport updateSellerReport(SellerReport sellerReport) throws Exception {

        return sellerReportRepository.save(sellerReport);
    }
}
