package org.usermanagement.smartshopy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.usermanagement.smartshopy.dto.response.CustomerDto;
import org.usermanagement.smartshopy.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
     CustomerDto toDto(Customer customer);
     Customer toEntity(CustomerDto cutomerDto);
     void UpdateEntityfromDto(CustomerDto dto , @MappingTarget Customer customer);


}
