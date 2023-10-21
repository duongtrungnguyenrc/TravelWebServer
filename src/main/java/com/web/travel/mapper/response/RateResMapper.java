package com.web.travel.mapper.response;

import com.web.travel.dto.response.RateResDTO;
import com.web.travel.mapper.Mapper;
import com.web.travel.model.Rate;

public class RateResMapper implements Mapper {
    @Override
    public Object mapToDTO(Object obj) {
        obj = (Rate) obj;
        RateResDTO rateResDTO = new RateResDTO();
        rateResDTO.setRatedDate(((Rate) obj).getDateRated());
        rateResDTO.setStar(((Rate) obj).getPoint());
        rateResDTO.setComment(((Rate) obj).getComment());
        rateResDTO.setUsername(((Rate) obj).getUser().getFullName());
        return rateResDTO;
    }

    @Override
    public Object mapToObject(Object obj) {
        return null;
    }
}
