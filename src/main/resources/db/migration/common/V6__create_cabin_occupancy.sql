CREATE TABLE cabin_occupancy
(
    id         SERIAL PRIMARY KEY,                    -- Auto-incrementing ID
    date       DATE      NOT NULL,                    -- Occupancy date
    cabin_id   INT       NOT NULL,                    -- ID of the cabin
    created_at TIMESTAMP NOT NULL,                    -- Record creation timestamp
    CONSTRAINT fk_cabin
        FOREIGN KEY (cabin_id) REFERENCES cabins (id) -- Assuming cabins table exists
);
