package com.capstone.mall.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "meta_id", nullable = false)
    private String metaId;
    
    @Column(nullable = false)
    private Role role;
}
