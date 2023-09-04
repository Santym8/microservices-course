package com.santym.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.santym.orderservice.dto.OrderLineItemsDto;
import com.santym.orderservice.dto.OrderRequest;
import com.santym.orderservice.model.OrderLineItems;
import com.santym.orderservice.repository.OrderLineItemsRepository;
import com.santym.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
class OrderControllerIntegrationTest {
    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.0.26");

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineItemsRepository orderLineItemsRepository;

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", mySQLContainer::getDriverClassName);
    }

    @BeforeEach
    void setup() {
        orderLineItemsRepository.deleteAll();
        orderRepository.deleteAll();
    }

    @Test
    void shouldCreateOrder() throws Exception {
        OrderRequest orderRequest = OrderRequest.builder()
                .orderLineItemsList(
                        List.of(
                                OrderLineItemsDto.builder()
                                        .skuCode("ABC")
                                        .quantity(2)
                                        .price(BigDecimal.valueOf(100.00))
                                        .build(),
                                OrderLineItemsDto.builder()
                                        .skuCode("DEF")
                                        .quantity(2)
                                        .price(BigDecimal.valueOf(30.50))
                                        .build()))
                .build();

        String orderRequestString = objectMapper.writeValueAsString(orderRequest);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/order")
                .content(orderRequestString)
                .contentType("application/json"))
                .andExpect(status().isCreated());

        List<OrderLineItems> orderLineItemsSaved = orderLineItemsRepository.findAll();

        assertThat(orderLineItemsSaved.size()).isEqualTo(orderRequest.getOrderLineItemsList().size());

        assertThat(orderLineItemsSaved.get(0).getSkuCode())
                .isEqualTo(orderRequest.getOrderLineItemsList().get(0).getSkuCode());
        assertThat(orderLineItemsSaved.get(0).getQuantity())
                .isEqualTo(orderRequest.getOrderLineItemsList().get(0).getQuantity());
        assertThat(orderLineItemsSaved.get(0).getPrice()
                .compareTo(orderRequest.getOrderLineItemsList().get(0).getPrice()))
                .isZero();


        assertThat(orderLineItemsSaved.get(1).getSkuCode())
                .isEqualTo(orderRequest.getOrderLineItemsList().get(1).getSkuCode());
        assertThat(orderLineItemsSaved.get(1).getQuantity())
                .isEqualTo(orderRequest.getOrderLineItemsList().get(1).getQuantity());
        assertThat(orderLineItemsSaved.get(1).getPrice()
                .compareTo(orderRequest.getOrderLineItemsList().get(1).getPrice()))
                .isZero();


    }
}