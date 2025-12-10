package org.usermanagement.smartshopy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.usermanagement.smartshopy.dto.response.PromoCodeDTO;
import org.usermanagement.smartshopy.entity.PromoCode;

@Mapper(componentModel = "spring")
public interface PromoCodeMapper {

    @Mapping(target = "remainingUsages", expression = "java(promoCode.getMaxUsages() - promoCode.getCurrentUsages())")
    @Mapping(target = "isValid", expression = "java(promoCode.isValid())")
    PromoCodeDTO toDTO(PromoCode promoCode);

}
