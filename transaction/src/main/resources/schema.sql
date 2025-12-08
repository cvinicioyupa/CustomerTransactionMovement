-- Crear tabla Account
CREATE TABLE IF NOT EXISTS account (
    number INT PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    initial_balance DOUBLE NOT NULL,
    status BOOLEAN NOT NULL,
    client_identification INT NOT NULL
);

-- Crear tabla Movement
CREATE TABLE IF NOT EXISTS movement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_number INT NOT NULL,
    date TIMESTAMP,
    type VARCHAR(50) NOT NULL,
    amount DOUBLE NOT NULL,
    balance DOUBLE NOT NULL,
    status BOOLEAN NOT NULL,
    FOREIGN KEY (account_number) REFERENCES account(number)
);
