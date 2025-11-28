package org.usermanagement.smartshopy.service.Order;

import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.usermanagement.smartshopy.dto.request.CreateOrderDTO;
import org.usermanagement.smartshopy.dto.response.OrderDTO;
import org.usermanagement.smartshopy.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(CreateOrderDTO request);
    OrderDTO getOrderById(Long id);
    List<OrderDTO> getAllOrders();
    List<OrderDTO> getOrdersByustomers(Long CustomerId);
    List<OrderDTO> getOrdersByStatus(OrderStatus status);
    OrderDTO confirmOrder(Long id);
    OrderDTO cancelOrder(Long id);
}
