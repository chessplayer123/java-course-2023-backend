CREATE TABLE IF NOT EXISTS link (
    id              BIGINT NOT NULL PRIMARY KEY,
    url             TEXT   NOT NULL UNIQUE,
    data            TEXT,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL,
    last_check_time TIMESTAMP WITH TIME ZONE NOT NULL
);
