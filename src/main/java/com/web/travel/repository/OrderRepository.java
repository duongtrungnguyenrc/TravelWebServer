package com.web.travel.repository;

import com.web.travel.model.Order;
import com.web.travel.model.Tour;
import com.web.travel.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByUserAndTour(User user, Tour tour);
}
