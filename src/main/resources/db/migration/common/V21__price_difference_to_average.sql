ALTER TABLE properties_for_sale
    ADD COLUMN region_price_to_average_difference DECIMAL(8, 2),
    ADD COLUMN district_price_to_average_difference DECIMAL(8, 2),
    ADD COLUMN locality_price_to_average_difference DECIMAL(8, 2)
;