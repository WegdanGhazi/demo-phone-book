package com.jumia.demo.utils;

import com.jumia.demo.model.CountryInfo;
import com.jumia.demo.model.CustomerInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PhoneNumberVisitor {

    @Autowired
    CountryCodeLookupCache cache;

    public void visit(CustomerInfo customerInfo){
        CountryInfo countryInfo = cache.find(customerInfo.getCountryCode());
        if(countryInfo == null)
            return;
        customerInfo.setValid(Pattern.matches(countryInfo.getRegex(), customerInfo.getPhone()));
        customerInfo.setCountry(countryInfo.getName());
    }
}
