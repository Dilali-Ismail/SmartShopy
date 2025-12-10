package org.usermanagement.smartshopy.security;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.usermanagement.smartshopy.dto.response.Userdto;
import org.usermanagement.smartshopy.enums.UserRole;
import org.usermanagement.smartshopy.exception.UnautorizedException;
import org.usermanagement.smartshopy.service.Auth.AuthServiceImpl;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RoleAspect {

    private final AuthServiceImpl authService;

    @Before("@annotation(org.usermanagement.smartshopy.security.RequireRole) || " +
            "@within(org.usermanagement.smartshopy.security.RequireRole)")
    public void checkRole(JoinPoint joinPoint){

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new UnautorizedException("Impossible de recuperer la session");
        }

        HttpSession session = attributes.getRequest().getSession(false);

        if (!authService.isAuthenticated(session)) {
            throw new UnautorizedException("il faut etre connecter ");
        }

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        RequireRole requireRole = method.getAnnotation(RequireRole.class);

        if (requireRole == null) {
            requireRole = joinPoint.getTarget().getClass().getAnnotation(RequireRole.class);
        }

        UserRole[] requiredRoles = requireRole.value();

        if (!authService.hasAnyRole(session, requiredRoles)) {
            throw new UnautorizedException(String.format("Acces refuse."));
        }


//        Userdto user = authService.getCurrentUser(session);
//        log.debug(" Accès autorisé pour {} (rôle: {})", user.getUsername(), user.getRole());

    }


}
