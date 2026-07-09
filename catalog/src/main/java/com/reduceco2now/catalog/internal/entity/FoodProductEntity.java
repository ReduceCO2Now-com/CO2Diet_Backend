package com.reduceco2now.catalog.internal.entity;

import com.reduceco2now.catalog.Food;
import com.reduceco2now.catalog.FoodUpsert;
import jakarta.persistence.*;


@Entity
@Table(name = "food_product")
public class FoodProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "food_product_seq")
    @SequenceGenerator(name = "food_product_seq", sequenceName = "food_product_id_seq", allocationSize = 1)
    private Long id;

    private String barcode;
    private String name;
    private String brand;

    @Column(name = "category_code")
    private String categoryCode;

    @Column(name = "row_version", nullable = false)
    private long rowVersion;

    protected FoodProductEntity() {

    }
    FoodProductEntity(Long id, String barcode, String name, String brand,
                      String categoryCode, long rowVersion) {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.brand = brand;
        this.categoryCode = categoryCode;
        this.rowVersion = rowVersion;
    }

    public Food toDomain() {

        return new Food(id, barcode, name, brand, categoryCode, rowVersion);
    }


    static FoodProductEntity newFrom(FoodUpsert u) {
        FoodProductEntity e = new FoodProductEntity();
        e.barcode = u.barcode();
        e.name = u.name();
        e.brand = u.brand();
        e.categoryCode = u.categoryCode();
        e.rowVersion = 1L;
        return e;
    }

    FoodProductEntity updateFrom(FoodUpsert u) {
        this.name = u.name();
        this.brand = u.brand();
        this.categoryCode = u.categoryCode();
        this.rowVersion += 1;
        return this;
    }
}