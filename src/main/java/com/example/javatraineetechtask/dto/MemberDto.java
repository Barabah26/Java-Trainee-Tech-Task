package com.example.javatraineetechtask.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {
    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate membershipDate;

    private List<BookDto> borrowedBooks = new ArrayList<>();
}
