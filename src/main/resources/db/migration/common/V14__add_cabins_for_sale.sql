CREATE TABLE cabins_for_sale (
    id UUID PRIMARY KEY,
    title TEXT NOT NULL,
    price DECIMAL(8, 2),
    locality TEXT,
    updated_at TIMESTAMP,
    created_at TIMESTAMP,
    house_estate TEXT,
    floor_estate TEXT,
    estate TEXT,
    gps_position_latitude DOUBLE PRECISION,
    gps_position_longitude DOUBLE PRECISION,
    pictures JSONB
);