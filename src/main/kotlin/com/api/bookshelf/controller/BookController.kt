package com.api.bookshelf.controller

import com.api.bookshelf.dto.request.AddBookDto
import com.api.bookshelf.dto.response.BookListDto
import com.api.bookshelf.service.BookService
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService) {

    @GetMapping("/")
    fun bookList(): BookListDto {
        return bookService.findBooksByNotDelete();
    }

    @PostMapping("/add")
    fun addBook(@RequestBody @Validated addBookDto: AddBookDto, bindingResult: BindingResult
    ): String {
        if(bindingResult.hasErrors()){
            return bindingResult.allErrors.toString()
        }
        bookService.addBook(addBookDto)

        return "post:$addBookDto"
    }
}