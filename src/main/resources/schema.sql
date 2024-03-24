CREATE TABLE IF NOT EXISTS TODO (
  id INTEGER not null,
  title VARCHAR(30) not null,
  description VARCHAR(30),
  is_completed SMALLINT,
  created_at DATE,
  updated_at DATE,
  PRIMARY KEY (id)
);