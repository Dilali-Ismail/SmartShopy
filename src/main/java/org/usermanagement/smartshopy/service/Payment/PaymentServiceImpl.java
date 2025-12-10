package org.usermanagement.smartshopy.service.Payment;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.CreatePaymentDTO;
import org.usermanagement.smartshopy.dto.response.PaymentDTO;
import org.usermanagement.smartshopy.entity.Order;
import org.usermanagement.smartshopy.entity.Payment;
import org.usermanagement.smartshopy.enums.OrderStatus;
import org.usermanagement.smartshopy.enums.PaymentStatus;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement.smartshopy.mapper.PaymentMapper;
import org.usermanagement.smartshopy.repository.OrderRepository;
import org.usermanagement.smartshopy.repository.PaymentRepository;
import org.usermanagement.smartshopy.service.Order.OrderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            @Lazy OrderService orderService,  // ✅ @Lazy résout la dépendance circulaire
            PaymentMapper paymentMapper) {

        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
        this.paymentMapper = paymentMapper;
    }

   public  PaymentDTO createPayment(CreatePaymentDTO dto){

       Order order = orderRepository.findById(dto.getOrderId()).orElseThrow(()-> new NotFoundException("Commande not found"));
         if(!order.getStatus().equals(OrderStatus.PENDING)){
             throw new BadRequestException("Impossible d'ajouter un paiement à cette commande");
         }

         if(dto.getAmount().compareTo(order.getRemainingAmount())> 0){
             throw new BadRequestException("Le montant du paiement dépasse le reste à payer");
         }

       Payment payment = Payment.builder()
               .amount(dto.getAmount())
               .method(dto.getMethod())
               .status(PaymentStatus.ENCAISSE)
               .reference(dto.getReference())
               .notes(dto.getNotes())
               .build();

         order.addPayment(payment);

       BigDecimal newAmountPaid = order.getAmountPaid().add(dto.getAmount());
       order.setAmountPaid(newAmountPaid);

       BigDecimal RestAmountPaid = order.getTotalTTC().subtract(newAmountPaid);
       order.setRemainingAmount(RestAmountPaid);

       paymentRepository.save(payment);
       orderRepository.save(order);

       if (order.isFullyPaid()) {
           orderService.confirmOrder(order.getId());
       }

     return paymentMapper.toDTO(payment);
   }

   public  PaymentDTO getPayment(Long id){

       Payment payment = paymentRepository.findById(id).orElseThrow(()-> new NotFoundException("payment not found"));
       return paymentMapper.toDTO(payment);

   }

    public List<PaymentDTO> getPaymentsByOrder(Long orderId){

       if(!orderRepository.existsById(orderId)){

           throw new NotFoundException("Order introvable");

       }

         return paymentRepository.findByOrderId(orderId).stream().map(paymentMapper::toDTO).collect(Collectors.toList());
    }


}
