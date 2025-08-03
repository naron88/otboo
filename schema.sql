CREATE TABLE "users"
(
    "id"                      UUID         NOT NULL,
    "created_at"              TIMESTAMPTZ  NOT NULL,
    "updated_at"              TIMESTAMPTZ NULL,
    "email"                   VARCHAR(100) NOT NULL,
    "name"                    VARCHAR      NOT NULL,
    "password"                VARCHAR      NOT NULL,
    "gender"                  VARCHAR NULL,
    "birth_date"              DATE NULL,
    "temperature_sensitivity" INTEGER NULL,
    "profile_image_url"       TEXT NULL,
    "role"                    VARCHAR(10)  NOT NULL,
    "locked"                  BOOLEAN      NOT NULL,
    "location_id"             UUID NULL
);