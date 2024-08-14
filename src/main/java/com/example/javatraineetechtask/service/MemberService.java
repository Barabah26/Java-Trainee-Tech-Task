package com.example.javatraineetechtask.service;

import com.example.javatraineetechtask.dto.BookDto;
import com.example.javatraineetechtask.dto.MemberDto;
import com.example.javatraineetechtask.entity.Book;
import com.example.javatraineetechtask.entity.Member;

import java.util.List;

public interface MemberService {
    MemberDto saveMember(MemberDto member);

    void deleteMember(Long id);

    List<Member> getAllMembers();

    void borrowBook(Long memberId, Long bookId);

    void returnBook(Long memberId, Long bookId);
}
