package com.web.travel.repository;

import com.web.travel.model.Tour;
import com.web.travel.model.enumeration.ETourType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByTourType(ETourType tourType);
}
