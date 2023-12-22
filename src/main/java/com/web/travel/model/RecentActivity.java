package com.web.travel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.web.travel.model.enums.ERecentActivityType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecentActivity {
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "blogId")
    private DestinationBlog blog;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tourId")
    private Tour tour;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId")
    private User user;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date time;
    @Enumerated(EnumType.STRING)
    private ERecentActivityType type;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
