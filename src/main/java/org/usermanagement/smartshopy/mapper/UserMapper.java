package org.usermanagement.smartshopy.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.usermanagement.smartshopy.dto.request.LoginRequestDTO;
import org.usermanagement.smartshopy.dto.response.Userdto;
import org.usermanagement.smartshopy.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper  {


    @Mapping(target = "customerId", source = "customer.id")
    Userdto toDto(User entity);
}
