package com.example.javatraineetechtask.mapper;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.dto.MemberDto;
import com.example.javatraineetechtask.entity.Book;
import com.example.javatraineetechtask.entity.Member;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberDtoMapper extends DtoMapperFacade<Member, MemberDto> {

    private final BookDtoMapper bookDtoMapper; // Inject BookDtoMapper to convert books

    public MemberDtoMapper(BookDtoMapper bookDtoMapper) {
        super(Member.class, MemberDto.class);
        this.bookDtoMapper = bookDtoMapper;
    }

    @Override
    protected void decorateDto(MemberDto dto, Member member) {
        dto.setName(member.getName());
        dto.setMembershipDate(member.getMembershipDate());

        List<BookDto> borrowedBooks = member.getBorrowedBooks().stream()
                .map(bookDtoMapper::convertToDto)
                .collect(Collectors.toList());

        dto.setBorrowedBooks(borrowedBooks);
    }
}

