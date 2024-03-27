package com.api.bookshelf.controller

import com.api.bookshelf.dto.request.AddBookDto
import com.api.bookshelf.dto.request.DeleteBookDto
import com.api.bookshelf.dto.request.UpdateBookDto
import com.api.bookshelf.dto.response.BookListDto
import com.api.bookshelf.service.AuthorService
import com.api.bookshelf.service.BookService
import jakarta.validation.constraints.NotBlank
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book")
class BookController(private val bookService: BookService, private val authorService: AuthorService) {

    @GetMapping("/")
    fun bookList(): BookListDto {
        return bookService.findBooksByNotDeleted()
    }

    @GetMapping("/keyword={keyword}")
    fun bookListByKeyword(@PathVariable("keyword") @Validated @NotBlank(message = "キーワードの入力は必須です") keyword: String): BookListDto {
        return bookService.findBooksByKeyword(keyword)
    }

    @GetMapping("/{authorId}")
    fun bookListByAuthor(@PathVariable authorId: Int): BookListDto {
        return bookService.findBooksByAuthorIdAndNotDeleted(authorId)
    }

    @PostMapping("/add")
    fun addBook(
        @RequestBody @Validated addBookDto: AddBookDto, bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (authorService.findAuthorById(addBookDto.authorId) == null) {
            bindingResult.addError(FieldError("addBookDto", "authorId", "存在しない著者IDです"))
        }
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage }.joinToString(", ")
            return ResponseEntity.badRequest().body(errors)
        }
        bookService.addBook(addBookDto)

        return ResponseEntity.ok("post:$addBookDto")
    }

    @PostMapping("/update")
    fun updateBook(
        @RequestBody @Validated updateBookDto: UpdateBookDto,
        bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bookService.findBookById(updateBookDto.id) == null) {
            bindingResult.addError(FieldError("updateBookDto", "id", "存在しない書籍IDです"))
        }
        if (authorService.findAuthorById(updateBookDto.authorId)!!.equals(null)) {
            bindingResult.addError(FieldError("updateBookDto", "authorId", "存在しない著者IDです"))
        }
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage }.joinToString(", ")
            return ResponseEntity.badRequest().body(errors)
        }
        bookService.updateBook(updateBookDto)

        return ResponseEntity.ok("post:$updateBookDto")
    }

    @PostMapping("/delete")
    fun deleteBook(
        @RequestBody @Validated deleteBookDto: DeleteBookDto,
        bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bookService.findBookById(deleteBookDto.id) == null) {
            bindingResult.addError(FieldError("deleteBookDto", "id", "存在しない書籍IDです"))
        }
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage }.joinToString(", ")
            return ResponseEntity.badRequest().body(errors)
        }
        bookService.deleteBook(deleteBookDto)

        return ResponseEntity.ok("post:$deleteBookDto")
    }

}