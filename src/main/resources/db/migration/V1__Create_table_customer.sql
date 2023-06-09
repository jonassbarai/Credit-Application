CREATE TABLE customer_table (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name VARCHAR(255) NOT NULL,
   cpf VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   zip_code VARCHAR(255),
   street VARCHAR(255),
   CONSTRAINT pk_customer_table PRIMARY KEY (id)
);

ALTER TABLE customer_table ADD CONSTRAINT uc_customer_table_cpf UNIQUE (cpf);

ALTER TABLE customer_table ADD CONSTRAINT uc_customer_table_email UNIQUE (email);