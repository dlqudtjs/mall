package com.capstone.mall.model.category;

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
@Table(name = "category")
public class Category {

    @Id
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "parent_category_id", nullable = true)
    private Long parentCategoryId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;
}
