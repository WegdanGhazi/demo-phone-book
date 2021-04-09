package com.jumia.demo.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CountryInfo {

    private String name;
    private String code;
    private String regex;

    public CountryInfo(String name, String code, String regex) {
        this.name = name;
        this.code = code;
        this.regex = regex;
    }
}
