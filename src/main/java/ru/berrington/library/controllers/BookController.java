package ru.berrington.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.berrington.library.models.Book;
import ru.berrington.library.models.Person;
import ru.berrington.library.services.BooksService;
import ru.berrington.library.services.PeopleService;


import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page",required = false) String pages,
                        @RequestParam(value = "books_per_page",required = false) String booksPerPage,
                        @RequestParam(value = "sort_by_year",required = false) boolean sortByYear){
        model.addAttribute("books", booksService.findAll(pages, booksPerPage, sortByYear));

        return "books/index";
    }

    @GetMapping("/search")
    public String google(@RequestParam(value = "sample", required = false) String sample, Model model){
        if(sample != null && sample.length() > 0){
            Book book = booksService.findBookLike(sample);
            if(book == null) return "books/search";
            model.addAttribute("book",book );
            int book_id= book.getBook_id();
            Person bookOwner = booksService.findOwnerById(book_id);
            if(bookOwner != null){
                model.addAttribute("bookOwner", bookOwner);
            } else{
                model.addAttribute("bookOwner", null);
            }
        } else {
            model.addAttribute("start", "start");
        }
        return "books/search";
    }

    @GetMapping("/{book_id}")
    public String show(@PathVariable("book_id") int book_id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(book_id));
        Person bookOwner = booksService.findOwnerById(book_id);
        if(bookOwner != null){
            model.addAttribute("bookOwner", bookOwner);
        } else{
            model.addAttribute("people",peopleService.findAll());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{book_id}/edit")
    public String edit(Model model, @PathVariable("book_id") int book_id){
        model.addAttribute("book", booksService.findById(book_id));
        return "books/edit";
    }

    @PatchMapping("{book_id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("book_id") int book_id){
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.update(book_id,book);
        return "redirect:/books";
    }

    @DeleteMapping("/{book_id}")
    public String delete(@PathVariable("book_id") int book_id){
        booksService.delete(book_id);
        return "redirect:/books";
    }

    @PatchMapping("/{book_id}/assign")
    public String assign(@PathVariable("book_id") int book_id, @ModelAttribute("person") Person selectedPerson){
        booksService.assign(book_id,selectedPerson);
        return "redirect:/books/" + book_id;
    }

    @PatchMapping("/{book_id}/release")
    public String release(@PathVariable("book_id") int book_id){
        booksService.release(book_id);
        return "redirect:/books/" + book_id;
    }

}
