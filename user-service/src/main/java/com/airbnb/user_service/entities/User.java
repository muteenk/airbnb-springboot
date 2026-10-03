package com.airbnb.user_service.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String provider;

    private String providerSubject;

    @Column(
            name = "created_at"
    )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(
            name = "updated_at"
    )
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
