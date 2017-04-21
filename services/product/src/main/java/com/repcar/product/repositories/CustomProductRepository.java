/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CustomProductRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

}
