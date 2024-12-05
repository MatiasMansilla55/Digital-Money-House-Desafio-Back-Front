-- Crear la tabla 'user'
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR NOT NULL,
    last_name  VARCHAR NOT NULL,
    dni INT NOT NULL,
    phone INT NOT NULL, -- Agregar user_id aquí
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    cvu INT NOT NULL,
    alias VARCHAR NOT NULL
    -- Asegúrate de que 'users' existe y la clave foránea esté bien definida
);