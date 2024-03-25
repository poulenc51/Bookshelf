package com.api.bookshelf.controller

import com.api.bookshelf.dto.response.BookListDto
import com.api.bookshelf.service.BookService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService) {

    @GetMapping("/")
    fun bookList(): BookListDto {
        return bookService.findBooksByNotDelete();
    }
}