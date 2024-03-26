package com.api.bookshelf.controller

import com.api.bookshelf.dto.request.AddAuthorDto
import com.api.bookshelf.dto.request.DeleteAuthorDto
import com.api.bookshelf.dto.request.UpdateAuthorDto
import com.api.bookshelf.dto.response.AuthorListDto
import com.api.bookshelf.service.AuthorService
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
    ): String {
        if (bindingResult.hasErrors()) {
            return bindingResult.allErrors.toString()
        }
        authorService.addAuthor(addAuthorDto)

        return "post:$addAuthorDto"
    }

    @PostMapping("/update")
    fun updateAuthor(@RequestBody @Validated updateAuthorDto: UpdateAuthorDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return bindingResult.allErrors.toString()
        }
        authorService.updateBook(updateAuthorDto)

        return "post:$updateAuthorDto"
    }

    @PostMapping("/delete")
    fun deleteBook(@RequestBody @Validated deleteAuthorDto: DeleteAuthorDto, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) {
            return bindingResult.allErrors.toString()
        }
        authorService.deleteAuthor(deleteAuthorDto)

        return "post:$deleteAuthorDto"
    }

}