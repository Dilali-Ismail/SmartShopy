package org.usermanagement.smartshopy.service.Auth;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.usermanagement.smartshopy.dto.request.LoginRequestDTO;
import org.usermanagement.smartshopy.dto.response.Userdto;
import org.usermanagement.smartshopy.entity.User;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.exception.UnautorizedException;
import org.usermanagement.smartshopy.mapper.UserMapper;
import org.usermanagement.smartshopy.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final String Session_User_key = "SessionUserKey";
   public  Userdto login(LoginRequestDTO request , HttpSession session){

       User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
               ()-> new UnautorizedException("Username ou password incorrect")
       );

       if(!user.getPassword().equals(request.getPassword())){
           throw new UnautorizedException("Username ou password incorrect");
       }

       Userdto userdto=  userMapper.toDto(user);
       session.setAttribute("username",userdto);
       session.setAttribute(Session_User_key,userdto);
       session.setMaxInactiveInterval(30 * 60);

       return userdto;

   }
   public  void logout(HttpSession session){
       Userdto currentUser = (Userdto) session.getAttribute(Session_User_key);
       if (currentUser != null) {
           log.info("Déconnexion de l'utilisateur: {}", currentUser.getUsername());
       }
       session.invalidate();
   }
   public  Userdto getCurrentUser(HttpSession session){
       Userdto user = (Userdto) session.getAttribute(Session_User_key);
       if (user == null) {
           throw new UnautorizedException("Aucun utilisateur connecté");
       }
       return user;
   }

    public boolean isAuthenticated(HttpSession session) {
        if (session == null) {
            return false;
        }
        return session.getAttribute(Session_User_key) != null;
    }

    public boolean hasRole(HttpSession session, UserRole role) {
        if (!isAuthenticated(session)) {
            return false;
        }

        Userdto user = (Userdto) session.getAttribute(Session_User_key);
        return user. getRole() == role;
    }
    public boolean hasAnyRole(HttpSession session, UserRole... roles) {
        if (!isAuthenticated(session)) {
            return false;
        }

        Userdto user = (Userdto) session.getAttribute(Session_User_key);

        for (UserRole role : roles) {
            if (user.getRole() == role) {
                return true;
            }
        }

        return false;
    }
}
