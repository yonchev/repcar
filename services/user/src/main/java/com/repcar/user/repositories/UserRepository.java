/*
 * Copyright RepCar AD 2017
 */
package com.repcar.user.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.user.beans.User;

/**
 * Home object for domain model class User.
 * 
 */
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUserName(String userName);

    Page<User> findByCompanyId(Long companyId, Pageable pageable);
}
