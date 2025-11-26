package org.usermanagement.smartshopy.mapper;

import org.mapstruct.Mapper;
import org.usermanagement.smartshopy.dto.request.LoginRequestDTO;
import org.usermanagement.smartshopy.dto.response.Userdto;
import org.usermanagement.smartshopy.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper  {
   Userdto toDto(User entity);
   User toEntity(Userdto dto);
}
