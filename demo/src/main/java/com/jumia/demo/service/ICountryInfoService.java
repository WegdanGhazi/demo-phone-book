package com.jumia.demo.service;

import com.jumia.demo.model.CountryInfo;

import java.util.Collection;

public interface ICountryInfoService {
    Collection<CountryInfo> findAllCountries();
}
