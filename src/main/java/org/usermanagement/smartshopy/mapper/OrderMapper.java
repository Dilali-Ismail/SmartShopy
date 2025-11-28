package org.usermanagement.smartshopy.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.usermanagement.smartshopy.dto.response.OrderDTO;
import org.usermanagement.smartshopy.entity.Order;
import org.usermanagement.smartshopy.entity.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "items", source = "items")
    OrderDTO toDTO(Order order);

    @Mapping(target = "product",source = "product")
    OrderDTO.OrderItemDTO orderItemToDTO(OrderItem orderItem);

}
