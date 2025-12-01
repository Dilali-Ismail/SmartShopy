package org.usermanagement.smartshopy.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.usermanagement.smartshopy.dto.response.OrderDTO;
import org.usermanagement.smartshopy.entity.Order;
import org.usermanagement.smartshopy.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDTO(Order order);

    OrderDTO.OrderItemDTO orderItemToDTO(OrderItem orderItem);

}
