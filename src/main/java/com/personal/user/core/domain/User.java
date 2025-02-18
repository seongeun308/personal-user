package com.personal.user.core.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String password;
}

