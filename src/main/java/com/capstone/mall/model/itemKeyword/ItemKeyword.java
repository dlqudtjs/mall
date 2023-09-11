package com.capstone.mall.model.itemKeyword;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "itemKeyword")
public class ItemKeyword implements Serializable {

    @EmbeddedId
    private ItemKeywordID itemKeywordID;
}
