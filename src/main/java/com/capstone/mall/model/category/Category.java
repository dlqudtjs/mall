package com.capstone.mall.model.category;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "parent_category_id", nullable = true)
    private Long parentCategoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;
}
