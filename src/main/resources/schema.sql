CREATE TABLE "users"
(
    id                      UUID          PRIMARY KEY,
    created_at              TIMESTAMPTZ   NOT NULL,
    updated_at              TIMESTAMPTZ,
    email                   VARCHAR(100)  NOT NULL,
    name                    VARCHAR       NOT NULL,
    password                VARCHAR       NOT NULL,
    gender                  VARCHAR,
    birth_date              DATE,
    temperature_sensitivity INTEGER,
    profile_image_url       TEXT,
    role                    VARCHAR(10)   NOT NULL,
    locked                  BOOLEAN       NOT NULL,
    location_id             UUID
);

create table auth_tokens
(
    id            UUID          PRIMARY KEY,
    user_id       UUID          NOT NULL UNIQUE,
    access_token  TEXT,
    refresh_token TEXT,
    created_at    TIMESTAMPTZ   NOT NULL,
    updated_at    TIMESTAMPTZ
);
