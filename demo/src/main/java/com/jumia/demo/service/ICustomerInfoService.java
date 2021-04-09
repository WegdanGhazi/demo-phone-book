package com.jumia.demo.service;

import com.jumia.demo.model.CountryInfo;
import com.jumia.demo.model.CustomerInfo;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface ICustomerInfoService {
    Collection<CountryInfo> findCountries();
    Page<CustomerInfo> findAll(int page, int size, String sortBy, Boolean desc);
    Page<CustomerInfo> findByCountryCode(int page, int size, String sortBy, Boolean desc, String q, Boolean valid);

}