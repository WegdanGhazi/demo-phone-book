package com.jumia.demo.controller;

import com.jumia.demo.model.CountryInfo;
import com.jumia.demo.model.CustomerInfo;
import com.jumia.demo.service.ICountryInfoService;
import com.jumia.demo.service.ICustomerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
public class CustomerInfoController {

    @Autowired
    private ICustomerInfoService service;

    @Autowired
    private ICountryInfoService countryInfoService;

    @GetMapping("/")
    public Page<CustomerInfo> findAll(@RequestParam int page,
                                      @RequestParam int size,
                                      @RequestParam(required = false) String sortBy,
                                      @RequestParam(required = false) Boolean desc) {
        return service.findAll(page, size, sortBy, desc);
    }

    @GetMapping("/search/")
    public Page<CustomerInfo> findByCountry(@RequestParam int page,
                                            @RequestParam int size,
                                            @RequestParam(required = false) String sortBy,
                                            @RequestParam(required = false) Boolean desc,
                                            @RequestParam(required = false) String q,
                                            @RequestParam(required = false) Boolean valid) {
        return service.findByCountryCodeOrValidity(page, size, sortBy, desc, q, valid);
    }

    @GetMapping("/countries/")
    public Collection<CountryInfo> findCountries() {
        return countryInfoService.findAllCountries();
    }
}
