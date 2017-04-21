/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.hateoas.MediaTypes.HAL_JSON;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.Link;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import com.repcar.product.Util;
import com.repcar.product.config.UnitTestContext;
import com.repcar.product.assembler.ProductAssembler;
import com.repcar.product.beans.Product;
import com.repcar.product.beans.Shop;
import com.repcar.product.controllers.ProductController;
import com.repcar.product.repositories.ProductRepository;
import com.repcar.product.repositories.ShopRepository;
import com.repcar.product.resources.ProductResource;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { ProductController.class })
@ContextConfiguration(classes = { ProductController.class, ProductAssembler.class, UnitTestContext.class })
public class ProductControllerTest {

    @Resource
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ShopRepository shopRepository;

    @Autowired
    private Product product;

    @Autowired
    private Shop shop;

    @Autowired
    private ProductResource productResource;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @MockBean
    private ProductAssembler productAssembler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String FIELD_SORT_TITLE = "productName,asc";
    private final Long COMPANYID = 1L;
    private final String COMPANYID_STRING = "1";
    private final Long SHOPID = 1L;
    private final String SHOPID_STRING = "1";
    private final int PAGE_NUMBER = 1;
    private final String PAGE_NUMBER_STRING = "1";
    private final int PAGE_SIZE = 10;
    private final String PAGE_SIZE_STRING = "10";

    @Before
    public void setUp() {

        mockMvc = webAppContextSetup(wac).apply(springSecurity(new Filter() {
            @Override
            public void init(FilterConfig arg0) throws ServletException {
            }

            @Override
            public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
                    throws IOException, ServletException {
                // mock OAuth2
                arg2.doFilter(arg0, arg1);
            }

            @Override
            public void destroy() {
            }
        })).build();
    }

