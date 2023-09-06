package com.capstone.mall.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User")
public class User {

    @Id
    @Column(name = "meta_id", nullable = false)
    private Long metaId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private Role role;
}
