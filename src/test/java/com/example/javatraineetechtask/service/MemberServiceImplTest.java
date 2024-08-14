package com.example.javatraineetechtask.service;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.dto.MemberDto;
import com.example.javatraineetechtask.entity.Book;
import com.example.javatraineetechtask.entity.Member;
import com.example.javatraineetechtask.mapper.BookDtoMapper;
import com.example.javatraineetechtask.mapper.MemberDtoMapper;
import com.example.javatraineetechtask.repository.BookRepository;
import com.example.javatraineetechtask.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Transactional
public class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BookService bookService;

    @Mock
    private MemberDtoMapper memberDtoMapper;

    @Mock
    private BookDtoMapper bookDtoMapper;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMember_CreatesNewMember() {
        MemberDto memberDto = new MemberDto("Name", LocalDate.now(), new ArrayList<>());
        Member member = new Member(null, "Name", LocalDate.now(), new HashSet<>());
        Member savedMember = new Member(1L, "Name", LocalDate.now(), new HashSet<>());

        when(memberRepository.findByName(memberDto.getName())).thenReturn(Optional.empty());
        when(memberDtoMapper.convertToEntity(memberDto)).thenReturn(member);
        when(memberRepository.save(member)).thenReturn(savedMember);
        when(memberDtoMapper.convertToDto(savedMember)).thenReturn(memberDto);

        MemberDto result = memberService.saveMember(memberDto);

        assertNotNull(result);
        assertEquals("Name", result.getName());

        verify(memberRepository).save(member);
    }

    @Test
    void saveMember_UserAlreadyExists() {
        MemberDto memberDto = new MemberDto("Name", LocalDate.now(), new ArrayList<>());
        when(memberRepository.findByName(memberDto.getName())).thenReturn(Optional.of(new Member()));

        Exception exception = assertThrows(RuntimeException.class, () -> memberService.saveMember(memberDto));
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    void deleteMember_RemovesMember() {
        Member member = new Member(1L, "Name", LocalDate.now(), new HashSet<>());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        verify(memberRepository).delete(member);
    }

    @Test
    void deleteMember_BooksBorrowed() {
        Member member = new Member(1L, "Name", LocalDate.now(), new HashSet<>());
        member.getBorrowedBooks().add(new Book(1L, "Title", "Author", 5, new ArrayList<>()));

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Exception exception = assertThrows(RuntimeException.class, () -> memberService.deleteMember(1L));
        assertEquals("Member cannot be deleted while books are borrowed", exception.getMessage());
    }

    @Test
    void borrowBook_Success() {
        Member member = new Member(1L, "Name", LocalDate.now(), new HashSet<>());
        BookDto bookDto = new BookDto("Title", "Author", 5);
        Book book = new Book(1L, "Title", "Author", 5, new ArrayList<>());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookDto));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(memberRepository.save(member)).thenReturn(member);

        memberService.borrowBook(1L, 1L);

        assertTrue(member.getBorrowedBooks().contains(book));
        assertEquals(4, book.getAmount());

        verify(bookRepository).save(book);
        verify(memberRepository).save(member);
    }

    @Test
    void borrowBook_NoMoreBooksAllowed() {
        Member member = new Member(1L, "Name", LocalDate.now(), new HashSet<>());
        for (int i = 0; i < 10; i++) {
            member.getBorrowedBooks().add(new Book((long) i, "Title", "Author", 5, new ArrayList<>()));
        }
        BookDto bookDto = new BookDto("Title", "Author", 5);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookDto));

        Exception exception = assertThrows(RuntimeException.class, () -> memberService.borrowBook(1L, 1L));
        assertEquals("Member with ID 1 has reached the borrowing limit", exception.getMessage());
    }


    @Test
    void returnBook_Success() {
        Member member = new Member(1L, "Name", LocalDate.now(), new HashSet<>());
        BookDto bookDto = new BookDto("Title", "Author", 4);
        Book book = new Book(1L, "Title", "Author", 4, new ArrayList<>());

        member.getBorrowedBooks().add(book);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookService.getBookById(1L)).thenReturn(Optional.of(bookDto));
        when(bookDtoMapper.convertToEntity(bookDto)).thenReturn(book);

        when(bookService.updateBook(anyLong(), any(BookDto.class))).thenAnswer(invocation -> {
            BookDto updatedBookDto = invocation.getArgument(1);
            book.setAmount(updatedBookDto.getAmount());
            return null;
        });

        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        memberService.returnBook(1L, 1L);

        assertFalse(member.getBorrowedBooks().contains(book));
        assertEquals(5, book.getAmount());

        verify(bookService).updateBook(1L, bookDto);
        verify(memberRepository).save(member);
    }





    @Test
    void returnBook_BookNotFound() {
        Member member = new Member(1L, "Name", LocalDate.now(), new HashSet<>());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> memberService.returnBook(1L, 1L));
        assertEquals("Book with ID 1 not found", exception.getMessage());
    }
}
