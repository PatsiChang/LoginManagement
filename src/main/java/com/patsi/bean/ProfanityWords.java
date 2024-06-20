package com.patsi.bean;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ProfanityWords")
public class ProfanityWords {
    @Id
    @NotNull
    private String word;
}
