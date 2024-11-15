ALTER TABLE cabins
    ADD COLUMN district       VARCHAR(255),
    ADD COLUMN region         VARCHAR(255),
    ADD COLUMN geocoding_dump JSON;