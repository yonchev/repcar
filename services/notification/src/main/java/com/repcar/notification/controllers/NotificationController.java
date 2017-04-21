/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.controllers;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.List;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.repcar.notification.beans.Notification;
import com.repcar.notification.repositories.NotificationRepository;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 */
@RestController
@Validated
@RequestMapping("/notifications")
@ExposesResourceFor(Notification.class)
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationRepository notificationService;

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<List<Notification>> getNotifications(
            @RequestParam(value = "companyId", required = false) Long companyId,
            @RequestParam(value = "shopId", required = false) Long shopId,
            @NotNull(message = "Please enter eventType!") @RequestParam(value = "eventType",
                    required = true) Notification.Events eventType) {
        List<Notification> notifications = null;
        if (shopId != null && shopId > 0) {
            notifications = notificationService.findByShopIdAndEventType(shopId, eventType);
        } else {
            if (companyId != null && companyId > 0) {
                notifications = notificationService.findByCompanyIdAndEventType(companyId, eventType);
            } else {
                throw new ConstraintViolationException("Please enter shopId or companyId!Not only eventType", null);
            }
        }
        if (notifications.isEmpty()) {
            throw new IllegalArgumentException("Not found " + eventType + " notifications!");
        }
        logger.info("Notifications received {} :", notifications);
        return ResponseEntity.ok(notifications);
    }

    @RequestMapping(method = POST, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<Notification> createNotification(
            @Validated({ Notification.NotificationCreate.class }) @RequestBody Notification notification) {
        notification = notificationService.saveAndFlush(notification);
        logger.info("Created notification {}: ", notification);
        return ResponseEntity
                .created(linkTo(NotificationController.class).slash(notification.getNotificationId()).toUri())
                .body(notification);
    }

    @RequestMapping(method = PUT, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<Notification> updateNotification(
            @Validated({ Notification.NotificationUpdate.class }) @RequestBody Notification notification) {
        if (notificationService.findOne(notification.getNotificationId()) == null) {
            throw new IllegalArgumentException(
                    "Notification is not found for the provided id " + notification.getNotificationId());
        }
        Notification updatedNotification = notificationService.saveAndFlush(notification);
        logger.info("Updated notification {}: ", updatedNotification);
        return ResponseEntity.ok(notification);
    }

    @RequestMapping(value = "/{notificationId}", method = DELETE)
    public ResponseEntity<Void> deleteNotification(@PathVariable("notificationId") Long notificationId) {
        notificationService.delete(notificationId);
        logger.info("Deleted notification with id: {}", notificationId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ IllegalArgumentException.class, EmptyResultDataAccessException.class,
            DataIntegrityViolationException.class, JpaObjectRetrievalFailureException.class })
    public ResponseEntity<ExceptionBody> handleNotFoundExceptions(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(HttpStatus.NOT_FOUND, e), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ UnsatisfiedServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
            MethodArgumentNotValidException.class, MissingServletRequestParameterException.class,
            HttpMessageNotReadableException.class })
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

}
