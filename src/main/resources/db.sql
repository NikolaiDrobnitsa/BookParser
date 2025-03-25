
CREATE TABLE IF NOT EXISTS book_categories (
                                               id INT AUTO_INCREMENT PRIMARY KEY,
                                               name VARCHAR(255) NOT NULL,
    url VARCHAR(500) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS books (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(500) NOT NULL,
    price VARCHAR(50) NOT NULL,
    image_url VARCHAR(500),
    rating VARCHAR(50),
    availability VARCHAR(100),
    category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES book_categories(id)
    );

CREATE TABLE IF NOT EXISTS parsing_stats (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             category_name VARCHAR(255) NOT NULL,
    total_books INT NOT NULL,
    avg_price DECIMAL(10, 2),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS currency_rates (
                                              id INT AUTO_INCREMENT PRIMARY KEY,
                                              currency_name VARCHAR(50) NOT NULL,
    buy_rate DECIMAL(10, 4) NOT NULL,
    sell_rate DECIMAL(10, 4) NOT NULL,
    fetch_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

INSERT INTO currency_rates (currency_name, buy_rate, sell_rate)
VALUES ('USD', 38.1500, 38.6500);

INSERT INTO currency_rates (currency_name, buy_rate, sell_rate)
VALUES ('EUR', 40.2500, 41.2500);