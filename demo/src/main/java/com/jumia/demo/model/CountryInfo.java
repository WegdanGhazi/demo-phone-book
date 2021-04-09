package com.jumia.demo.model;

public class CountryInfo {

    private String name;
    private String code;
    private String regex;

    public CountryInfo() {}

    public CountryInfo(String name, String code, String regex) {
        this.name = name;
        this.code = code;
        this.regex = regex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }
}
