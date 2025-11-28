package org.usermanagement.smartshopy.service.Order;

import ch.qos.logback.core.net.server.Client;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.CreateOrderDTO;
import org.usermanagement.smartshopy.dto.response.OrderDTO;
import org.usermanagement.smartshopy.entity.Customer;
import org.usermanagement.smartshopy.entity.Order;
import org.usermanagement.smartshopy.entity.OrderItem;
import org.usermanagement.smartshopy.entity.Product;
import org.usermanagement.smartshopy.enums.OrderStatus;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement.smartshopy.mapper.OrderMapper;
import org.usermanagement.smartshopy.repository.CustomerRepository;
import org.usermanagement.smartshopy.repository.OrderRepository;
import org.usermanagement.smartshopy.repository.ProductRepository;
import org.usermanagement.smartshopy.repository.UserRepository;
import org.usermanagement.smartshopy.service.Product.ProductService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderDTO createOrder(CreateOrderDTO request){
        Customer customer = customerRepository.findById(request.getCustomerId()).orElseThrow(()-> new NotFoundException("Client Introvable"));

        Order order = Order.builder()
                .customer(customer)
                .items(new ArrayList<>())
                .status(OrderStatus.PENDING)
                .amountPaid(BigDecimal.ZERO)
                .build();

        for(CreateOrderDTO.OrderItemDTO itemDTO: request.getItems()){
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(()-> new NotFoundException("Produit introvable"));

            if(!product.isAvailable(itemDTO.getQuantity())){
                throw new BadRequestException("Quantity indisponible");
            }

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(itemDTO.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();

            orderItem.calculateSubtotal();

            order.addItem(orderItem);
        }
        calculateOrderTotals(order);
        orderRepository.save(order);

        return orderMapper.toDTO(order);
}
    @Override
    @Transactional
   public OrderDTO getOrderById(Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new NotFoundException("Commande non trouvée"));

        return orderMapper.toDTO(order);
   }
   public  List<OrderDTO> getAllOrders(){
       return  orderRepository.findAll().stream().map(orderMapper::toDTO).collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByustomers(Long CustomerId){

        if (!customerRepository.existsById(CustomerId)) {
            throw new NotFoundException("Client introuvable avec l'ID : " + CustomerId);
        }
        return orderRepository.findByCustomerId(CustomerId)
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOrdersByStatus(OrderStatus status){
        return orderRepository.findByStatus(status)
                .stream()
                .map(orderMapper::toDTO)
                .collect(Collectors.toList());
    }
    public OrderDTO confirmOrder(Long id){

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Commande introuvable avec l'ID : " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Seules les commandes PENDING peuvent être confirmées");
        }

        if (! order.isFullyPaid()) {
            throw new BadRequestException(
                    String.format("Paiement incomplet.  Reste à payer : %. 2f DH", order.getRemainingAmount())
            );
        }

        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            try {
                product.decrementStock(item.getQuantity());
                productRepository.save(product);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        //mettre a jour les statistique de customer
        Customer customer = order.getCustomer();
        customer.UpdateState(order.getTotalTTC());
        customerRepository.save(customer);

        //change the statut of order
        order.setStatus(OrderStatus.CONFIRMED);
        order.setConfirmedAt(LocalDateTime.now());
        orderRepository.save(order);

        return orderMapper.toDTO(order);

    }
    public OrderDTO cancelOrder(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(" Commande non trouvée" ));


        if(!order.canBeCancelled()){
             throw new BadRequestException("Cette commande ne peut pas etre annulé");
        }

        if(order.getStatus().equals(OrderStatus.CONFIRMED)){

            for(OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                product.incrementStock(item.getQuantity());
                productRepository.save(product);
            }

            Customer customer = order.getCustomer();

            customer.setTotalOrders(customer.getTotalOrders() - 1);
            customer.setTotalSpent(customer.getTotalSpent().subtract(order.getTotalTTC()));
            customer.updateTier();
            customerRepository.save(customer);
        }
        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return orderMapper.toDTO(order);
    }
    private void calculateOrderTotals(Order order){
        //subtotal
        BigDecimal subtotal = order.getItems().stream().map(OrderItem::getSubtotal).reduce(BigDecimal.ZERO,BigDecimal::add);
        order.setSubtotalHT(subtotal);
        //discount loyalty
        BigDecimal loyaltyDiscount = order.getCustomer().Calculediscount(subtotal);
        order.setLoyaltyDiscount(loyaltyDiscount);
        //resultat de subsract de loyalty from subtotal
       BigDecimal totalAfterDisocunt = subtotal.subtract(loyaltyDiscount);
       order.setTotalAfterDiscount(totalAfterDisocunt);

       //Tva
        BigDecimal tva = totalAfterDisocunt.multiply(new BigDecimal("0.20"))
                .setScale(2, RoundingMode.HALF_UP);
        order.setTva(tva);

        //total ttc

        BigDecimal TTC = totalAfterDisocunt.add(tva);
        order.setTotalTTC(TTC);

        //rest a payer

        order.setRemainingAmount(TTC.subtract(order.getAmountPaid()));
    }
}
