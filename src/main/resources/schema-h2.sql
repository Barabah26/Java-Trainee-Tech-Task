DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS members;
DROP TABLE IF EXISTS member_borrowed_books;

CREATE TABLE books (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      amount INT NOT NULL CHECK (amount >= 0),
                      CONSTRAINT uc_title_author UNIQUE (title, author)
);

CREATE TABLE members (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        name VARCHAR(255) NOT NULL,
                        membership_date DATE NOT NULL
);

CREATE TABLE member_borrowed_books (
                                      member_id BIGINT,
                                      book_id BIGINT,
                                      PRIMARY KEY (member_id, book_id),
                                      FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
                                      FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);
