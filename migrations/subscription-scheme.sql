CREATE TABLE IF NOT EXISTS subscription (
    chat_id BIGINT NOT NULL REFERENCES chat(id) ON DELETE CASCADE,
    link_id BIGINT NOT NULL REFERENCES link(id) ON DELETE CASCADE,

    PRIMARY KEY (chat_id, link_id)
);
