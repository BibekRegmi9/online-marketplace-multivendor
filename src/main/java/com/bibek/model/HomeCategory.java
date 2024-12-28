package com.bibek.model;

import com.bibek.enums.HomeCategorySection;
import com.bibek.utils.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "home_category")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class HomeCategory extends BaseEntity {
    @Id
    @SequenceGenerator(name = "home_category_seq_gen", sequenceName = "home_category_seq")
    @GeneratedValue(generator = "home_category_seq_gen", strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String image;

    private String categoryId;

    private HomeCategorySection section;
}
