-- Catalogo inicial de Let's Read (21 libros)
-- INSERT IGNORE: si el ISBN ya existe, se omite y no se duplica.

SET NAMES utf8mb4;
USE lets_read_db;

INSERT IGNORE INTO libro
  (isbn, titulo, autor, anio_publicacion, categoria, editorial,
   numero_paginas, precio_venta, porcentaje_iva, cantidad_inventario, formato)
VALUES
('978-0134685991', 'Cien años de soledad', 'Gabriel García M.', 1967, 'Novela', 'Sudamericana', 496, 50000, 19.0, 10, 'Físico'),
('978-0132350884', 'Clean Code', 'Robert C. Martin', 2008, 'Tecnología', 'Prentice Hall', 464, 120000, 19.0, 5, 'Digital'),
('978-0547928227', 'El Hobbit', 'J.R.R. Tolkien', 1937, 'Fantasía', 'Minotauro', 310, 45000, 19.0, 8, 'Físico'),
('978-0140449136', 'La Odisea', 'Homero', -800, 'Épica', 'Penguin Classics', 560, 40000, 19.0, 7, 'Digital'),
('978-0307277671', '1984', 'George Orwell', 1949, 'Distopía', 'Secker & Warburg', 328, 35000, 19.0, 12, 'Físico'),
('978-0061120084', 'Matar a un ruiseñor', 'Harper Lee', 1960, 'Novela', 'J.B. Lippincott & Co.', 281, 38000, 19.0, 6, 'Digital'),
('978-0140449266', 'La Ilíada', 'Homero', -750, 'Épica', 'Penguin Classics', 704, 42000, 19.0, 9, 'Físico'),
('978-0307387899', 'El código Da Vinci', 'Dan Brown', 2003, 'Thriller', 'Doubleday', 489, 55000, 19.0, 15, 'Digital'),
('978-0140449181', 'La Eneida', 'Virgilio', -19, 'Épica', 'Penguin Classics', 432, 39000, 19.0, 4, 'Físico'),
('978-0307474278', 'El nombre de la rosa', 'Umberto Eco', 1980, 'Misterio', 'Harcourt', 512, 60000, 19.0, 11, 'Digital'),
('978-0140449273', 'Metamorfosis', 'Franz Kafka', 1915, 'Novela corta', 'Penguin Classics', 201, 30000, 19.0, 5, 'Físico'),
('978-0307474279', 'El retrato de Dorian Gray', 'Oscar Wilde', 1890, 'Novela', 'Harcourt', 254, 32000, 19.0, 8, 'Digital'),
('978-0140449280', 'Fausto', 'Johann Wolfgang von Goethe', 1808, 'Tragedia', 'Penguin Classics', 158, 28000, 19.0, 3, 'Físico'),
('978-0307474280', 'Drácula', 'Bram Stoker', 1897, 'Horror', 'Harcourt', 418, 36000, 19.0, 10, 'Digital'),
('978-0140449297', 'El proceso', 'Franz Kafka', 1925, 'Novela', 'Penguin Classics', 255, 31000, 19.0, 6, 'Físico'),
('978-0307474281', 'Frankenstein', 'Mary Shelley', 1818, 'Horror', 'Harcourt', 280, 34000, 19.0, 7, 'Digital'),
('978-0140449303', 'El extranjero', 'Albert Camus', 1942, 'Novela', 'Penguin Classics', 123, 29000, 19.0, 4, 'Físico'),
('978-0307474282', 'El gran Gatsby', 'F. Scott Fitzgerald', 1925, 'Novela', 'Harcourt', 180, 33000, 19.0, 9, 'Digital'),
('978-0140449310', 'Crimen y castigo', 'Fiódor Dostoyevski', 1866, 'Novela', 'Penguin Classics', 430, 37000, 19.0, 5, 'Físico'),
('978-0307474283', 'Anna Karenina', 'León Tolstói', 1877, 'Novela', 'Harcourt', 864, 40000, 19.0, 12, 'Digital'),
('978-0140449327', 'Madame Bovary', 'Gustave Flaubert', 1856, 'Novela', 'Penguin Classics', 329, 35000, 19.0, 6, 'Físico');