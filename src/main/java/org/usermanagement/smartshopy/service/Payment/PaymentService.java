package org.usermanagement.smartshopy.service.Payment;

import org.usermanagement.smartshopy.dto.request.CreatePaymentDTO;
import org.usermanagement.smartshopy.dto.response.PaymentDTO;

import java.util.List;

public interface PaymentService {

    PaymentDTO createPayment(CreatePaymentDTO dto);
    PaymentDTO getPayment(Long id);
    List<PaymentDTO> getPaymentsByOrder(Long orderId);
}
