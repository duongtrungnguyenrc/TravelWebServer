package com.web.travel.core;

import com.web.travel.model.Rate;
import com.web.travel.model.Tour;
import com.web.travel.model.enumeration.ETourType;
import com.web.travel.repository.RateRepository;
import com.web.travel.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Configuration
public class DatabaseConfig {
//    @Autowired
    TourRepository tourRepository;
//    @Autowired
    RateRepository rateRepository;
//    @Bean
//    public CommandLineRunner conf(){
//        return new CommandLineRunner() {
//            @Override
//            public void run(String... args) throws Exception {
//                Tour tour = new Tour("TOUR1",
//                20.5,
//                18,
//                "Xe khach",
//                "HCM",
//                "Da nang",
//                new Date(),
//                new Date(2023 - 1900, 9, 15),
//                5,
//                0,
//                "");
//                Tour tour2 = new Tour("TOUR3",
//                        20.5,
//                        18,
//                        "Xe khach",
//                        "HCM",
//                        "Da nang",
//                        new Date(),
//                        new Date(2023 - 1900, 9, 15),
//                        5,
//                        0,
//                        "");
//                Tour tour3 = new Tour("TOUR2",
//                        20.5,
//                        18,
//                        "Xe khach",
//                        "HCM",
//                        "Da nang",
//                        new Date(),
//                        new Date(2023 - 1900, 9, 15),
//                        5,
//                        0,
//                        "");
//                Tour tour4 = new Tour("TOUR4",
//                        20.5,
//                        18,
//                        "Xe khach",
//                        "HCM",
//                        "Da nang",
//                        new Date(),
//                        new Date(2023 - 1900, 9, 15),
//                        5,
//                        0,
//                        "");
//                Tour tour5 = new Tour("TOUR5",
//                        20.5,
//                        18,
//                        "Xe khach",
//                        "HCM",
//                        "Da nang",
//                        new Date(),
//                        new Date(2023 - 1900, 9, 15),
//                        5,
//                        0,
//                        "");
////
//                tour.setTourType(ETourType.TYPE_POPULAR);
//                tour2.setTourType(ETourType.TYPE_POPULAR);
//                tour3.setTourType(ETourType.TYPE_POPULAR);
//                tourRepository.save(tour);
//                tourRepository.save(tour2);
//                tourRepository.save(tour3);
//                tour4.setTourType(ETourType.TYPE_SAVING);
//                tourRepository.save(tour4);
//                tour5.setTourType(ETourType.TYPE_SPECIAL);
//                tourRepository.save(tour5);
////                Tour tour = tourRepository.findById(1L).orElseGet(Tour::new);
////                Rate rate1 = new Rate(4, "This is too Ok", tour);
////
////                Rate rate2 = new Rate(4, "This is too Ok", tour);
////
////                Rate rate3 = new Rate(4, "This is too Ok", tour);
////
////                Rate rate4 = new Rate(4, "This is too Ok", tour);
////
////                List<Rate> list = new ArrayList<>();
////                list.add(rate1);
////                list.add(rate2);
////                list.add(rate3);
////                list.add(rate4);
////
////                rateRepository.saveAll(list);
//
//            }
//        };
//    }
}
