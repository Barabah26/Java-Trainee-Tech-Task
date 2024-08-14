package com.example.javatraineetechtask.controller;

import com.example.javatraineetechtask.dto.MemberDto;
import com.example.javatraineetechtask.entity.Member;
import com.example.javatraineetechtask.service.MemberService;
import com.example.javatraineetechtask.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "Create a new member")
    @PostMapping
    public ResponseEntity<MemberDto> createMember(@RequestBody @Valid MemberDto member) {
        MemberDto createdMember = memberService.saveMember(member);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMember);
    }

    @Operation(summary = "Get all members")
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @Operation(summary = "Borrow book by id")
    @PutMapping("/{id}/borrow/{bookId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long id, @PathVariable Long bookId) {
        try {
            memberService.borrowBook(id, bookId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Member or Book not found");
        }
    }

    @Operation(summary = "Return book by id")
    @PutMapping("/{id}/return/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long id, @PathVariable Long bookId) {
        try {
            memberService.returnBook(id, bookId);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Member or Book not found");
        }
    }

    @Operation(summary = "Delete member by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Member not found");
        }
    }
}
