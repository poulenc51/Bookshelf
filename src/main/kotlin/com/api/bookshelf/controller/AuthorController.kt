package com.api.bookshelf.controller

import com.api.bookshelf.dto.request.AddAuthorDto
import com.api.bookshelf.dto.request.DeleteAuthorDto
import com.api.bookshelf.dto.request.UpdateAuthorDto
import com.api.bookshelf.dto.response.AuthorListDto
import com.api.bookshelf.service.AuthorService
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
@RequestMapping("/author")
class AuthorController(private val authorService: AuthorService) {

    @GetMapping("/")
    fun authorList(): AuthorListDto {
        return authorService.findAuthorsByNotDeleted()
    }

    @GetMapping("/keyword={keyword}")
    fun authorListByKeyword(@PathVariable("keyword") @Validated @NotBlank(message = "キーワードの入力は必須です") keyword: String): AuthorListDto {
        return authorService.findAuthorByKeyword(keyword)
    }

    @PostMapping("/add")
    fun addAuthor(
        @RequestBody @Validated addAuthorDto: AddAuthorDto, bindingResult: BindingResult
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage }.joinToString(", ")
            return ResponseEntity.badRequest().body(errors)
        }
        authorService.addAuthor(addAuthorDto)

        return ResponseEntity.ok("post:$addAuthorDto")
    }

    @PostMapping("/update")
    fun updateAuthor(@RequestBody @Validated updateAuthorDto: UpdateAuthorDto, bindingResult: BindingResult): ResponseEntity<Any> {
        if (authorService.findAuthorById(updateAuthorDto.id) == null) {
            bindingResult.addError(FieldError("addBookDto", "authorId", "存在しない著者IDです"))
        }
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage }.joinToString(", ")
            return ResponseEntity.badRequest().body(errors)
        }
        authorService.updateBook(updateAuthorDto)

        return ResponseEntity.ok("post:$updateAuthorDto")
    }

    @PostMapping("/delete")
    fun deleteBook(@RequestBody @Validated deleteAuthorDto: DeleteAuthorDto, bindingResult: BindingResult): ResponseEntity<Any> {
        if (authorService.findAuthorById(deleteAuthorDto.id) == null) {
            bindingResult.addError(FieldError("addBookDto", "authorId", "存在しない著者IDです"))
        }
        if (bindingResult.hasErrors()) {
            val errors = bindingResult.allErrors.map { it.defaultMessage }.joinToString(", ")
            return ResponseEntity.badRequest().body(errors)
        }
        authorService.deleteAuthor(deleteAuthorDto)

        return ResponseEntity.ok("post:$deleteAuthorDto")
    }

}