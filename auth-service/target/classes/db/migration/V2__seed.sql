INSERT INTO users (username, password, email, first_name, last_name, role)
VALUES
('admin', '$2a$10$zM8aQqJ6FJ7V1C7TQ2k7JOCrj0gqG1I9e9mWcEo9d3qKQk5m8l1We', 'admin@example.com', 'Admin', 'User', 'ADMIN'),
('user',  '$2a$10$8n5QeZs8JxqTgQ8bq2oWqe1JmCjv8iU3V6mJcL5eS2Z9c3QG3nW6e', 'user@example.com',  'Regular', 'User', 'USER');
-- bcrypt for: admin123 / user123
