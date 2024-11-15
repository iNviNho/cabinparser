ALTER TABLE cabin_occupancy
    ADD CONSTRAINT date_cabin_id
        UNIQUE (date, cabin_id);
