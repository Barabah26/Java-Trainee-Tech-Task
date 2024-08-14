package com.example.javatraineetechtask.service;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.entity.Book;
import com.example.javatraineetechtask.mapper.BookDtoMapper;
import com.example.javatraineetechtask.repository.BookRepository;
import com.example.javatraineetechtask.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookDtoMapper bookDtoMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveBook_CreatesNewBook() {
        BookDto bookDto = new BookDto("Title", "Author", 5);
        Book book = new Book(null, "Title", "Author", 5, new ArrayList<>());
        Book savedBook = new Book(1L, "Title", "Author", 5, new ArrayList<>());

        when(bookDtoMapper.convertToEntity(bookDto)).thenReturn(book);
        when(bookRepository.findByTitleAndAuthor(book.getTitle(), book.getAuthor())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookDtoMapper.convertToDto(savedBook)).thenReturn(bookDto);

        BookDto result = bookService.saveBook(bookDto);

        assertNotNull(result);
        assertEquals("Title", result.getTitle());
        assertEquals("Author", result.getAuthor());
        assertEquals(5, result.getAmount());

        verify(bookRepository).save(book);
    }
    @Test
    void deleteBook_RemovesBook() {
        Book book = new Book(1L, "Title", "Author", 5, new ArrayList<>());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(memberRepository.save(any())).thenReturn(new Book());

        bookService.deleteBook(1L);

        verify(bookRepository).delete(book);
    }

    @Test
    void deleteBook_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> bookService.deleteBook(1L));
        assertEquals("Book not found", exception.getMessage());
    }

    @Test
    void updateBook_UpdatesExistingBook() {
        BookDto bookDto = new BookDto("New Title", "New Author", 10);
        Book existingBook = new Book(1L, "Old Title", "Old Author", 5, new ArrayList<>());
        Book updatedBook = new Book(1L, "New Title", "New Author", 10, new ArrayList<>());

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(updatedBook)).thenReturn(updatedBook);
        when(bookDtoMapper.convertToDto(updatedBook)).thenReturn(bookDto);

        BookDto result = bookService.updateBook(1L, bookDto);

        assertEquals("New Title", result.getTitle());
        assertEquals("New Author", result.getAuthor());
        assertEquals(10, result.getAmount());

        verify(bookRepository).save(updatedBook);
    }

    @Test
    void getBookById_ReturnsBook() {
        Book book = new Book(1L, "Title", "Author", 5, new ArrayList<>());
        BookDto bookDto = new BookDto("Title", "Author", 5);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookDtoMapper.convertToDto(book)).thenReturn(bookDto);

        Optional<BookDto> result = bookService.getBookById(1L);

        assertTrue(result.isPresent());
        assertEquals("Title", result.get().getTitle());

        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_BookNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<BookDto> result = bookService.getBookById(1L);

        assertFalse(result.isPresent());
    }
}
