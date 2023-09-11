package com.capstone.mall.model.keyword;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Keyword {

    @Id
    private Long keywordId;

    @Id
    private Long itemId;
}
