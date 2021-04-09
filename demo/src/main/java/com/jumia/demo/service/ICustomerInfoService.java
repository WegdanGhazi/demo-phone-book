package com.jumia.demo.service;

import com.jumia.demo.model.CustomerInfo;
import org.springframework.data.domain.Page;

public interface ICustomerInfoService {
    Page<CustomerInfo> findAll(int page, int size, String sortBy, Boolean desc);
    Page<CustomerInfo> findByCountryCodeOrValidity(int page, int size, String sortBy, Boolean desc, String q, Boolean valid);
}