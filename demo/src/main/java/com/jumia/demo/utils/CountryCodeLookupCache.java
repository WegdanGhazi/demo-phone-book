package com.jumia.demo.utils;

import com.jumia.demo.model.CountryInfo;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class CountryCodeLookupCache {

    private Map<String, CountryInfo> countryCodeLookupMap = new HashMap<>();

    @PostConstruct
    public void initCountryLookupCache(){
        // Setting a hard Coded map based on the given data presented in the document of the test
        countryCodeLookupMap.put("237", new CountryInfo("Cameroon","237","\\(237\\)\\ ?[2368]\\d{7,8}$"));
        countryCodeLookupMap.put("251", new CountryInfo("Ethiopia","251","\\(251\\)\\ ?[1-59]\\d{8}$"));
        countryCodeLookupMap.put("212", new CountryInfo("Morocco","212","\\(212\\)\\ ?[5-9]\\d{8}$"));
        countryCodeLookupMap.put("258", new CountryInfo("Mozambique","258","\\(258\\)\\ ?[28]\\d{7,8}$"));
        countryCodeLookupMap.put("256", new CountryInfo("Uganda","256","\\(256\\)\\ ?\\d{9}$"));
    }

    public CountryInfo find(String key){
        return countryCodeLookupMap.get(key);
    }

    public Collection<CountryInfo> getValues(){
        return countryCodeLookupMap.values();
    }
}
