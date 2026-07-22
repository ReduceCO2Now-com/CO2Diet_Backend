package com.reduceco2now.catalog;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
class CatalogService implements CatalogQuery, CatalogCommand {

    private final FoodProductRepository repo;

    CatalogService(FoodProductRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Food> byId(long id) {
        return repo.findById(id).map(FoodProductEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Food> byBarcode(String barcode) {
        return repo.findByBarcode(barcode).map(FoodProductEntity::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Food> search(String query, String categoryCode, int limit) {
        return repo.search(query, categoryCode).stream()
                .limit(Math.max(1, limit))
                .map(FoodProductEntity::toDomain)
                .toList();
    }

    @Override
    public Food upsert(FoodUpsert upsert) {
        FoodProductEntity entity = repo.findByBarcode(upsert.barcode())
                .map(existing -> existing.updateFrom(upsert))
                .orElseGet(() -> FoodProductEntity.newFrom(upsert));
        return repo.save(entity).toDomain();
    }
}