/*
 * Copyright RepCar AD 2017
 */
package com.repcar.product.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.repcar.product.beans.Shop;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Shop findByShopId(Long shopId);

    List<Shop> findAllByShopIdIn(List<Long> shops);
}
