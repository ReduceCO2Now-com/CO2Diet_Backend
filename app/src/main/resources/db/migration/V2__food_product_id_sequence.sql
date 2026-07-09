CREATE SEQUENCE food_product_id_seq;
ALTER TABLE food_product ALTER COLUMN id SET DEFAULT nextval('food_product_id_seq');
ALTER SEQUENCE food_product_id_seq OWNED BY food_product.id;