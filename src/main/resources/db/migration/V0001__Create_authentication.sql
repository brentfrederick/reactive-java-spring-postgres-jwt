CREATE TABLE users (
  id uuid NOT NULL,
  version integer NOT NULL DEFAULT 0,
  created_time timestamp with time zone NOT NULL,
  updated_time timestamp with time zone NOT NULL,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  role varchar(255) NOT NULL,
  enabled boolean NOT NULL,
  account_non_locked boolean NOT NULL,
  account_non_expired boolean NOT NULL,
  credentials_non_expired boolean NOT NULL,
  push_id varchar(255),
  brand varchar(255),
  build_id varchar(255),
  carrier varchar(255),
  device_id varchar(255),
  device_token varchar(255) NOT NULL,
  manufacturer varchar(255),
  model varchar(255),
  system_name varchar(255),
  system_version varchar(255),
  unique_id varchar(255) NOT NULL,
  app_version varchar(255),
  app_build_number varchar(255),
  PRIMARY KEY (id)
);

CREATE UNIQUE INDEX idx_unique_users_username ON users(username);

CREATE TABLE conversation (
    id uuid NOT NULL,
    created_time timestamp with time zone NOT NULL,
    updated_time timestamp with time zone NOT NULL,
    author_id uuid NOT NULL,
    starter text,
    PRIMARY KEY (id),
    CONSTRAINT fk_conversation_author_id FOREIGN KEY (author_id) REFERENCES users(id)
);

CREATE INDEX idx_conversation_author_id ON conversation(author_id);

CREATE TABLE reply (
    id uuid NOT NULL,
    created_time timestamp with time zone NOT NULL,
    updated_time timestamp with time zone NOT NULL,
    conversation_id uuid NOT NULL,
    reply text NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_reply_conversation_id FOREIGN KEY (conversation_id) REFERENCES conversation(id)
);

CREATE INDEX idx_conversation_id ON reply(conversation_id);
