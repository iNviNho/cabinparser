ALTER TABLE cabins_for_sale
    ALTER COLUMN house_estate TYPE DECIMAL(8, 2) USING house_estate::DECIMAL(8, 2),
    ALTER COLUMN floor_estate TYPE DECIMAL(8, 2) USING floor_estate::DECIMAL(8, 2),
    ALTER COLUMN estate TYPE DECIMAL(8, 2) USING estate::DECIMAL(8, 2);