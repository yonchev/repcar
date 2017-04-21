/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.repcar.notification.beans.Notification;

import java.util.List;

/**
 * @author <a href="mailto:imishev@repcarpro.com">Ivan Mishev</a>
 */

@Repository
@Transactional
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByCompanyIdAndEventType(Long companyId, Notification.Events eventType);

    List<Notification> findByShopIdAndEventType(Long shopId, Notification.Events eventType);

}
