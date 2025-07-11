ALTER TABLE cabins_for_sale
    ADD COLUMN category VARCHAR(30);

ALTER TABLE cabins_for_sale
RENAME TO properties_for_sale;