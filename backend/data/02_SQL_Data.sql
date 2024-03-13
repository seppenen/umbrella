
\connect spring_db;

DROP TABLE IF EXISTS users;

CREATE TABLE users (
           id BIGSERIAL PRIMARY KEY,
           name VARCHAR(255),
           email VARCHAR(255),
           phone VARCHAR(255),
           address VARCHAR(255)
);

INSERT INTO users (name, email, phone, address) VALUES
                    ('Matti Meikäläinen', 'matti.meikäläinen@email.fi', '0401234567', 'Keskuskatu 1, 00100 Helsinki'),
                    ('Liisa Laaksonen', 'liisa.laaksonen@email.fi', '0407654321', 'Piippukatu 2, 00580 Helsinki'),
                    ('Olli Oksanen', 'olli.oksanen@email.fi', '0502345678', 'Satamakatu 3, 33210 Tampere'),
                    ('Aino Aaltonen', 'aino.aaltonen@email.fi', '0508765432', 'Rantakatu 4, 90100 Oulu'),
                    ('Eero Erkinen', 'eero.erkinen@email.fi', '0603456789', 'Kirkkokatu 5, 15140 Lahti')
ON CONFLICT DO NOTHING;
