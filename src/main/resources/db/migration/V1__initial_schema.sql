CREATE TABLE users (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 email VARCHAR(190) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 full_name VARCHAR(80) NOT NULL,
 role VARCHAR(20) NOT NULL,
 created_at TIMESTAMP(6) NOT NULL
);
CREATE TABLE categories (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(80) NOT NULL UNIQUE,
 description VARCHAR(500)
);
CREATE TABLE products (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 name VARCHAR(160) NOT NULL,
 description VARCHAR(3000) NOT NULL,
 price DECIMAL(12,2) NOT NULL,
 stock INT NOT NULL,
 image_url VARCHAR(500),
 category_id BIGINT NOT NULL,
 active BOOLEAN NOT NULL,
 created_at TIMESTAMP(6) NOT NULL,
 version BIGINT NOT NULL DEFAULT 0,
 CONSTRAINT fk_products_category FOREIGN KEY(category_id) REFERENCES categories(id),
 INDEX idx_products_category(category_id),
 INDEX idx_products_name(name)
);
CREATE TABLE cart_items (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 product_id BIGINT NOT NULL,
 quantity INT NOT NULL,
 CONSTRAINT fk_cart_user FOREIGN KEY(user_id) REFERENCES users(id),
 CONSTRAINT fk_cart_product FOREIGN KEY(product_id) REFERENCES products(id),
 CONSTRAINT uq_cart_user_product UNIQUE(user_id,product_id)
);
CREATE TABLE customer_orders (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 total DECIMAL(12,2) NOT NULL,
 status VARCHAR(30) NOT NULL,
 stripe_session_id VARCHAR(255),
 idempotency_key VARCHAR(80) NOT NULL UNIQUE,
 created_at TIMESTAMP(6) NOT NULL,
 CONSTRAINT fk_orders_user FOREIGN KEY(user_id) REFERENCES users(id),
 INDEX idx_orders_user_created(user_id,created_at)
);
CREATE TABLE order_items (
 id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 order_id BIGINT NOT NULL,
 product_id BIGINT NOT NULL,
 product_name VARCHAR(160) NOT NULL,
 quantity INT NOT NULL,
 unit_price DECIMAL(12,2) NOT NULL,
 CONSTRAINT fk_items_order FOREIGN KEY(order_id) REFERENCES customer_orders(id)
);
