package com.example.javatraineetechtask.service;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.entity.Book;
import com.example.javatraineetechtask.entity.Member;
import com.example.javatraineetechtask.mapper.BookDtoMapper;
import com.example.javatraineetechtask.repository.BookRepository;
import com.example.javatraineetechtask.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final BookDtoMapper bookDtoMapper;

    @Override
    public BookDto saveBook(BookDto bookDto) {
        Book book = bookDtoMapper.convertToEntity(bookDto);
        Book savedBook = bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor())
                .map(existingBook -> {
                    existingBook.setAmount(existingBook.getAmount() + 1);
                    return existingBook;
                }).orElseGet(() -> bookRepository.save(book));

        return bookDtoMapper.convertToDto(savedBook);
    }

    @Override
    public void deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Remove the book from all members
        for (Member member : book.getMembers()) {
            member.getBorrowedBooks().remove(book);
            memberRepository.save(member);
        }

        // Delete the book
        bookRepository.delete(book);
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setAmount(bookDto.getAmount());

        Book updatedBook = bookRepository.save(book);
        return bookDtoMapper.convertToDto(updatedBook);
    }

    @Override
    public Optional<BookDto> getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(bookDtoMapper::convertToDto);
    }
}
