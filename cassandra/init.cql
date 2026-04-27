CREATE KEYSPACE IF NOT EXISTS review_keyspace
    WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

CREATE TABLE IF NOT EXISTS review_keyspace.reviews (
    product_id uuid,
    author_id text,
    review_id uuid,
    rating int,
    content text,
    created_at timestamp,
    PRIMARY KEY (product_id, author_id)
) WITH CLUSTERING ORDER BY (author_id ASC);

CREATE INDEX IF NOT EXISTS ON review_keyspace.reviews (review_id);

CREATE TABLE IF NOT EXISTS review_keyspace.reviews_by_author (
    author_id text,
    created_at timestamp,
    review_id uuid,
    product_id uuid,
    rating int,
    content text,
    PRIMARY KEY (author_id, created_at, review_id)
) WITH CLUSTERING ORDER BY (created_at DESC, review_id ASC);

CREATE TABLE IF NOT EXISTS review_keyspace.reviews_by_product (
    product_id uuid,
    created_at timestamp,
    review_id uuid,
    author_id text,
    rating int,
    content text,
    PRIMARY KEY (product_id, created_at, review_id)
) WITH CLUSTERING ORDER BY (created_at DESC, review_id ASC);

CREATE TABLE IF NOT EXISTS review_keyspace.replies (
    parent_id uuid,
    reply_id uuid,
    author_id text,
    content text,
    created_at timestamp,
    PRIMARY KEY (parent_id, reply_id)
) WITH CLUSTERING ORDER BY (reply_id DESC);

CREATE INDEX IF NOT EXISTS ON review_keyspace.replies (reply_id);
