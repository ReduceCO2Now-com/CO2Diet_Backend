package com.reduceco2now.ingestion;

import java.util.List;

/**
 * Normalized representation of a food product, ready to be upserted into our
 * catalog regardless of which upstream {@link FoodSource} it came from.
 *
 * <p>Fields are intentionally nullable/optional where upstream data is often
 * sparse (e.g. {@code categoryCode}, {@code imageUrl}) — mappers should fill in
 * what they can and leave the rest {@code null} rather than fail the whole record.
 *
 * @param externalId    Stable identifier from the source system (e.g. OFF barcode/code).
 * @param sourceName    Short name of the originating source, e.g. {@code "off"}.
 * @param name          Product display name.
 * @param brand         Primary brand, if known.
 * @param categoryCode  Our internal category code, best-effort mapped from the
 *                      source's own taxonomy. {@code null} when no confident
 *                      mapping could be made.
 * @param quantityText  Free-text quantity/size as provided by the source (e.g. "500g").
 * @param imageUrl      URL of a representative product image, if any.
 * @param rawCategories The source's own raw category tags, kept around so
 *                      unmapped/ambiguous categories can be reviewed later.
 */
public record FoodUpsert(
        String externalId,
        String sourceName,
        String name,
        String brand,
        String categoryCode,
        String quantityText,
        String imageUrl,
        List<String> rawCategories
) {
}
