package com.patsi.bean;

import com.common.validation.annotations.IsEmail;
import com.common.validation.annotations.IsPassword;
import com.patsi.annotations.IsUserName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "unverifiedperson")
public class UnverifiedPerson {
    @Id
    private String token;
    @NotNull
    @NotEmpty
    @Column(name = "USERID")
    private String userId;
    @NotNull
    @NotEmpty
    @IsUserName
    private String name;
    @NotNull
    @NotEmpty
    @IsEmail
    private String email;
    @NotNull
    @NotEmpty
    @IsPassword(min = 4)
    private String password;
}
