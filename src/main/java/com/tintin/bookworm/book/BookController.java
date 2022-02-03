package com.tintin.bookworm.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
	
	@Autowired
    BookRepository bookRepository;
	
	@GetMapping(value = "/books/{bookId}")
	public Book getBook(@PathVariable String bookId) {
		return bookRepository.findById(bookId).get();
	}
}
