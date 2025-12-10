package org.usermanagement.smartshopy.service.Customer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.CreateCustomerDTO;
import org.usermanagement.smartshopy.dto.request.UpdateCustomerDTO;
import org.usermanagement.smartshopy.dto.response.CustomerDto;
import org.usermanagement.smartshopy.entity.Customer;
import org.usermanagement.smartshopy.entity.User;
import org.usermanagement.smartshopy.enums.CustomerTier;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.exception.BadRequestException;
import org.usermanagement.smartshopy.exception.NotFoundException;
import org.usermanagement.smartshopy.mapper.CustomerMapper;
import org.usermanagement.smartshopy.mapper.UserMapper;
import org.usermanagement.smartshopy.repository.CustomerRepository;
import org.usermanagement.smartshopy.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;

   public  CustomerDto createCustomeer(CreateCustomerDTO dto){

       if(customerRepository.existsByEmail(dto.getEmail())){
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


   public List<CustomerDto> getAllCustomers(){
         return customerRepository.findAll()
                 .stream()
                 .map(customerMapper::toDto)
                 .collect(Collectors.toList());
    }

    public List<CustomerDto> getCustomersByTier(CustomerTier tier) {

        return customerRepository.findCustomerByTier(tier). stream()
                .map(customerMapper::toDto)
                . collect(Collectors.toList());
    }

    public  CustomerDto getCustomer(Long id){
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Client introuvable avec l'ID : " + id));
        return customerMapper.toDto(customer);
    }

    public CustomerDto updateCustomer(Long id , UpdateCustomerDTO dtoUp){
       Customer customer = customerRepository.findById(id)
               .orElseThrow(()-> new NotFoundException("Client introuvable avec l'ID : " + id));

       customerMapper.UpdateEntityfromDto(dtoUp,customer);

       customerRepository.save(customer);

       return customerMapper.toDto(customer);
    }

   public  void deleteCustomer(Long id){
       Customer customer = customerRepository.findById(id)
               .orElseThrow(() -> new NotFoundException("Client introuvable avec l'ID : " + id));
       User user = customer.getUser();
       customerRepository.delete(customer);
       if (user != null) {
           userRepository.delete(user);
       }
   }

}
