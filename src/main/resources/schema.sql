DROP TABLE IF EXISTS users, items, bookings, comments;


CREATE TABLE IF NOT EXISTS users
(
    id    SERIAL PRIMARY KEY,
    name  VARCHAR                                             NOT NULL,
    email VARCHAR                                             NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR                                             NOT NULL,
    description VARCHAR,
    available   BOOLEAN,
    owner_id    BIGINT REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         SERIAL PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE,
    end_date   TIMESTAMP WITHOUT TIME ZONE,
    item_id    BIGINT REFERENCES items (id),
    booker_id  BIGINT REFERENCES users (id),
    status     varchar
);

CREATE TABLE IF NOT EXISTS comments (
    id SERIAL PRIMARY KEY,
    text VARCHAR,
    item_id BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES users (id),
    created TIMESTAMP WITHOUT TIME ZONE
);