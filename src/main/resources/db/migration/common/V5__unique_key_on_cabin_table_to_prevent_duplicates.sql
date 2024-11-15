ALTER TABLE cabins
    ADD CONSTRAINT unique_vendor_id_name
        UNIQUE (vendor_unique_id, name);
