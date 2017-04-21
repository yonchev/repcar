/*
 * Copyright RepCar AD 2017
 */
package com.repcar.userdata.rest;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.repcar.userdata.assemblers.RecommendationDetailsAssembler;
import com.repcar.userdata.bean.ExceptionBody;
import com.repcar.userdata.bean.RecommendationDetails;
import com.repcar.userdata.repository.RecommendationDetailsRepository;
import com.repcar.userdata.resources.RecommendationDetailsResource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

@RestController
@Validated
@ControllerAdvice
@RequestMapping("/recommendationdetails")
@ExposesResourceFor(RecommendationDetailsResource.class)
public class RecommendationDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationDetailsController.class);

    @Autowired
    private RecommendationDetailsRepository recommendationDetailsRepository;

    @Autowired
    private RecommendationDetailsAssembler recommendationDetailsAssembler;

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE, params = "companyId")
    public ResponseEntity<RecommendationDetailsResource> getDetailsByCompanyId(
            @NotNull(message = "Please provide compnayId") @RequestParam(value = "companyId") Long companyId) {
        logger.info("Get recommendationDetails for companyId: {}", companyId);
        RecommendationDetails details = recommendationDetailsRepository.findByCompanyId(companyId);
        if (details == null) {
            throw new EmptyResultDataAccessException(
                    "No recommendationDetails record found for companyId " + companyId, 1);
        }
        logger.info("Found recommendationDetails for companyId: {} - {}", companyId, details);
        return ResponseEntity.ok(recommendationDetailsAssembler.toResource(details));
    }

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE, params = "applicationName")
    public ResponseEntity<RecommendationDetailsResource> getDetailsByAppName(
            @NotNull(message = "Please provide applicationName") @RequestParam(value = "applicationName") String applicationName) {
        logger.info("Get recommendationDetails for applicationName: {}", applicationName);
        RecommendationDetails details = recommendationDetailsRepository.findByApplicationName(applicationName);
        if (details == null) {
            throw new EmptyResultDataAccessException("No recommendationDetails record found for applicationName "
                    + applicationName, 1);
        }
        logger.info("Found recommendationDetails for applicationName: {} - {}", applicationName, details);
        return ResponseEntity.ok(recommendationDetailsAssembler.toResource(details));
    }

    @RequestMapping(path = "/{recommendationDetailsId:[\\d]+}", method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<RecommendationDetailsResource> getDetailsById(
            @NotNull(message = "Please enter recommendationDetailsId!") @PathVariable("recommendationDetailsId") Long recommendationDetailsId) {
        RecommendationDetails details = recommendationDetailsRepository.findOne(recommendationDetailsId);
        if (details == null) {
            throw new IllegalArgumentException(
                    "No recommendation details were found for provided recommendationDetailsId: "
                            + recommendationDetailsId);
        }
        logger.info("Received recommendation details for id {} : {}.", recommendationDetailsId, details);
        return ResponseEntity.ok(recommendationDetailsAssembler.toResource(details));
    }

    @RequestMapping(method = POST, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<RecommendationDetailsResource> create(
            @Validated({ RecommendationDetails.RecommendationDetailsCreate.class }) @RequestBody RecommendationDetails details) {
        details = recommendationDetailsRepository.saveAndFlush(details);
        RecommendationDetailsResource detailsResource = recommendationDetailsAssembler.toResource(details);
        logger.info("Created recommendationDetails record: {}", detailsResource);
        return ResponseEntity.created(
                linkTo(RecommendationDetailsController.class).slash(details.getRecommendationDetailsId()).toUri())
                .body(detailsResource);
    }

    @RequestMapping(method = PUT, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<RecommendationDetailsResource> update(
            @Validated({ RecommendationDetails.RecommendationDetailsUpdate.class }) @RequestBody RecommendationDetails details) {
        if (recommendationDetailsRepository.findOne(details.getRecommendationDetailsId()) == null) {
            throw new IllegalArgumentException("No recommendationDetails record was found for provided id: "
                    + details.getRecommendationDetailsId());
        }
        details = recommendationDetailsRepository.saveAndFlush(details);
        RecommendationDetailsResource detailsResource = recommendationDetailsAssembler.toResource(details);
        logger.info("Updated recommendationDetails record: {}", detailsResource);
        return ResponseEntity.ok(detailsResource);
    }

    @RequestMapping(path = "/{recommendationDetailsId}", method = DELETE)
    public ResponseEntity<Void> delete(@PathVariable("recommendationDetailsId") Long id) {
        recommendationDetailsRepository.delete(id);
        logger.info("Deleted recommendationDetails record with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionBody> handleExceptions(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage());
        }
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.BAD_REQUEST, strBuilder.toString()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionBody> handleExceptions(DataIntegrityViolationException e) {
        logger.error(e.getMessage() + ". " + e.getMostSpecificCause().getMessage());
        logger.debug(e.getMessage() + ". " + e.getMostSpecificCause().getMessage(), e);
        return new ResponseEntity<ExceptionBody>(
                new ExceptionBody(HttpStatus.BAD_REQUEST, e.getMessage() + ". " + e.getMostSpecificCause().getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ IllegalArgumentException.class, EmptyResultDataAccessException.class,
            JpaObjectRetrievalFailureException.class })
    public ResponseEntity<ExceptionBody> handleNotFoundExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.NOT_FOUND, e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ExceptionBody> handleBadRequestExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.BAD_REQUEST, e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
    public ResponseEntity<ExceptionBody> handleUnsupportedMediaTypeExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage()),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({ HttpServerErrorException.class, InternalError.class })
    public ResponseEntity<ExceptionBody> handleInternalServerExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
