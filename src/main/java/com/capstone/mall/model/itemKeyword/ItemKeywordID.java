package com.capstone.mall.model.itemKeyword;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ItemKeywordID implements Serializable {

    @Column(name = "keyword_id")
    private Long keywordId;

    @Column(name = "item_id")
    private Long itemId;
}
