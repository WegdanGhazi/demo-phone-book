package com.jumia.demo.service;

import com.jumia.demo.model.CountryInfo;
import com.jumia.demo.utils.CountryCodeLookupCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CountryInfoService implements ICountryInfoService {

    @Autowired
    private CountryCodeLookupCache cache;

    /**
     *
     * @return A list of available countries, containing name, country code, and regex to mach number validity
     */
    @Override
    public Collection<CountryInfo> findAllCountries() {
        return cache.getValues();
    }
}
