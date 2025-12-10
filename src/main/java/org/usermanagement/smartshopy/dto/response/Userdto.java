package org.usermanagement.smartshopy.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.usermanagement.smartshopy.enums.UserRole;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Userdto {

    private long id;
    private String username;
    private UserRole role;
    private Long customerId;
    private LocalDate createdAt;

}
