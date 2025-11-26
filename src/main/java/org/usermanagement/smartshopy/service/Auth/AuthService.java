package org.usermanagement.smartshopy.service.Auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.LoginRequestDTO;
import org.usermanagement.smartshopy.dto.response.Userdto;
import org.usermanagement.smartshopy.entity.User;


public interface AuthService {

     Userdto login(LoginRequestDTO request , HttpSession session);
     void logout(HttpSession session);
     Userdto getCurrentUser(HttpSession session);


}
