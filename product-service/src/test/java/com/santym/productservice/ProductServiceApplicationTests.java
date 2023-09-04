package com.santym.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.santym.productservice.dto.ProductRequest;
import com.santym.productservice.model.Product;
import com.santym.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.3");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ProductRepository productRepository;



	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@BeforeEach
	void setup() {
		productRepository.deleteAll();
	}

	@Test
	void shouldCreateProduct() throws Exception {

		ProductRequest productRequest = ProductRequest.builder()
				.name("Product 1")
				.description("Product 1 description")
				.price(BigDecimal.valueOf(100.00))
				.build();

		String productRequestString = objectMapper.writeValueAsString(productRequest);

		mockMvc.perform(MockMvcRequestBuilders
						.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(productRequestString))
				.andExpect(status().isCreated());

		List<Product> productsSaved = productRepository.findAll();

		assertThat(productsSaved.size()).isEqualTo(1);
		assertThat(productsSaved.get(0).getName()).isEqualTo(productRequest.getName());
		assertThat(productsSaved.get(0).getDescription()).isEqualTo(productRequest.getDescription());
		assertThat(productsSaved.get(0).getPrice()).isEqualTo(productRequest.getPrice());
	}

	@Test
	void shouldGetAllProducts() throws Exception {

		List<Product> productsSaved = List.of(
				Product.builder()
					.name("Product 1")
					.description("Product 1 description")
					.price(BigDecimal.valueOf(100.00))
					.build(),

				Product.builder()
					.name("Product 2")
					.description("Product 2 description")
					.price(BigDecimal.valueOf(200.00))
					.build()
		);

		productRepository.saveAll(productsSaved);

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders
						.get("/api/product")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Product> products = objectMapper.readValue(responseContent, new TypeReference<>() {});

		assertThat(products.size()).isEqualTo(productsSaved.size());

		assertThat(products.get(0).getName()).isEqualTo(productsSaved.get(0).getName());
		assertThat(products.get(0).getDescription()).isEqualTo(productsSaved.get(0).getDescription());
		assertThat(products.get(0).getPrice()).isEqualTo(productsSaved.get(0).getPrice());

		assertThat(products.get(1).getName()).isEqualTo(productsSaved.get(1).getName());
		assertThat(products.get(1).getDescription()).isEqualTo(productsSaved.get(1).getDescription());
		assertThat(products.get(1).getPrice()).isEqualTo(productsSaved.get(1).getPrice());
	}


}
