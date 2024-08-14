package com.example.javatraineetechtask.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class MemberDto {
    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate membershipDate;

    private List<BookDto> borrowedBooks = new ArrayList<>();
}
