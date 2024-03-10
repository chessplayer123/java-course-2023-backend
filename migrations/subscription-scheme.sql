CREATE TABLE IF NOT EXISTS subscription (
    chat_id BIGINT REFERENCES chat(id) NOT NULL,
    link_id BIGINT REFERENCES link(id) NOT NULL,
    PRIMARY KEY (chat_id, link_id)
);
