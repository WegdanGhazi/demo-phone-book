package com.jumia.demo;

import com.jumia.demo.dao.CustomerInfoRepository;
import com.jumia.demo.model.CountryInfo;
import com.jumia.demo.model.CustomerInfo;
import com.jumia.demo.utils.CountryCodeLookupCache;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class CunstomerInfoControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CustomerInfoRepository repository;

    @Autowired
    private CountryCodeLookupCache countryCodeLookUp;

    @Before
    public void setUp(){
        // Creating 4 CusomerInfo instances with different validity and country data
        repository.save(new CustomerInfo(1l, "Valid from Mozambique", "(258) 847651504"));
        repository.save(new CustomerInfo(2l, "Valid from Uganda", "(256) 775069443"));
        repository.save(new CustomerInfo(3l, "Invalid from Morocco", "(212) 6007989253"));
        repository.save(new CustomerInfo(4l, "Invalid from Ethiopia", "(251) 9119454961"));
    }

    @Test
    public void testFindAll() throws Exception {
        // WHEN
        // Fetching data without filtering or sorting
        // THEN
        // All 4 instances are returned
        mvc.perform(get("/?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("4"));
    }

    @Test
    public void testSortAll() throws Exception {
        // WHEN
        // Fetching data without filtering but with a sort parameter: by phone number & asc
        // THEN
        // All 4 instances are returned sorted by phone number
        mvc.perform(get("/?page=0&size=10&sortBy=phone&desc=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("4"))
                .andExpect(jsonPath("$.content[0].country").value("Morocco"))
                .andExpect(jsonPath("$.content[1].country").value("Ethiopia"))
                .andExpect(jsonPath("$.content[2].country").value("Uganda"))
                .andExpect(jsonPath("$.content[3].country").value("Mozambique"));
    }

    @Test
    public void testValiditySearchForValidUsers() throws Exception {
        // GIVEN
        // 2 CunstomerInfo instances have valid phone numbers
        // WHEN
        // Fetching data with filtering by valid phone numbers
        // THEN
        // All 2 valid instances are returned
        mvc.perform(get("/search/?page=0&size=10&valid=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("2"))
                .andExpect(jsonPath("$.content[0].valid").value("true"))
                .andExpect(jsonPath("$.content[1].valid").value("true"));
    }

    @Test
    public void testValiditySearchAndSortForValidUsers() throws Exception {
        // GIVEN
        // 2 CunstomerInfo instances have valid phone numbers
        // WHEN
        // Fetching data with filtering by valid phone numbers & sorting by phone number
        // THEN
        // All 2 valid instances are returned, sorted by phone number
        mvc.perform(get("/search/?page=0&size=10&sortBy=phone&desc=false&valid=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("2"))
                .andExpect(jsonPath("$.content[0].valid").value("true"))
                .andExpect(jsonPath("$.content[0].country").value("Uganda"))
                .andExpect(jsonPath("$.content[1].valid").value("true"))
                .andExpect(jsonPath("$.content[1].country").value("Mozambique"));
    }

    @Test
    public void testValiditySearchForInvalidNumbers() throws Exception {
        // GIVEN
        // 2 CunstomerInfo instances have invalid phone numbers
        // WHEN
        // Fetching data with filtering by invalid phone numbers
        // THEN
        // All 2 invalid instances are returned
        mvc.perform(get("/search/?page=0&size=10&valid=false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("2"))
                .andExpect(jsonPath("$.content[0].valid").value("false"))
                .andExpect(jsonPath("$.content[1].valid").value("false"));
    }

    @Test
    public void testCountrySearchForInvalidValidNumber() throws Exception {
        // GIVEN
        // 1 CunstomerInfo instance has a phone number with country code 212
        // Country code 212 corresponds to Morocco
        // WHEN
        // Fetching data with filtering country code 212
        // THEN
        // The right instance is fetched with its correspondent country: Morocco

        CountryInfo countryInfo = countryCodeLookUp.find("212");
        Assert.assertNotNull(countryInfo);
        Assert.assertEquals("Morocco", countryInfo.getName());

        mvc.perform(get("/search/?page=0&size=10&q=212"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("1"))
                .andExpect(jsonPath("$.content[0].valid").value("false"))
                .andExpect(jsonPath("$.content[0].country").value("Morocco"));
    }

    @Test
    public void testCountrySearchForValidNumber() throws Exception {
        // GIVEN
        // 1 CunstomerInfo instance has a phone number with country code 258
        // Country code 258 corresponds to Mozambique
        // WHEN
        // Fetching data with filtering country code 258
        // THEN
        // The right instance is fetched with its correspondent country: Morocco

        CountryInfo countryInfo = countryCodeLookUp.find("258");
        Assert.assertNotNull(countryInfo);
        Assert.assertEquals("Mozambique", countryInfo.getName());

        mvc.perform(get("/search/?page=0&size=10&q=258"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("1"))
                .andExpect(jsonPath("$.content[0].valid").value("true"))
                .andExpect(jsonPath("$.content[0].country").value("Mozambique"));
    }

    @Test
    public void testNonExistingCountrySearch() throws Exception {
        // GIVEN
        // No CunstomerInfo instance has a phone number with country code 333
        // Country code 333 corresponds to Morocco
        // WHEN
        // Fetching data with filtering country code 333
        // THEN
        // An empty result set is returned

        CountryInfo countryInfo = countryCodeLookUp.find("333");
        Assert.assertNull(countryInfo);

        mvc.perform(get("/search/?page=0&size=10&q=333"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void testCountryAndValiditySearchForNonExistingCustomer() throws Exception {
        // GIVEN
        // No CunstomerInfo instance has a phone number with country code 212 and has a valid phone number
        // WHEN
        // Fetching data with filtering country code 212 and validity status true
        // THEN
        // An empty result set is returned

        mvc.perform(get("/search/?page=0&size=10&q=212&valid=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    public void testCountryAndValiditySearchForExistingCustomer() throws Exception {
        // GIVEN
        // 1 CunstomerInfo instance has a phone number with country code 256 and has a valid phone number
        // Country code 256 corresponds to Uganda
        // WHEN
        // Fetching data with filtering country code 256
        // THEN
        // The right instance is fetched with its correspondent country: Morocco

        CountryInfo countryInfo = countryCodeLookUp.find("256");
        Assert.assertNotNull(countryInfo);
        Assert.assertEquals("Uganda", countryInfo.getName());

        mvc.perform(get("/search/?page=0&size=10&q=256&valid=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value("1"))
                .andExpect(jsonPath("$.content[0].valid").value("true"))
                .andExpect(jsonPath("$.content[0].country").value("Uganda"));
    }

    @Test
    public void testFetchAllCounties() throws Exception {
        // GIVEN
        // There exists 5 countries in the existing Country Code LookUp cache
        // WHEN
        // Fetching data for available countries
        // THEN
        // Five countries are returned, matching the lookup cache data

        Assert.assertEquals(5, countryCodeLookUp.getValues().size());

        mvc.perform(get("/countries/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
}
