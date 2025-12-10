package org.usermanagement.smartshopy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.usermanagement.smartshopy.dto.response.PaymentDTO;
import org.usermanagement.smartshopy.entity.Payment;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "orderId", source = "order.id")
    PaymentDTO toDTO(Payment payment);
}
