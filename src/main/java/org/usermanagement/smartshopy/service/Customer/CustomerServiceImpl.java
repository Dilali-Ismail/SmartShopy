package org.usermanagement.smartshopy.service.Customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.CreateCustomerDTO;
import org.usermanagement.smartshopy.dto.response.CustomerDto;
import org.usermanagement.smartshopy.entity.Customer;
import org.usermanagement.smartshopy.entity.User;
import org.usermanagement.smartshopy.enums.CustomerTier;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.mapper.CustomerMapper;
import org.usermanagement.smartshopy.mapper.UserMapper;
import org.usermanagement.smartshopy.repository.CustomerRepository;
import org.usermanagement.smartshopy.repository.UserRepository;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

   public  CustomerDto createCustomeer(CreateCustomerDTO dto){

       if(customerRepository.existByEmail(dto.getEmail())){
           throw new BadRequestException("Email deja utiliser");
       }
       if(userRepository.existsByUsername(dto.getUsername())){
           throw new BadRequestException("Username deja utiliser");
       }

       User user = User.builder()
               .username(dto.getUsername())
               .password(dto.getPassword())
               .role(UserRole.CLIENT)
               .build();

       userRepository.save(user);

      Customer customer = customerMapper.toEntity(dto);
      customer.setUser(user);
      customer.setTier(CustomerTier.BASIC);
      customer.setTotalOrders(0);
      customer.setTotalSpent(BigDecimal.ZERO);

      customerRepository.save(customer);

      return customerMapper.toDto(customer);
   };




}
