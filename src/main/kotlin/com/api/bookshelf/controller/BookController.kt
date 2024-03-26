package com.api.bookshelf.controller

import com.api.bookshelf.dto.request.AddBookDto
import com.api.bookshelf.dto.request.DeleteBookDto
import com.api.bookshelf.dto.request.UpdateBookDto
import com.api.bookshelf.dto.response.BookListDto
import com.api.bookshelf.service.BookService
import jakarta.validation.constraints.NotBlank
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService) {

    @GetMapping("/")
    fun bookList(): BookListDto {
        return bookService.findBooksByNotDeleted()
    }

    @GetMapping("/{keyword}")
    fun bookListByKeyword(@PathVariable @Validated @NotBlank(message = "キーワードの入力は必須です") keyword: String): BookListDto {
        return bookService.findBooksByKeyword(keyword)
    }

    @GetMapping("/{authorId}")
    fun bookListByAuthor(@PathVariable authorId: Int): BookListDto {
        return bookService.findBooksByAuthorIdAndNotDeleted(authorId)
    }

    @PostMapping("/add")
    fun addBook(
        @RequestBody @Validated addBookDto: AddBookDto, bindingResult: BindingResult
    ): String {
        if (bindingResult.hasErrors()) {
            return bindingResult.allErrors.toString()
        }
        bookService.addBook(addBookDto)

        return "post:$addBookDto"
    }

    @PostMapping("/update")
    fun updateBook(@RequestBody @Validated updateBookDto: UpdateBookDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return bindingResult.allErrors.toString()
        }
        bookService.updateBook(updateBookDto)

        return "post:$updateBookDto"
    }

    @PostMapping("/delete")
    fun deleteBook(@RequestBody @Validated deleteBookDto: DeleteBookDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return bindingResult.allErrors.toString()
        }
        bookService.deleteBook(deleteBookDto)

        return "post:$deleteBookDto"
    }

}