package com.jumia.demo.dao;

import com.jumia.demo.model.CustomerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerInfoRepository extends PagingAndSortingRepository<CustomerInfo, Long>, JpaSpecificationExecutor<CustomerInfo> {
    List<CustomerInfo> findByPhoneStartingWith(String prefix, Sort sort);
}
