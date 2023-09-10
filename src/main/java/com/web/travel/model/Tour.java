package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;
    @NotBlank
    private double adultPrice;
    @NotBlank
    private double childPrice;
    @NotBlank
    private String vehical;
    @NotBlank
    private String depart;
    @NotBlank
    private String destination;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column
    @NotBlank
    private Date departDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    @Column
    @NotBlank
    private Date endDate;
    @NotBlank
    private int maxPeople;
    private int currentPeople;
    @NotBlank
    private String img;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Collection<Schedule> schedules;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private Collection<Order> orders;
    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL)
    private Collection<Rate> rates;
    @ManyToMany
    @JoinTable(
            name = "tour_hotel",
            joinColumns = @JoinColumn(name = "tourId"),
            inverseJoinColumns = @JoinColumn(name = "hotelId")
    )
    private Collection<Hotel> hotels;

    public Tour(String name,
                double adultPrice,
                double childPrice,
                String vehical,
                String depart,
                String destination,
                Date departDate,
                Date endDate,
                int maxPeople,
                int currentPeople,
                String img) {
        this.name = name;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.vehical = vehical;
        this.depart = depart;
        this.destination = destination;
        this.departDate = departDate;
        this.endDate = endDate;
        this.maxPeople = maxPeople;
        this.currentPeople = currentPeople;
        this.img = img;
    }
}
