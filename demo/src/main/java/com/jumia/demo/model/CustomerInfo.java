package com.jumia.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jumia.demo.utils.PhoneNumberVisitor;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class CustomerInfo {

    @Id
    @JsonIgnore
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="phone")
    private String phone;

    @Transient
    private boolean valid;

    @Transient
    private String country;

    public CustomerInfo() {}

    public CustomerInfo(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public CustomerInfo(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // extracting country code between "(" & ")" using StringUtils
    public String getCountryCode(){
        return StringUtils.substringBetween(phone, "(", ")");
    }

    public void accept(PhoneNumberVisitor visitor){
        visitor.visit(this);
    }
}
