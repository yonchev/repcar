/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.controllers;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.google.common.collect.ImmutableList;
import com.repcar.product.assembler.ProductAssembler;
import com.repcar.product.beans.CodeType;
import com.repcar.product.beans.EntityType;
import com.repcar.product.beans.Event;
import com.repcar.product.beans.EventType;
import com.repcar.product.beans.IdMapResource;
import com.repcar.product.beans.Product;
import com.repcar.product.repositories.ProductRepository;
import com.repcar.product.resources.ProductResource;

@RestController
@Validated
@RequestMapping("/products")
@ExposesResourceFor(ProductResource.class)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productService;

    @Autowired
    private ProductAssembler productAssembler;

    @Autowired
    @Qualifier("restTemplate")
    private RestTemplate restTemplate;

    @Autowired
    @Qualifier("clientRestTemplate")
    private OAuth2RestOperations clientRestTemplate;

    /**
     * Create a new user with an auto-generated id and email and name as passed values.
     */
    @RequestMapping(method = GET, produces = HAL_JSON_VALUE, params = { "!productIds" })
    public ResponseEntity<PagedResources<ProductResource>> list(
            @RequestParam(value = "companyId", required = false) Long companyId, @RequestParam(value = "shopId",
                    required = false) Long shopId, Pageable pageable,
            PagedResourcesAssembler<Product> pagedResourcesAssembler) {
        Page<Product> pagedProducts = null;
        if (shopId != null) {
            pagedProducts = productService.findAllByProductShopShopId(shopId, pageable);
        } else if (shopId == null && companyId != null) {
            pagedProducts = productService.findAllByCompanyCompanyId(companyId, pageable);
        } else {
            throw new ConstraintViolationException("Please enter shopId or companyId or productIds", null);
        }
        logger.info("Get products for companyId {} and parameters {}, the products are {}", companyId, pageable,
                pagedProducts);
        Link self = linkTo(methodOn(ProductController.class).list(companyId, shopId, pageable, pagedResourcesAssembler))
                .withSelfRel();
        if (!pagedProducts.hasContent()) {
            return ResponseEntity.ok((PagedResources<ProductResource>) pagedResourcesAssembler.toEmptyResource(
                    pagedProducts, ProductResource.class, self));
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toResource(pagedProducts, productAssembler, self));
    }

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE, params = { "productIds" })
    public ResponseEntity<PagedResources<ProductResource>> listByProductIds(
            @NotEmpty(message = "Please enter productIds") @RequestParam(value = "productIds", required = true) final List<Long> productIds,
            @RequestParam(value = "companyId", required = false) Long companyId, @RequestParam(value = "shopId",
                    required = false) Long shopId, Pageable pageable,
            PagedResourcesAssembler<Product> pagedResourcesAssembler) {
        List<Product> products = null;
        if (shopId != null) {
            products = productService.findAllByProductShopShopIdAndProductIdIn(shopId, productIds);
        } else if (shopId == null && companyId != null) {
            products = productService.findAllByCompanyCompanyIdAndProductIdIn(companyId, productIds);
        } else if (shopId == null && companyId == null) {
            products = productService.findAllByProductIdIn(productIds);
        }
//     /   Collections.sort(products, Comparator.comparing(item -> productIds.indexOf(item.getProductId())));
        Page<Product> page = new PageImpl<Product>(products, pageable, products.size());
        logger.info("Get products for productIds {} , the products are {}", productIds, products);
        Link self = linkTo(
                methodOn(ProductController.class).listByProductIds(productIds, companyId, shopId, pageable,
                        pagedResourcesAssembler)).withSelfRel();
        if (!page.hasContent()) {
            return ResponseEntity.ok((PagedResources<ProductResource>) pagedResourcesAssembler.toEmptyResource(page,
                    ProductResource.class, self));
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toResource(page, productAssembler, self));
    }

    @RequestMapping(value = "/{productId:[\\d]+}", method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<ProductResource> getProductById(@PathVariable("productId") Long productId) {
        Product product = productService.findOne(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product is not found for the provided id " + productId);
        }
        logger.info("Get product for productId {} , the products is {}", productId, product);
        ProductResource resource = productAssembler.toResource(product);
        return ResponseEntity.ok(resource);
    }

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE, params = { "code", "codeType" })
    public ResponseEntity<ProductResource> getProductByRfidOrNfc(
           @NotNull(message = "Please enter code!") @RequestParam(name = "code", required = true) String code,
           @NotNull(message = "Please enter codeType!")  @RequestParam(name = "codeType", required = true) CodeType codeType,
            @RequestParam(name = "weakId", required = false) String weakId) {
        Product product = null;
        if (codeType.equals(CodeType.rfid)) {
            product = productService.findByProductRfid(code);
            if (product == null) {
                throw new IllegalArgumentException("Product is not found for the RFID code " + code);
            }
            if (weakId != null && product.getCompany() != null && product.getCompany().getCompanyId() != null) {
                trackUserAction(weakId, product.getProductId(), product.getCompany().getCompanyId());
            }
            logger.info("Get product for RFID code {} , the products is {}", code, product.getProductId());
        } else if (codeType.equals(CodeType.nfc)) {
            product = productService.findByProductNfc(code);
            if (product == null) {
                throw new IllegalArgumentException("Product is not found for the Nfc code " + code);
            }
            if (weakId != null && product.getCompany() != null && product.getCompany().getCompanyId() != null) {
                trackUserAction(weakId, product.getProductId(), product.getCompany().getCompanyId());
            }
            logger.info("Get product for Nfc code {} , the products is {}", code, product.getProductId());
        } else {
            throw new ConstraintViolationException("Invalid code type: " + codeType, null);
        }
        ProductResource resource = productAssembler.toResource(product);
        return ResponseEntity.ok(resource);
    }

    @RequestMapping(method = POST, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<ProductResource> create(
            @Validated({ Product.ProductCreate.class }) @RequestBody Product product) {
        product = productService.saveAndFlush(product);
        logger.info("Created product {}: ", product);
        ProductResource resource = productAssembler.toResource(product);
        return ResponseEntity.created(linkTo(ProductController.class).slash(product.getProductId()).toUri()).body(
                resource);
    }

    @RequestMapping(method = PUT, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<ProductResource> update(
            @Validated({ Product.ProductUpdate.class }) @RequestBody Product product) {
        if (productService.findOne(product.getProductId()) == null) {
            throw new IllegalArgumentException("Product is not found for the provided id " + product.getProductId());
        }
        Product updatedProduct = productService.saveAndFlush(product);
        ProductResource productResource = productAssembler.toResource(updatedProduct);
        logger.info("Updated product: {}", updatedProduct);
        return ResponseEntity.ok(productResource);
    }

    @RequestMapping(path = "/{productId:[\\d]+}", method = DELETE)
    public ResponseEntity<Void> delete(@PathVariable("productId") Long productId) {
        productService.delete(productId);
        logger.info("Deleted product with id: {}", productId);
        return ResponseEntity.noContent().build();
    }

    private void trackUserAction(String weakId, Long productId, Long companyId) {
        Map<String, Object> params = new HashMap<>();
        params.put("weakId", weakId);
        try {
            IdMapResource idMap = restTemplate.getForObject("http://idmap/idMaps?weakId=" + weakId,
                    IdMapResource.class, params);
            if (idMap != null) {
                sendEventToPredictionIO(weakId, productId);
            }
        } catch (RestClientException ex) {
            if (ex instanceof HttpClientErrorException) {
                logger.error("No IdMap found for weakId {}. No scan event will be sent to Event Server.", weakId);
                logger.debug(ex.getMessage());
            } else {
                logger.error("Error getting IdMap for weakId {}", weakId);
                logger.debug(ex.getMessage());
            }
        }
    }

    private void sendEventToPredictionIO(String weakId, Long productId) {
        Event event = new Event();
        event.setEvent(EventType.scan);
        event.setEntityId(weakId);
        event.setTargetEntityId("i" + productId);
        event.setEntityType(EntityType.user);
        event.setTargetEntityType(EntityType.item);
        Map<String, Object> propMap = new HashMap<String, Object>();
        propMap.put("weakId", ImmutableList.of(weakId));
        event.setProperties(propMap);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaTypes.HAL_JSON));
        headers.setContentType(MediaTypes.HAL_JSON);
        HttpEntity<Event> request = new HttpEntity<Event>(event, headers);

        logger.info("Sending event {} to user-data service.", event.toString());
        try {
            ResponseEntity<String> response = restTemplate.exchange("http://user-data/userdata", HttpMethod.POST,
                    request, String.class);
            logger.info("Sent scan event {} to EventServer.", response.getBody());
        } catch (RestClientException e) {
            logger.error("Cannot send scan event to Event Server: {}" + e.getMessage());
            logger.debug(e.getMessage(), e);
        }
    }

    @ExceptionHandler({ IllegalArgumentException.class, EmptyResultDataAccessException.class,
            DataIntegrityViolationException.class, JpaObjectRetrievalFailureException.class })
    public ResponseEntity<ExceptionBody> handleNotFoundExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.NOT_FOUND, e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ UnsatisfiedServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ExceptionBody> handleBadRequestExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.BAD_REQUEST, e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionBody> handleExceptions(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        if (violations != null) {
            for (ConstraintViolation<?> violation : violations) {
                strBuilder.append(violation.getMessage());
            }
        } else {
            strBuilder.append(e.getMessage());
        }
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.BAD_REQUEST, strBuilder.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HttpMediaTypeNotAcceptableException.class })
    public ResponseEntity<ExceptionBody> handleNotAcceptableExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.NOT_ACCEPTABLE, e),
                HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<ExceptionBody> handleUnsupportedMediaTypeExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({ HttpServerErrorException.class, InternalError.class })
    public ResponseEntity<ExceptionBody> handleInternalServerExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, e),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

}