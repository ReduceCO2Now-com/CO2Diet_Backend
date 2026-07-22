package com.reduceco2now.catalog;

import com.reduceco2now.shared.error.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/foods")
class FoodController {

    private final CatalogQuery catalog;

    FoodController(CatalogQuery catalog) {
        this.catalog = catalog;
    }

    @GetMapping("/search")
    List<Food> search(@RequestParam(name = "q", defaultValue = "") String q,
                      @RequestParam(name = "category", required = false) String category,
                      @RequestParam(name = "limit", defaultValue = "50") int limit) {
        return catalog.search(q, category, limit);
    }

    @GetMapping("/barcode/{code}")
    Food byBarcode(@PathVariable String code) {
        return catalog.byBarcode(code)
                .orElseThrow(() -> new NotFoundException("No product for barcode " + code));
    }

    @GetMapping("/{id}")
    Food byId(@PathVariable long id) {
        return catalog.byId(id)
                .orElseThrow(() -> new NotFoundException("No product with id " + id));
    }
}