package com.patsi.bean;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLogin {
    @Column(name = "USERID")
    @NotNull
    @NotEmpty
    private String userId;
    @NotNull
    @NotEmpty
    private String password;
}
