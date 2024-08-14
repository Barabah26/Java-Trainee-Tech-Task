package com.example.javatraineetechtask.service;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.dto.MemberDto;
import com.example.javatraineetechtask.entity.Book;
import com.example.javatraineetechtask.entity.Member;
import com.example.javatraineetechtask.mapper.BookDtoMapper;
import com.example.javatraineetechtask.mapper.MemberDtoMapper;
import com.example.javatraineetechtask.repository.BookRepository;
import com.example.javatraineetechtask.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BookService bookService;
    private final MemberDtoMapper memberDtoMapper;
    private final BookDtoMapper bookDtoMapper;
    private final BookRepository bookRepository;

    @Override
    public MemberDto saveMember(MemberDto memberDto) {
        memberRepository.findByName(memberDto.getName()).ifPresent(member -> {
            throw new RuntimeException("User already exists");
        });

        Member member = memberDtoMapper.convertToEntity(memberDto);
        member.setMembershipDate(LocalDate.now());

        Member savedMember = memberRepository.save(member);
        return memberDtoMapper.convertToDto(savedMember);
    }

    @Override
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (!member.getBorrowedBooks().isEmpty()) {
            throw new RuntimeException("Member cannot be deleted while books are borrowed");
        }
        memberRepository.delete(member);
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public void borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member with ID " + memberId + " not found"));

        BookDto bookDto = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book with ID " + bookId + " not found"));

        if (bookDto.getAmount() <= 0) {
            throw new RuntimeException("Book with ID " + bookId + " is not available");
        }
        if (member.canBorrowMoreBooks()) {
            bookDto.setAmount(bookDto.getAmount() - 1);
            Book existingBook = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("Book with ID " + bookId + " not found"));

            existingBook.setAmount(bookDto.getAmount());
            bookRepository.save(existingBook);

            member.getBorrowedBooks().add(existingBook);
            memberRepository.save(member);
        } else {
            throw new RuntimeException("Member with ID " + memberId + " has reached the borrowing limit");
        }
    }


    @Override
    public void returnBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member with ID " + memberId + " not found"));
        BookDto bookDto = bookService.getBookById(bookId)
                .orElseThrow(() -> new RuntimeException("Book with ID " + bookId + " not found"));

        Book book = bookDtoMapper.convertToEntity(bookDto);

        boolean bookRemoved = member.getBorrowedBooks().removeIf(b -> b.getId().equals(bookId));

        if (bookRemoved) {
            bookDto.setAmount(bookDto.getAmount() + 1);
            bookService.updateBook(bookId, bookDto);
        } else {
            throw new RuntimeException("Book with ID " + bookId + " was not borrowed by member with ID " + memberId);
        }

        memberRepository.save(member);
    }

}
