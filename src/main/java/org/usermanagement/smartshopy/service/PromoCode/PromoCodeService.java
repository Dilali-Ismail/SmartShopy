package org.usermanagement.smartshopy.service.PromoCode;

import org.usermanagement.smartshopy.dto.request.CreatePromoCodeDTO;
import org.usermanagement.smartshopy.dto.response.PromoCodeDTO;
import org.usermanagement.smartshopy.entity.PromoCode;

import java.util.List;

public interface PromoCodeService {

    PromoCodeDTO createPromocode(CreatePromoCodeDTO dto);
    List<PromoCodeDTO> getAllPromocodes();

}
