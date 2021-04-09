package com.jumia.demo.service;

import com.google.common.collect.Lists;
import com.jumia.demo.dao.CustomerInfoRepository;
import com.jumia.demo.model.CountryInfo;
import com.jumia.demo.model.CustomerInfo;
import com.jumia.demo.utils.CountryCodeLookupCache;
import com.jumia.demo.utils.PhoneNumberVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CustomerInfoService implements ICustomerInfoService {

    @Autowired
    private CustomerInfoRepository repository;

    @Autowired
    private CountryCodeLookupCache cache;

    @Autowired
    private PhoneNumberVisitor visitor;

    @Override
    public Collection<CountryInfo> findCountries() {
        return cache.getValues();
    }

    @Override
    public Page<CustomerInfo> findAll(int page, int size, String sortBy, Boolean desc) {
        Pageable paging = PageRequest.of(page, size, createSortParameter(sortBy, desc));
        Page<CustomerInfo> result = repository.findAll(paging);
        result.stream().forEach(visitor::visit);
        return result;
    }

    @Override
    public Page<CustomerInfo> findByCountryCode(int page, int size, String sortBy, Boolean desc, String q, Boolean valid) {
        List<CustomerInfo> result;
        Sort sort = createSortParameter(sortBy, desc);
        // checking on filtering by country
        if(q == null){
            Iterable<CustomerInfo> iterableResult = repository.findAll(sort);
            result = Lists.newArrayList(iterableResult);
        } else {
            result = repository.findByPhoneStartingWith("(" + q + ")", sort);
        }

        result.stream().forEach(visitor::visit);

        if(valid != null){
//            boolean validStatus = Boolean.valueOf(valid);
            result.removeIf(customerInfo -> Boolean.compare(valid, customerInfo.isValid()) != 0);
        }

        // Preparing Paged result
        Pageable paging = PageRequest.of(page, size);
        PagedListHolder pagedListHolder = new PagedListHolder(result);
        pagedListHolder.setPageSize(paging.getPageSize()); // number of items per page
        pagedListHolder.setPage(paging.getPageNumber());
        Page<CustomerInfo> customerInfoPage = new PageImpl<CustomerInfo>(pagedListHolder.getPageList(), paging, result.size());

        return customerInfoPage;
    }


    private Sort createSortParameter(String sortBy, Boolean desc){
        if(sortBy == null || desc == null){
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}
