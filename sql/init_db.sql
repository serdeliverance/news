DROP TABLE IF EXISTS "headlines";

CREATE TABLE IF NOT EXISTS "headlines" (
  link VARCHAR PRIMARY KEY,
  title VARCHAR NOT NULL
);
