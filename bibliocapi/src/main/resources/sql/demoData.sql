INSERT INTO book (isbn, title, author, type) VALUES ('978-2267011258', 'Le Seigneur des anneaux', 'J.R.R. Tolkien', 'Roman Fantastique'), ('978-2845999145', 'Fairy Tail - Tome 1', 'Hiro Mashima', 'Shonen'), ('978-2845999459', 'Fairy Tail - Tome 2', 'Hiro Mashima', 'Shonen'), ('978-2080703248', 'Ruy Blas', 'Victor Hugo', 'Théatre'), ('978-2800109558', 'Gaston, Tome 14 : La Saga des gaffes ', 'André FRANQUIN', 'BD');

INSERT INTO library (lib_name) VALUES ('Bib_A'),('Bib_B');

INSERT INTO copy (book_book_id, location_id_library) VALUES (1,1),(2,1),(2,2),(3,1),(3,2),(5,1),(5,2),(5,2);

INSERT INTO member (member_number, first_name, last_name, email, password) VALUES ('2020020801', 'xxxx', 'xxxx', 'xxxx@xxxx.com', '$2a$10$F4ZTJ/zie.KtQjvmq.FZLeaVzbaVmzXCf1qZZQlX1UPvBBwXz1f.C'), ('2020020802', 'xxxxy', 'xxxx', 'xxxx@xxxx.com', '$2a$10$F4ZTJ/zie.KtQjvmq.FZLeaVzbaVmzXCf1qZZQlX1UPvBBwXz1f.C'), ('2020020803', 'xxxxy', 'xxxx', 'xxxx@xxxx.com', '$2a$10$F4ZTJ/zie.KtQjvmq.FZLeaVzbaVmzXCf1qZZQlX1UPvBBwXz1f.C'), ('2020020804', 'xxxxy', 'xxxx', 'xxxx@xxxx.com', '$2a$10$F4ZTJ/zie.KtQjvmq.FZLeaVzbaVmzXCf1qZZQlX1UPvBBwXz1f.C');

INSERT INTO loan (loan_date, extended_loan, copy_copy_id, member_member_id) VALUES ('2020-01-11 10:02:26','false',1,1),('2021-02-05 16:45:26','false',2,1),('2020-01-19 17:45:26','true',3,2),('2019-12-12 08:45:26','true',5,3);
