package com.example.javatraineetechtask.mapper;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.entity.Book;
import org.springframework.stereotype.Service;

@Service
public class BookDtoMapper extends DtoMapperFacade<Book, BookDto> {

    public BookDtoMapper() {
        super(Book.class, BookDto.class);
    }

    @Override
    protected void decorateDto(BookDto dto, Book book) {
        dto.setAmount(book.getAmount());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
    }
}
