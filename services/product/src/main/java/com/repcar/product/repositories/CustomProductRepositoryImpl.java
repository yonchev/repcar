/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.repositories;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

public class CustomProductRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements
        CustomProductRepository<T, ID> {

    static Logger logger = LoggerFactory.getLogger(CustomProductRepositoryImpl.class);

    private final EntityManager em;

    private final JpaEntityInformation<T, ?> entityInformation;

    /**
     * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
     *
     * @param entityInformation
     * @param entityManager
     */
    public CustomProductRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.em = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    @Transactional
    public <S extends T> S saveAndFlush(S entity) {
        if (entityInformation.isNew(entity)) {
            S result = save(entity);
            flush();
            em.refresh(result);
            return result;
        } else {
            S result = save(entity);
            flush();
            return result;
        }
    }

}
