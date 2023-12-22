package com.web.travel.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecentActivityResDTO {
    private TourGeneralResDTO tour;
    private DestinationBlogResDTO blog;
    @JsonFormat(pattern = "HH:mm:ss dd/MM/yyyy")
    private Date activityTime;
}
