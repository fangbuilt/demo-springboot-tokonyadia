package com.fangbuilt.demo_springboot_tokonyadia.customer.serv;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fangbuilt.demo_springboot_tokonyadia.common.util.Gender;
import com.fangbuilt.demo_springboot_tokonyadia.customer.dto.CustomerReq;
import com.fangbuilt.demo_springboot_tokonyadia.customer.dto.CustomerRes;

public interface CustomerServ {
  CustomerRes create(CustomerReq payload);

  CustomerRes read(UUID id);

  Page<CustomerRes> read(String fullname, String email, String address, Gender gender, Boolean hasMember,
      Pageable pageable);

  CustomerRes update(UUID id, CustomerReq payload);

  void delete(UUID id); // Soft delete
}
