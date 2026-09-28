-- Kinoicon API schema (PostgreSQL)
-- Run this once against an empty database to bootstrap the schema.

CREATE EXTENSION IF NOT EXISTS pgcrypto; -- gen_random_uuid()
CREATE EXTENSION IF NOT EXISTS pg_trgm;  -- trigram search

-- =========================================================
-- SEQUENCES
-- =========================================================
CREATE SEQUENCE IF NOT EXISTS app_user_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS film_id_seq START WITH 100000;
CREATE SEQUENCE IF NOT EXISTS person_id_seq START WITH 100000;
CREATE SEQUENCE IF NOT EXISTS film_image_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS film_trailer_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS person_image_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS film_crew_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS featured_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS featured_row_id_seq START WITH 1;
CREATE SEQUENCE IF NOT EXISTS featured_row_item_id_seq START WITH 1;

-- =========================================================
-- USER (auth, no server-side session - JWT only)
-- =========================================================
CREATE TABLE IF NOT EXISTS app_user (
    id          BIGINT PRIMARY KEY DEFAULT nextval('app_user_id_seq'),
    uuid        UUID NOT NULL DEFAULT gen_random_uuid(),
    username    VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    rights      TEXT[] NOT NULL DEFAULT ARRAY['USER']::TEXT[]
);

-- =========================================================
-- FILM
-- =========================================================
CREATE TABLE IF NOT EXISTS film (
    id                      BIGINT PRIMARY KEY DEFAULT nextval('film_id_seq'),
    uuid                    UUID NOT NULL DEFAULT gen_random_uuid(),

    name_en                 VARCHAR(500),
    name_native             VARCHAR(500),
    name_ru                 VARCHAR(500),

    date                    DATE,
    duration                INTEGER,

    logline_en              TEXT,
    logline_native          TEXT,
    logline_ru              TEXT,

    budget_amount           INTEGER,
    budget_currency         VARCHAR(10),

    gross_russia_amount     INTEGER,
    gross_russia_currency   VARCHAR(10),
    gross_usa_amount        INTEGER,
    gross_usa_currency      VARCHAR(10),
    gross_world_amount      INTEGER,
    gross_world_currency    VARCHAR(10),

    external_imdb_id        VARCHAR(50),
    external_kp_id           VARCHAR(50),

    rating_imdb_rating      VARCHAR(20),
    rating_imdb_vote_count  INTEGER,
    rating_kp_rating        VARCHAR(20),
    rating_kp_vote_count    INTEGER,
    rating_metacritic       INTEGER,
    rating_rottentomatos    NUMERIC,

    cover_url               VARCHAR(1000),

    published               BOOLEAN NOT NULL DEFAULT FALSE,
    schema_version          INTEGER NOT NULL DEFAULT 1,

    -- Plain column, not a GENERATED ... STORED column: kept in sync by the trigger below
    -- instead, since generated columns are PG12+ only and some SQL clients mis-parse the
    -- inline GENERATED ALWAYS AS (...) STORED syntax when splitting multi-statement scripts.
    search_text             TEXT
);

CREATE OR REPLACE FUNCTION film_search_text_update() RETURNS trigger AS $$
BEGIN
    NEW.search_text := coalesce(NEW.name_en, '') || ' ' || coalesce(NEW.name_ru, '') || ' ' || coalesce(NEW.name_native, '');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS film_search_text_trigger ON film;
CREATE TRIGGER film_search_text_trigger
    BEFORE INSERT OR UPDATE ON film
    FOR EACH ROW EXECUTE FUNCTION film_search_text_update();

CREATE INDEX IF NOT EXISTS film_search_trgm_idx ON film USING gin (search_text gin_trgm_ops);

