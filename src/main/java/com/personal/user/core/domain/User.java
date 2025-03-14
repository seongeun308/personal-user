package com.personal.user.core.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.redis.core.index.Indexed;

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
    @Indexed
    private String email;
    private String password;
}

