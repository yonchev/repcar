/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javassist.NotFoundException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.repcar.userdata.bean.Event;
import com.repcar.userdata.bean.EventResponse;
import com.repcar.userdata.bean.ExceptionBody;
import com.repcar.userdata.bean.RecommendedContent;
import com.repcar.userdata.domain.Category;
import com.repcar.userdata.domain.IdMap;
import com.repcar.userdata.domain.Product;
import com.repcar.userdata.repository.CategoryRepository;
import com.repcar.userdata.repository.IdMapRepository;
import com.repcar.userdata.repository.ProductRepository;
import com.repcar.userdata.services.EventServerConnector;

import feign.FeignException;

@RestController
@RequestMapping("/userdata")
public class UserDataController {

    private static final Logger logger = LoggerFactory.getLogger(UserDataController.class);

    private static final String PRODUCT_PREFIX = "i";
    private static final String CATEGORY_PREFIX = "c";

    @Autowired
    private EventServerConnector esRepository;

    @Autowired
    private IdMapRepository idMapRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @RequestMapping(method = POST, produces = HAL_JSON_VALUE, consumes = HAL_JSON_VALUE)
    public ResponseEntity<EventResponse> setEvent(@Valid @RequestBody Event event) throws ExecutionException,
            InterruptedException, IOException, IllegalArgumentException {

        IdMap idmap = idMapRepository.findByWeakId(event.getEntityId());

        if (idmap != null && idmap.isAccepted()) {
            if (event.getTargetEntityId().startsWith(CATEGORY_PREFIX)) {
                List<Long> categoryIds = new ArrayList<Long>();
                categoryIds.add(Long.valueOf(event.getTargetEntityId().substring(1)));
                List<Category> categories = categoryRepository.findAllByCompanyIdAndCategoryIdIn(idmap.getCompanyId(),
                        categoryIds);
                if (!categories.isEmpty()) {
                    String eventId = esRepository.sendEvent(idmap.getCompanyId(), event);
                    return ResponseEntity.created(linkTo(UserDataController.class).toUri())
                            .body(new EventResponse(eventId));
                }
            } else if (event.getTargetEntityId().startsWith(PRODUCT_PREFIX)) {
                Product product = productRepository.findOne(Long.valueOf(event.getTargetEntityId().substring(1)));
                if (product != null) {
                    if (!product.getCompanyId().equals(idmap.getCompanyId())) {
                        throw new InternalError(
                                "CompanyId does dot match for product " + product.getProductId() + " and weakId "
                                        + idmap.getWeakId() + ". No events will be sent.");
                    }
                    String eventId = esRepository.sendEvent(idmap.getCompanyId(), event);
                    return ResponseEntity.created(linkTo(UserDataController.class).toUri())
                            .body(new EventResponse(eventId));
                }
            }
        } else {
            // If request body is correct, but user has not accepted terms and conditions, then do noting
            return ResponseEntity.ok().body(new EventResponse(""));
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/{oldWeakId}/{newWeakId}", method = POST)
    public ResponseEntity<EventResponse> updateEvent(@PathVariable("oldWeakId") String oldWeakId,
            @PathVariable("newWeakId") String newWeakId) throws IOException, NotFoundException {
        if (!oldWeakId.equals(newWeakId)) {
            if (!getCompanyIdByWeakId(oldWeakId).equals(getCompanyIdByWeakId(newWeakId))) {
                throw new InternalError("CompanyId does dot match for oldWeakId " + oldWeakId + " and newWeakId {}"
                        + newWeakId + ". No events will be updated.");
            } else {
                Long companyId = getCompanyIdByWeakId(oldWeakId);
                esRepository.replace(companyId, oldWeakId, newWeakId);
                logger.debug("Replaced events for weakId {} with weakId {}.", oldWeakId, newWeakId);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/{weakId}", method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<Resource<Product>> getEvent(@PathVariable("weakId") String weakId) throws RuntimeException {
        com.repcar.userdata.bean.Event event = esRepository.getEvent(getCompanyIdByWeakId(weakId), weakId);
        Product product = productRepository.findOne(Long.valueOf(event.getTargetEntityId().substring(1)));
        if (product != null) {
            logger.debug("Get product {} for weakId {}.", product, weakId);
            Resource<Product> productResource = new Resource<Product>(product);
            return new ResponseEntity<>(productResource, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/recommendation/product", method = GET, produces = HAL_JSON_VALUE)
    public HttpEntity<PagedResources<Resource<Product>>> getProductRecommendation(
            @RequestParam(name = "weakId") String weakId, @RequestParam(name = "count") int count,
            @RequestParam(name = "shopId", required = false) Long shopId, Pageable pageable,
            PagedResourcesAssembler<Product> pagedResourcesAssembler)
            throws ExecutionException, InterruptedException, IOException {
        JsonObject jsonObject = null;
        Long companyId = getCompanyIdByWeakId(weakId);
        jsonObject = esRepository.getRecommendation(companyId, weakId, RecommendedContent.product);
        final List<Long> productIds = getListOfItemIds(jsonObject, RecommendedContent.product);
        List<Product> products = null;
        if (shopId != null) {
            products = productRepository.findAllByProductShopShopIdAndProductIdIn(shopId, productIds);
        } else {
            products = productRepository.findAllByCompanyIdAndProductIdIn(companyId, productIds);
        }
        // sort product according to the PIO score
    //    Collections.sort(products, Comparator.comparing(item -> productIds.indexOf(item.getProductId())));
        List<Product> prList = new ArrayList<>();
        Iterator<Product> itr = products.iterator();
        int i = 0;
        while (itr.hasNext() && i < count) {
            prList.add(itr.next());
            i++;
        }
        logger.debug("Recommended products for {} are {}.", weakId, prList);
        return new ResponseEntity<PagedResources<Resource<Product>>>(pagedResourcesAssembler.toResource(new PageImpl(
                prList)), OK);
    }

    @RequestMapping(value = "/recommendation/category", method = GET, produces = HAL_JSON_VALUE)
    public HttpEntity<PagedResources<Resource<Category>>> getCategoryRecommendation(
            @RequestParam(name = "weakId") String weakId, @RequestParam(name = "count") int count,
            @RequestParam(name = "shopId", required = false) Long shopId,
            PagedResourcesAssembler<Category> pagedResourcesAssembler)
            throws ExecutionException, InterruptedException, IOException {
        Long companyId = getCompanyIdByWeakId(weakId);
        JsonObject jsonObject = null;
        jsonObject = esRepository.getRecommendation(companyId, weakId, RecommendedContent.category);
        List<Long> categoryIds = getListOfItemIds(jsonObject, RecommendedContent.category);
        List<Category> categories = null;
        if (shopId != null) {
            categories = categoryRepository.findAllByCategoryShopShopIdAndCategoryIdIn(shopId, categoryIds);
        } else {
            categories = categoryRepository.findAllByCompanyIdAndCategoryIdIn(companyId, categoryIds);
        }
        int i = 0;
        Iterator<Category> itr = categories.iterator();
        List<Category> catList = new ArrayList<>();
        while (itr.hasNext() && i < count) {
            catList.add(itr.next());
            i++;
        }
        logger.debug("Recommended categories for {} are {}.", weakId, catList);
        return ResponseEntity.ok(pagedResourcesAssembler.toResource(new PageImpl(catList)));
    }

    private List<Long> getListOfItemIds(JsonObject jsonObject, RecommendedContent contentType) {
        String prefix = contentType.equals(RecommendedContent.product) ? PRODUCT_PREFIX : CATEGORY_PREFIX;
        ArrayList<Long> itemIds = new ArrayList<Long>();
        for (JsonElement json : jsonObject.getAsJsonArray("itemScores")) {
            if (json.getAsJsonObject().get("item").getAsString().startsWith(prefix)) {
                itemIds.add(Long.parseLong(json.getAsJsonObject().get("item").getAsString().substring(1)));
            }
        }
        return itemIds;
    }

    private Long getCompanyIdByWeakId(String weakId) {
        logger.info("Getting idMap for weakId: {}.", weakId);
        try {
            IdMap idmap = idMapRepository.findByWeakId(weakId);
            logger.info("Found idMap {} for weakId {}.", idmap, weakId);
            return idmap.getCompanyId();
        } catch (RuntimeException e) {
            logger.error("Error getting idMap for weakId: {}, {}", weakId, e.getMessage());
            throw e;
        }
    }

    @ExceptionHandler({ MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class, IOException.class })
    public ResponseEntity<ExceptionBody> handleMethodArgumentNotValidException(Exception e) {
        logger.error("BadRequestException while executing PIO request: {}", e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.BAD_REQUEST, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HttpServerErrorException.class, HttpClientErrorException.class })
    public ResponseEntity<ExceptionBody> handleRestClientException(HttpStatusCodeException e) {
        logger.error("Error executing PIO request: {}", e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(e.getStatusCode(), e.getResponseBodyAsString()),
                e.getStatusCode());
    }

    @ExceptionHandler({ ExecutionException.class, InterruptedException.class, Exception.class })
    public ResponseEntity<ExceptionBody> handleGeneralException(Exception e) {
        logger.error("Error executing PIO request: {}", e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionBody> handleFeignException(FeignException e) {
        logger.error("Feign exception: {}", e.getMessage());
        logger.debug(e.getMessage(), e);
        switch (HttpStatus.valueOf(e.status())) {
        case NOT_FOUND:
            return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.NOT_FOUND, e.getMessage()),
                    HttpStatus.NOT_FOUND);
        default:
            break;
        }
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ExceptionBody> handleNumberFormatException(NumberFormatException e) {
        logger.error("NumberFormat exception: {}", e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(
                new ExceptionBody(HttpStatus.BAD_REQUEST, "Wrong parameter provided."), HttpStatus.BAD_REQUEST);
    }

}
