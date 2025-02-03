package com.bibek.service.sellerReport;

import com.bibek.model.Seller;
import com.bibek.model.SellerReport;

public interface SellerReportService {
    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);


}
