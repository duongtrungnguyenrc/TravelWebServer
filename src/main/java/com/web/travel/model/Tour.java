package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.travel.model.enumeration.ETourType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "tour")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String vehicle;
    @Enumerated(EnumType.STRING)
    private ETourType tourType;
    private String depart;
    private String destination;
    private Boolean isRemoved;
    @OneToMany(mappedBy = "tour",
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL
    )
    @JsonIgnore
    private Collection<TourDate> tourDate;
    private int maxPeople;
    private int currentPeople;
    private String img;
    @OneToMany(mappedBy = "tour",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Collection<Schedule> schedules;
    @OneToMany(mappedBy = "tour", fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<Order> orders;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private Collection<Rate> rates;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "tour_hotel",
            joinColumns = @JoinColumn(name = "tourId"),
            inverseJoinColumns = @JoinColumn(name = "hotelId")
    )
    @JsonIgnore
    private Collection<Hotel> hotels;
    public Tour(String name,
                String vehicle,
                String depart,
                String destination,
                Collection<TourDate> tourDate,
                int maxPeople,
                int currentPeople,
                String img) {
        this.name = name;
        this.vehicle = vehicle;
        this.depart = depart;
        this.destination = destination;
        this.tourDate = tourDate;
        this.maxPeople = maxPeople;
        this.currentPeople = currentPeople;
        this.img = img;
    }
}
