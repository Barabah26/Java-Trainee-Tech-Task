package com.example.javatraineetechtask.service;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    BookDto saveBook(BookDto bookDto);

    void deleteBook(Long id);

    List<Book> getAllBooks();

    BookDto updateBook(Long id, BookDto bookDto);

    Optional<BookDto> getBookById(Long id);
}
