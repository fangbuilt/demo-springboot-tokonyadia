package com.fangbuilt.demo_springboot_tokonyadia.product;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fangbuilt.demo_springboot_tokonyadia.auth.JwtUtil;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductReq;
import com.fangbuilt.demo_springboot_tokonyadia.product.dto.ProductRes;
import com.fangbuilt.demo_springboot_tokonyadia.product.serv.ProductServ;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductCtrl.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductCtrlTest {
  @MockitoBean
  private ProductServ productServ;

  @MockitoBean
  private JwtUtil jwtUtil;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void create_shouldReturnProductRes() throws JacksonException, Exception {
    ProductReq req = new ProductReq("Laptop", 12000000.0, 50);

    ProductRes savedProduct = ProductRes.builder()
        .id(UUID.randomUUID())
        .name("Laptop")
        .cogm(12000000.0)
        .stock(50)
        .build();

    when(productServ.create(Mockito.any())).thenReturn(savedProduct);

    mockMvc.perform(post("/products")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Laptop"));
  }

}
