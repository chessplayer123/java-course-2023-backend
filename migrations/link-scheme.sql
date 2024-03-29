CREATE TABLE IF NOT EXISTS link (
    id              BIGINT GENERATED ALWAYS AS IDENTITY,
    url             TEXT   NOT NULL UNIQUE,
    description     TEXT   NOT NULL,

    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_check_time TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id)
);
