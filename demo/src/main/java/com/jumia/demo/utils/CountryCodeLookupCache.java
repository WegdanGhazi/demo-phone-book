package com.jumia.demo.utils;

import com.jumia.demo.model.CountryInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CountryCodeLookupCache {

    Map<String, CountryInfo> countryMap = new HashMap<>();

    // For tests mocking
    public CountryCodeLookupCache(){
        initCountryLookupCache();
    }

    @PostConstruct
    public void initCountryLookupCache(){
        // Setting a hard Coded
        countryMap.put("237", new CountryInfo("Cameroon","237","\\(237\\)\\ ?[2368]\\d{7,8}$"));
        countryMap.put("251", new CountryInfo("Ethiopia","251","\\(251\\)\\ ?[1-59]\\d{8}$"));
        countryMap.put("212", new CountryInfo("Morocco","212","\\(212\\)\\ ?[5-9]\\d{8}$"));
        countryMap.put("258", new CountryInfo("Mozambique","258","\\(258\\)\\ ?[28]\\d{7,8}$"));
        countryMap.put("256", new CountryInfo("Uganda","256","\\(256\\)\\ ?\\d{9}$"));
    }

    public CountryInfo find(String key){
        return countryMap.get(key);
    }

    public Collection<CountryInfo> getValues(){
        return countryMap.values();
    }
}
