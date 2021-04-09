package com.jumia.demo.dao;

import com.jumia.demo.model.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true) // Not explicitly needed but it's good to enforce readOnly restriction
public interface CustomerInfoRepository extends PagingAndSortingRepository<CustomerInfo, Long> {
    List<CustomerInfo> findByPhoneStartingWith(String prefix, Sort sort);
    Page<CustomerInfo> findByPhoneStartingWith(String prefix, Pageable paging);
}
