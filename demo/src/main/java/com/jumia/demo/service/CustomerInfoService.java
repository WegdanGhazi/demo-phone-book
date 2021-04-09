package com.jumia.demo.service;

import com.google.common.collect.Lists;
import com.jumia.demo.dao.CustomerInfoRepository;
import com.jumia.demo.model.CustomerInfo;
import com.jumia.demo.utils.PhoneNumberVisitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.List;

@Service
@Transactional(readOnly = true) // Not explicitly needed but it's good to enforce readOnly restriction
public class CustomerInfoService implements ICustomerInfoService {

    @Autowired
    private CustomerInfoRepository repository;

    @Autowired
    private PhoneNumberVisitor visitor;

    /**
     * A method to retried a Page of customer info with no filtering
     * @param page page index for the retrieved page
     * @param size page size for the retrieved page
     * @param sortBy field to be sorted by | @Nullable - in case of unsorted table
     * @param desc sort direction | @Nullable - in case of unsorted table
     *                                          (especially added for front end sorting direction cycle)
     * @return A Page of customer info
     */
    @Override
    public Page<CustomerInfo> findAll(int page, int size, @Nullable String sortBy, @Nullable Boolean desc) {
        Pageable paging = PageRequest.of(page, size, createSortParameter(sortBy, desc));
        Page<CustomerInfo> result = repository.findAll(paging);
        // Set Entity transient fields
        result.stream().forEach(visitor::visit);
        return result;
    }

    /**
     * A method to retried a Page of customer info with filtering.
     * Filtering can be done by either phone number validity (transient) or country code.
     * When filtering is done by country code alone, a direct database call can be sufficient.
     * When filtering involves validity (a transient field) as well, the data is filtered in server logic,
     * then the paging is implemented server side.
     * Sorting is done database-side on non-transient fields in all cases since it is not affected by paging.
     * @param page page index for the retrieved page
     * @param size page size for the retrieved page
     * @param sortBy field to be sorted by | @Nullable - in case of unsorted table
     * @param desc sort direction | @Nullable - in case of unsorted table
     *      *                                   (especially added for front end sorting direction cycle)
     * @param q the country code that the user wished to filter by | @Nullable
     * @param valid the phone number validity status that the user wished to filter by | @Nullable
     * @return A Page of customer info
     */
    @Override
    public Page<CustomerInfo> findByCountryCodeOrValidity(int page, int size, String sortBy, Boolean desc, String q, Boolean valid) {
        // Preparing paging info
        Pageable paging = PageRequest.of(page, size, createSortParameter(sortBy, desc));

        if(valid == null && q == null) {
            throw new IllegalArgumentException("Can't search with null parameters");
        } else if (valid == null){
            // By performing this check we avoid custom paging in case the search query depends on the country code alone
            return  findByCountryCode(q, paging);
        }

        // checking on filtering by country
        List<CustomerInfo> result;
        if(q == null) { // filter by validity only
            result = Lists.newArrayList(repository.findAll(paging.getSort()));
        } else { // filter by Country Code & validity
            result = repository.findByPhoneStartingWith("(" + q + ")", paging.getSort());
        }

        // Set Entity transient fields
        result.stream().forEach(visitor::visit);
        // Filter according to validity
        result.removeIf(customerInfo -> Boolean.compare(valid, customerInfo.isValid()) != 0);

        // This block creates a Paged result to return to the FE, by mimicking the paging logic
        PagedListHolder pagedListHolder = new PagedListHolder(result);
        pagedListHolder.setPageSize(paging.getPageSize()); // number of items per page
        pagedListHolder.setPage(paging.getPageNumber()); // Page index
        Page<CustomerInfo> customerInfoPage = new PageImpl<CustomerInfo>(pagedListHolder.getPageList(), paging, result.size());
        return customerInfoPage;
    }

    // Encapsulating findByCountryCode logic to use the generalized Repository method
    // This might seem redundant but it does save unnecessary server-side logic being executed
    public Page<CustomerInfo> findByCountryCode(String q, Pageable paging ){
        Page<CustomerInfo> result = repository.findByPhoneStartingWith("(" + q + ")", paging);
        // Set Entity transient fields
        result.stream().forEach(visitor::visit);
        return result;
    }

    private Sort createSortParameter(String sortBy, Boolean desc){
        if(sortBy == null || desc == null){
            return Sort.unsorted();
        }
        Sort.Direction sortDirection = desc ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(sortDirection, sortBy);
    }
}
