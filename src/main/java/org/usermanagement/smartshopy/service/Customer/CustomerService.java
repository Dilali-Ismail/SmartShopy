package org.usermanagement.smartshopy.service.Customer;


import org.usermanagement.smartshopy.dto.request.CreateCustomerDTO;
import org.usermanagement.smartshopy.dto.request.UpdateCustomerDTO;
import org.usermanagement.smartshopy.dto.response.CustomerDto;
import org.usermanagement.smartshopy.entity.Customer;

import java.util.List;

public interface CustomerService {
       CustomerDto createCustomeer(CreateCustomerDTO dto);

//     List<CustomerDto> getAllCustomers();
//     CustomerDto getCustomer(Long id);
//     CustomerDto updateCustomer(Long id , UpdateCustomerDTO dtoUp);
//     void deleteCustomer(Long id);

}
