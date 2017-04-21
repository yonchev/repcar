/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.controllers;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.repcar.user.assembler.UserAssembler;
import com.repcar.user.beans.User;
import com.repcar.user.beans.User.Create;
import com.repcar.user.beans.User.Update;
import com.repcar.user.encryption.EncryptDecryptService;
import com.repcar.user.exception.ExceptionBody;
import com.repcar.user.repositories.UserRepository;
import com.repcar.user.resources.UserResource;

@RestController
@RequestMapping("/users")
@ExposesResourceFor(UserResource.class)
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userService;

    @Autowired
    private UserAssembler userAssembler;

    @Autowired
    private EncryptDecryptService encryptDecryptService;

    @RequestMapping(method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<PagedResources<UserResource>> getUsers(
            @RequestParam(value = "companyId", required = true) Long companyId, Pageable pageable,
            PagedResourcesAssembler<User> pagedResourcesAssembler) {
        Page<User> users = userService.findByCompanyId(companyId, pageable);
        Link self = linkTo(methodOn(UserController.class).getUsers(companyId, pageable, pagedResourcesAssembler))
                .withSelfRel();
        logger.debug("Found users for companyId: {} - {}", companyId, users);
        if (!users.hasContent()) {
            return ResponseEntity.ok((PagedResources<UserResource>) pagedResourcesAssembler.toEmptyResource(users,
                    UserResource.class, self));
        }
        return ResponseEntity.ok(pagedResourcesAssembler.toResource(users, userAssembler, self));
    }

    @RequestMapping(value = "/logged", method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<UserResource> getLoggedUser(OAuth2Authentication principal) {
        Map<String, ?> userInfo = (Map<String, ?>) principal.getUserAuthentication().getDetails();
        User user = userService.findByUserName((String) userInfo.get("name"));
        if (user == null) {
            logger.info("User {} not found.", userInfo.get("name"));
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ResponseEntity.ok(userAssembler.toResource(user));
    }

    @RequestMapping(value = "/{userId:[\\d]+}", method = GET, produces = HAL_JSON_VALUE)
    public ResponseEntity<UserResource> getUser(@PathVariable("userId") Long userId) {
        User user = userService.findOne(userId);
        logger.info("For id: {} whe user is {}.", userId, user);
        if (user == null) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return ResponseEntity.ok(userAssembler.toResource(user));
    }

    @RequestMapping(method = POST, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<UserResource> create(@Validated({ Create.class }) @RequestBody User user) {
        user.setUserPassword(encryptDecryptService.encrypt(user.getUserPassword()));
        user = userService.save(user);
        UserResource resource = userAssembler.toResource(user);
        logger.info("Created user: {}", resource);
        return ResponseEntity
                .created(ControllerLinkBuilder.linkTo(UserController.class).slash(user.getUserId()).toUri())
                .body(resource);
    }

    @RequestMapping(method = PUT, consumes = HAL_JSON_VALUE, produces = HAL_JSON_VALUE)
    public ResponseEntity<UserResource> update(@Validated({ Update.class }) @RequestBody User user) {
        if (userService.findOne(user.getUserId()) == null) {
            throw new IllegalArgumentException("User is not found for the provided id " + user.getUserId());
        }
        user.setUserPassword(encryptDecryptService.encrypt(user.getUserPassword()));
        user = userService.saveAndFlush(user);
        UserResource userResource = userAssembler.toResource(user);
        logger.info("Updated user: {}", userResource);
        return ResponseEntity.ok(userResource);
    }

    @RequestMapping(path = "/{userId:[\\d]+}", method = DELETE)
    public ResponseEntity<Void> delete(@PathVariable("userId") Long userId) {
        userService.delete(userId);
        logger.info("Deleted user with id: {}", userId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, ConstraintViolationException.class })
    public ResponseEntity<ExceptionBody> handleBadRequestExcepion(Exception e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(BAD_REQUEST, e.getMessage()), BAD_REQUEST);
    }

    @ExceptionHandler({ IllegalArgumentException.class, EmptyResultDataAccessException.class })
    public ResponseEntity<ExceptionBody> handleINotFoundException(RuntimeException e) {
        logger.error(e.getMessage());
        logger.debug(e.getMessage(), e);
        return new ResponseEntity<ExceptionBody>(new ExceptionBody(NOT_FOUND, e.getMessage()), NOT_FOUND);
    }

}