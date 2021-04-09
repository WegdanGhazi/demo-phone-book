package com.jumia.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jumia.demo.utils.PhoneNumberVisitor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
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

    // Initialization constructor to be used in tests only
    public CustomerInfo(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // extracting country code between "(" & ")" using StringUtils
    public String getCountryCode(){
        return StringUtils.substringBetween(phone, "(", ")");
    }

    public void accept(PhoneNumberVisitor visitor){
        visitor.visit(this);
    }
}
