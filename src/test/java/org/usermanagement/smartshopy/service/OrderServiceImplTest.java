package org.usermanagement.smartshopy.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.usermanagement.smartshopy.dto.request.CreateOrderDTO;
import org.usermanagement.smartshopy.dto.response.OrderDTO;
import org.usermanagement.smartshopy.entity.*;
import org.usermanagement.smartshopy.enums.CustomerTier;
import org.usermanagement.smartshopy.enums.OrderStatus;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement.smartshopy.mapper.OrderMapper;
import org.usermanagement.smartshopy.repository.CustomerRepository;
import org.usermanagement.smartshopy.repository.OrderRepository;
import org.usermanagement.smartshopy.repository.ProductRepository;
import org.usermanagement.smartshopy.repository.PromoCodeRepository;
import org.usermanagement.smartshopy.service.Order.OrderServiceImpl;
import static org.mockito.ArgumentMatchers.any;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires - OrderService")
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PromoCodeRepository promoCodeRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Customer customer;
    private Product product;
    private PromoCode promoCode;
    private CreateOrderDTO createOrderDTO;
    private Order order;
    private OrderDTO orderDTO;

    @BeforeEach
    void setUp() {

        customer = Customer. builder()
                .id(1L)
                .nom("Test Customer")
                .email("test@example.com")
                .tier(CustomerTier.BASIC)
                .totalOrders(0)
                . totalSpent(BigDecimal.ZERO)
                .build();

        product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(new BigDecimal("100.00"))
                .stockQuantity(50)
                .active(true)
                .build();

        promoCode = PromoCode.builder()
                .id(1L)
                .code("TEST10")
                .discountPercentage(10)
                .maxUsages(100)
                . currentUsages(0)
                . build();

        createOrderDTO = CreateOrderDTO.builder()
                . customerId(1L)
                .items(List.of(
                        CreateOrderDTO.OrderItemDTO.builder()
                                . productId(1L)
                                .quantity(2)
                                .build()
                ))
                .build();

        order = Order.builder()
                .id(1L)
                .customer(customer)
                .items(new ArrayList<>())
                .subtotalHT(new BigDecimal("200.00"))
                .loyaltyDiscount(BigDecimal.ZERO)
                .totalAfterDiscount(new BigDecimal("200.00"))
                . tva(new BigDecimal("40.00"))
                .totalTTC(new BigDecimal("240.00"))
                .amountPaid(BigDecimal.ZERO)
                .remainingAmount(new BigDecimal("240. 00"))
                .status(OrderStatus.PENDING)
                . build();

        orderDTO = OrderDTO.builder()
                .id(1L)
                .subtotalHT(new BigDecimal("200.00"))
                . totalTTC(new BigDecimal("240.00"))
                .status(OrderStatus.PENDING)
                .build();
    }
// des testes sur creer une commande
    @Test
    @DisplayName("Créer une commande - Succès")
    void createOrder_Success() {
        // ARRANGE (Préparer)
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order. class))).thenReturn(order);
        when(orderMapper.toDTO(any(Order.class))). thenReturn(orderDTO);

        // ACT (Agir)
        OrderDTO result = orderService.createOrder(createOrderDTO);

        assertThat(result).isNotNull();
        assertThat(result. getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

        verify(customerRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderMapper, times(1)).toDTO(any(Order.class));

    }

    //test sur la foonction getOrderById

    @Test
    @DisplayName("Récupérer une commande par ID - Succès")
    void getOrderById_Success() {

        when(orderRepository.findById(1L)).thenReturn(Optional. of(order));
        when(orderMapper.toDTO(order)).thenReturn(orderDTO);

        OrderDTO result = orderService. getOrderById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(orderRepository, times(1)). findById(1L);
    }

    @Test
    @DisplayName("Récupérer une commande par ID - Introuvable")
    void getOrderById_NotFound() {
        // ARRANGE
        when(orderRepository.findById(999L)).thenReturn(Optional. empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> orderService.getOrderById(999L))
                .isInstanceOf(NotFoundException.class)
                . hasMessageContaining("Commande non trouvée");
    }

    //Test ConfirmerOrder

    @Test
    @DisplayName("Confirmer une commande - Succès")
    void confirmOrder_Success() {
        // ARRANGE
        order.setAmountPaid(order.getTotalTTC());  // Paiement complet
        order.setRemainingAmount(BigDecimal.ZERO);

        OrderItem orderItem = OrderItem.builder()
                .product(product)
                .quantity(2)
                .build();
        order.getItems().add(orderItem);

        when(orderRepository. findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(customerRepository.save(any(Customer.class))). thenReturn(customer);
        when(productRepository.save(any(Product. class))).thenReturn(product);
        when(orderMapper.toDTO(any(Order.class))).thenReturn(orderDTO);

        // ACT
        OrderDTO result = orderService.confirmOrder(1L);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        assertThat(order.getConfirmedAt()).isNotNull();
        assertThat(product.getStockQuantity()).isEqualTo(48);  // 50 - 2

        verify(orderRepository, times(1)).save(order);
        verify(productRepository, times(1)).save(product);
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    @DisplayName("Confirmer une commande - Commande introuvable")
    void confirmOrder_NotFound() {
        // ARRANGE
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> orderService.confirmOrder(999L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Commande introuvable");
    }

    // test sur la fonction cancel order

    @Test
    @DisplayName("Annuler une commande PENDING - Succès")
    void cancelOrder_Pending_Success() {
        // ARRANGE
        when(orderRepository.findById(1L)).thenReturn(Optional. of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDTO(any(Order.class))).thenReturn(orderDTO);

        // ACT
        OrderDTO result = orderService.cancelOrder(1L);

        // ASSERT
        assertThat(result). isNotNull();
        assertThat(order.getStatus()).isEqualTo(OrderStatus. CANCELLED);

        // Ne doit PAS restaurer le stock (car PENDING)
        verify(productRepository, never()).save(any());
        verify(customerRepository, never()).save(any());
    }


    @Test
    @DisplayName("Annuler une commande - Introuvable")
    void cancelOrder_NotFound() {
        // ARRANGE
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        // ACT & ASSERT
        assertThatThrownBy(() -> orderService.cancelOrder(999L))
                . isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Commande non trouvée");
    }

}