    @Test
    public void testGetAllProductsByCompanyId() throws Exception {

        Pageable pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, Direction.ASC, "productName");
        List<Product> productList = new ArrayList<Product>();
        productList.add(product);
        Page<Product> pagedProducts = new PageImpl<Product>(productList, pageRequest, 100);
        given(productRepository.findAllByCompanyCompanyId(COMPANYID, pageRequest)).willReturn(pagedProducts);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("companyId", COMPANYID_STRING);
        mapOfParams.add("page", PAGE_NUMBER_STRING);
        mapOfParams.add("size", PAGE_SIZE_STRING);
        mapOfParams.add("sort", FIELD_SORT_TITLE);
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams).accept(HAL_JSON)
                .contentType(HAL_JSON).content(Util.convertObjectToJsonString(pagedProducts)));

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetAllProductsByShopId() throws Exception {

        Pageable pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, Direction.ASC, "productName");
        List<Product> productList = new ArrayList<Product>();
        productList.add(product);
        Page<Product> pagedProducts = new PageImpl<Product>(productList, pageRequest, 100);
        given(productRepository.findAllByProductShopShopId(SHOPID, pageRequest)).willReturn(pagedProducts);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("shopId", SHOPID_STRING);
        mapOfParams.add("page", PAGE_NUMBER_STRING);
        mapOfParams.add("size", PAGE_SIZE_STRING);
        mapOfParams.add("sort", FIELD_SORT_TITLE);
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams).accept(HAL_JSON)
                .contentType(HAL_JSON).content(Util.convertObjectToJsonString(pagedProducts)));

        result.andExpect(status().isOk());
    }

    @Test
    public void testGetAllProductsBadRequest() throws Exception {
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("shopId", " ");
        mockMvc.perform(get("/products").params(mapOfParams).accept(HAL_JSON).contentType(HAL_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllProductsByUnknownCompanyId() throws Exception {

        Long unknownCompanyId = 2L;
        Pageable pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, Direction.ASC, "productName");

        List<Product> productList = new ArrayList<Product>();
        Page<Product> pagedProducts = new PageImpl<Product>(productList, pageRequest, 100);
        given(productRepository.findAllByCompanyCompanyId(unknownCompanyId, pageRequest)).willReturn(pagedProducts);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("companyId", String.valueOf(unknownCompanyId));
        mapOfParams.add("page", PAGE_NUMBER_STRING);
        mapOfParams.add("size", PAGE_SIZE_STRING);
        mapOfParams.add("sort", FIELD_SORT_TITLE);
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams));

        result.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.products").isEmpty());
    }

    @Test
    public void testGetAllProductsByUnknownShopId() throws Exception {

        Long unknownShopId = 2L;
        Pageable pageRequest = new PageRequest(PAGE_NUMBER, PAGE_SIZE, Direction.ASC, "productName");

        List<Product> productList = new ArrayList<Product>();
        Page<Product> pagedProducts = new PageImpl<Product>(productList, pageRequest, 100);
        given(productRepository.findAllByProductShopShopId(unknownShopId, pageRequest)).willReturn(pagedProducts);

        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("shopId", String.valueOf(unknownShopId));
        mapOfParams.add("page", PAGE_NUMBER_STRING);
        mapOfParams.add("size", PAGE_SIZE_STRING);
        mapOfParams.add("sort", FIELD_SORT_TITLE);
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams));

        result.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._embedded.products").isEmpty());
    }

    @Test
    public void testCreateProduct() throws Exception {
        Long productId = 600L;
        product.setProductId(null);
        List<Long> shopIds = new ArrayList<>();
        shopIds.add(1L);
        List<Shop> shops = new ArrayList<>();
        shops.add(shop);
        given(shopRepository.findAllByShopIdIn(shopIds)).willReturn(shops);
        given(productRepository.saveAndFlush(product)).willReturn(product);
        productResource.add(new Link("http://localhost:9004/products/" + productId));
        given(productAssembler.toResource(product)).willReturn(productResource);
        mockMvc.perform(post("/products").contentType(HAL_JSON).content(Util.convertObjectToJsonString(product)))
                .andExpect(status().isCreated()).andExpect(content().contentTypeCompatibleWith(HAL_JSON))
                .andExpect(jsonPath("$._links.self.href").value("http://localhost:9004/products/" + productId));
    }

    @Test
    public void testCreateProductBadRequest() throws Exception {
        Product wrongProduct = new Product();
        given(productRepository.saveAndFlush(wrongProduct)).willReturn(wrongProduct);
        mockMvc.perform(post("/products").contentType(HAL_JSON).content(Util.convertObjectToJsonString(wrongProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateProductError() throws Exception {
        given(productRepository.saveAndFlush(product)).willThrow(new DataIntegrityViolationException("error"));
        mockMvc.perform(post("/products").contentType(HAL_JSON).content(Util.convertObjectToJsonString(product)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetProduct() throws Exception {
        Long productId = 3L;
        Product product = new Product();
        product.setProductId(productId);
        given(productRepository.findOne(productId)).willReturn(product);
        ProductResource productResource = new ProductResource();
        productResource.setProductId(productId);
        productResource.add(new Link("http://localhost:9004/products/" + productId));
        given(productAssembler.toResource(product)).willReturn(productResource);

        ResultActions result = mockMvc.perform(get("/products/{productId}", productId).accept(HAL_JSON).contentType(HAL_JSON)
                .content(Util.convertObjectToJsonString(productResource)));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetProductNotFound() throws Exception {
        given(productRepository.findOne(COMPANYID)).willThrow(new NullPointerException());
        mockMvc.perform(get("/products/{productId}", 3)).andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductByRfid() throws Exception {
        String rfid = "123456789a";
        String nfc = "12345687";
        Product product = new Product();
        product.setProductRfid(rfid);
        product.setProductNfc(nfc);
        given(productRepository.findByProductRfid(rfid)).willReturn(product);
        ProductResource productResource = new ProductResource();
        productResource.setProductRfid(rfid);
        productResource.setProductNfc(nfc);
        productResource.add(new Link("http://localhost:9004/products?code=" + rfid + "&codeType=rfid"));
        given(productAssembler.toResource(product)).willReturn(productResource);
        productResource = productAssembler.toResource(product);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("code", rfid);
        mapOfParams.add("codeType", "rfid");
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams).accept(HAL_JSON)
                .contentType(HAL_JSON).content(Util.convertObjectToJsonString(productResource)));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetProductByRfidNotFound() throws Exception {
        String rfid = "123456789a";
        given(productRepository.findByProductRfid(rfid)).willReturn(null);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("code", rfid);
        mapOfParams.add("codeType", "rfid");
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductByNfc() throws Exception {
        String nfc = "987654321a";
        String rfid = "123465798";
        Product product = new Product();
        product.setProductRfid(rfid);
        product.setProductNfc(nfc);
        given(productRepository.findByProductNfc(nfc)).willReturn(product);
        ProductResource productResource = new ProductResource();
        productResource.setProductRfid(rfid);
        productResource.setProductNfc(nfc);
        productResource.add(new Link("http://localhost:9004/products?code=" + nfc + "&codeType=nfc"));
        given(productAssembler.toResource(product)).willReturn(productResource);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("code", nfc);
        mapOfParams.add("codeType", "nfc");
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams).accept(HAL_JSON)
                .contentType(HAL_JSON).content(Util.convertObjectToJsonString(productResource)));
        result.andExpect(status().isOk());
    }

    @Test
    public void testGetProductByNfcNotFound() throws Exception {
        String nfc = "987654321a";
        given(productRepository.findByProductNfc(nfc)).willReturn(null);
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("code", nfc);
        mapOfParams.add("codeType", "nfc");
        ResultActions result = mockMvc.perform(get("/products").params(mapOfParams));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductByCodeBadRequest() throws Exception {
        MultiValueMap<String, String> mapOfParams = new LinkedMultiValueMap<>();
        mapOfParams.add("code", "123465469798");
        mockMvc.perform(get("/products").params(mapOfParams)).andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteProduct() throws Exception {
        doNothing().when(productRepository).delete(COMPANYID);
        ResultActions result = mockMvc.perform(delete("/products/{productId}", COMPANYID));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteProductNotInDB() throws Exception {
        doThrow(new EmptyResultDataAccessException(1)).when(productRepository).delete(0L);
        ResultActions result = mockMvc.perform(delete("/products/{productId}", 0L));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateProduct() throws Exception {
        given(productRepository.findOne(product.getProductId())).willReturn(product);
        given(productRepository.saveAndFlush(product)).willReturn(product);
        given(productAssembler.toResource(product)).willReturn(productResource);
        mockMvc.perform(put("/products").contentType(HAL_JSON).content(Util.convertObjectToJsonString(productResource)))
                .andReturn();
    }

    @Test
    public void testUpdateProductBadRequest() throws Exception {
        Product wrongProduct = new Product();
        given(productRepository.save(wrongProduct)).willReturn(null);
        mockMvc.perform(put("/products").contentType(HAL_JSON).content(Util.convertObjectToJsonString(wrongProduct)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateProductError() throws Exception {
        List<Long> shopIds = new ArrayList<>();
        shopIds.add(1L);
        List<Shop> shops = new ArrayList<>();
        shops.add(shop);
        given(shopRepository.findAllByShopIdIn(shopIds)).willReturn(shops);
        given(productRepository.findOne(product.getProductId())).willReturn(null);
        mockMvc.perform(put("/products").contentType(HAL_JSON).content(Util.convertObjectToJsonString(product)))
                .andExpect(status().isNotFound());
    }

}