CREATE TABLE IF NOT EXISTS film_image (
    id          BIGINT PRIMARY KEY DEFAULT nextval('film_image_id_seq'),
    film_id     BIGINT NOT NULL REFERENCES film(id) ON DELETE CASCADE,
    image_url   VARCHAR(1000) NOT NULL,
    comment     VARCHAR(1000),
    tag         VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS film_image_film_id_idx ON film_image(film_id);

CREATE TABLE IF NOT EXISTS film_trailer (
    id          BIGINT PRIMARY KEY DEFAULT nextval('film_trailer_id_seq'),
    film_id     BIGINT NOT NULL REFERENCES film(id) ON DELETE CASCADE,
    language    VARCHAR(50),
    title       VARCHAR(500),
    video_url   VARCHAR(1000) NOT NULL
);
CREATE INDEX IF NOT EXISTS film_trailer_film_id_idx ON film_trailer(film_id);

-- =========================================================
-- PERSON
-- =========================================================
CREATE TABLE IF NOT EXISTS person (
    id              BIGINT PRIMARY KEY DEFAULT nextval('person_id_seq'),
    uuid            UUID NOT NULL DEFAULT gen_random_uuid(),

    name_en         VARCHAR(500),
    name_native     VARCHAR(500),
    name_ru         VARCHAR(500),

    born_city       VARCHAR(255),
    born_date       DATE,
    died_city       VARCHAR(255),
    died_date       DATE,

    length          INTEGER,
    cover_url       VARCHAR(1000),

    published       BOOLEAN NOT NULL DEFAULT FALSE,
    schema_version  INTEGER NOT NULL DEFAULT 1,

    -- Same rationale as film.search_text - plain column, kept in sync by a trigger.
    search_text     TEXT
);

CREATE OR REPLACE FUNCTION person_search_text_update() RETURNS trigger AS $$
BEGIN
    NEW.search_text := coalesce(NEW.name_en, '') || ' ' || coalesce(NEW.name_ru, '') || ' ' || coalesce(NEW.name_native, '');
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS person_search_text_trigger ON person;
CREATE TRIGGER person_search_text_trigger
    BEFORE INSERT OR UPDATE ON person
    FOR EACH ROW EXECUTE FUNCTION person_search_text_update();

CREATE INDEX IF NOT EXISTS person_search_trgm_idx ON person USING gin (search_text gin_trgm_ops);

CREATE TABLE IF NOT EXISTS person_image (
    id          BIGINT PRIMARY KEY DEFAULT nextval('person_image_id_seq'),
    person_id   BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    image_url   VARCHAR(1000) NOT NULL,
    comment     VARCHAR(1000),
    tag         VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS person_image_person_id_idx ON person_image(person_id);

-- =========================================================
-- FILM <-> PERSON (many-to-many, the only M:N in the project)
-- =========================================================
CREATE TABLE IF NOT EXISTS film_crew (
    id              BIGINT PRIMARY KEY DEFAULT nextval('film_crew_id_seq'),
    film_id         BIGINT NOT NULL REFERENCES film(id) ON DELETE CASCADE,
    person_id       BIGINT NOT NULL REFERENCES person(id) ON DELETE CASCADE,
    is_lead         BOOLEAN NOT NULL DEFAULT FALSE,
    leading_roles   TEXT[],
    other_roles     TEXT[],
    note            TEXT,
    UNIQUE (film_id, person_id)
);
CREATE INDEX IF NOT EXISTS film_crew_film_id_idx ON film_crew(film_id);
CREATE INDEX IF NOT EXISTS film_crew_person_id_idx ON film_crew(person_id);

-- =========================================================
-- FEATURED (singleton: main page data)
-- =========================================================
CREATE TABLE IF NOT EXISTS featured (
    id      BIGINT PRIMARY KEY DEFAULT nextval('featured_id_seq'),
    title   VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS featured_row (
    id          BIGINT PRIMARY KEY DEFAULT nextval('featured_row_id_seq'),
    featured_id BIGINT NOT NULL REFERENCES featured(id) ON DELETE CASCADE,
    position    INTEGER NOT NULL DEFAULT 0,
    kind        VARCHAR(100),
    title       VARCHAR(500)
);
CREATE INDEX IF NOT EXISTS featured_row_featured_id_idx ON featured_row(featured_id);

CREATE TABLE IF NOT EXISTS featured_row_item (
    id              BIGINT PRIMARY KEY DEFAULT nextval('featured_row_item_id_seq'),
    row_id          BIGINT NOT NULL REFERENCES featured_row(id) ON DELETE CASCADE,
    position        INTEGER NOT NULL DEFAULT 0,
    item_kind       VARCHAR(50) NOT NULL, -- 'film' | 'person'
    item_short_id   BIGINT NOT NULL
);
CREATE INDEX IF NOT EXISTS featured_row_item_row_id_idx ON featured_row_item(row_id);

-- Ensure there's always exactly one featured row to update (mirrors Featured.objects.first())
INSERT INTO featured (id, title)
SELECT nextval('featured_id_seq'), 'Featured'
WHERE NOT EXISTS (SELECT 1 FROM featured);
