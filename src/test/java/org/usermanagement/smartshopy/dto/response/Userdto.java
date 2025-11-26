package org.usermanagement.smartshopy.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.usermanagement.smartshopy.enums.UserRole;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Userdto {

    private long id;
    private String username;
    private UserRole role;
    

}
