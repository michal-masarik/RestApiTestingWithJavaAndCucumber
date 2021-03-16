CREATE TABLE users (
  usr_id UUID PRIMARY KEY,
  usr_username VARCHAR NOT NULL,
  usr_password VARCHAR NOT NULL
);

CREATE TABLE securities (
  sec_id UUID PRIMARY KEY,
  sec_name VARCHAR NOT NULL
);

CREATE TABLE orders (
  ord_id UUID PRIMARY KEY,
  ord_usr_id UUID NOT NULL,
  ord_sec_id UUID NOT NULL,
  ord_type VARCHAR NOT NULL,
  ord_price DECIMAL NOT NULL,
  ord_quantity LONG NOT NULL,
  ord_fulfilled BOOLEAN NOT NULL DEFAULT false
);

CREATE TABLE trades (
  trd_id UUID PRIMARY KEY,
  trd_ord_sell_id UUID NOT NULL,
  trd_ord_buy_id UUID NOT NULL,
  trd_price DECIMAL NOT NULL,
  trd_quantity LONG NOT NULL
);
