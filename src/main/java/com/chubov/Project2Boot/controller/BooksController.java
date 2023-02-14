package com.chubov.Project2Boot.controller;


import com.chubov.Project2Boot.model.Book;
import com.chubov.Project2Boot.model.Person;
import com.chubov.Project2Boot.services.BooksService;
import com.chubov.Project2Boot.services.PeopleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public BooksController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page", required = false) Integer page,
                        @RequestParam(name = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(name = "sort_by_title", required = false) Boolean sortByTitle) {
        if (sortByTitle != null && (page == null || booksPerPage == null)) {
            model.addAttribute("books", booksService.findAll(Sort.by("title")));
        } else if ((page != null && booksPerPage != null) && sortByTitle == null) {
            model.addAttribute("books", booksService.findAll(PageRequest.of(page, booksPerPage)).getContent());
        } else if ((page != null && booksPerPage != null) && sortByTitle == true) {
            model.addAttribute("books", booksService.findAll(PageRequest.of(
                    page,
                    booksPerPage,
                    Sort.by("title"))
            ).getContent());
        } else {
            model.addAttribute("books", booksService.findAll());
        }
        return "books/index";
    }

    @GetMapping("/{book_id}")
    public String show(@PathVariable("book_id") int book_id,
                       Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(book_id));

        Optional<Person> bookOwner = booksService.getBookOwner(book_id);

        if (bookOwner.isPresent())
            model.addAttribute("owner", bookOwner.get());
        else
            model.addAttribute("people", peopleService.findAll());

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book,
                          Model model) {
        model.addAttribute("book", book);
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("{book_id}/edit")
    public String edit(@PathVariable("book_id") int book_id,
                       Model model) {
        model.addAttribute("book", booksService.findOne(book_id));
        return "books/edit";
    }

    @PatchMapping("/{book_id}")
    public String update(@PathVariable("book_id") int book_id,
                         @ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        booksService.update(book_id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{book_id}")
    public String delete(@PathVariable("book_id") int book_id) {
        booksService.delete(book_id);
        return "redirect:/books";
    }

    @PatchMapping("/{book_id}/release")
    public String release(@PathVariable("book_id") int book_id) {
        booksService.release(book_id);
        return "redirect:/books/" + book_id;
    }

    @PatchMapping("/{book_id}/assign")
    public String assign(@PathVariable("book_id") int book_id, @ModelAttribute("person") Person selectedPerson) {
        booksService.assign(book_id, selectedPerson);
        return "redirect:/books/" + book_id;
    }


    @GetMapping("/search")
    public String searchBook(@RequestParam(required = false, defaultValue = "") String startString,
                             Model model) {
        model.addAttribute("startString", startString);
        if (!startString.isEmpty()) {
            model.addAttribute("books", booksService.searchByTitle(startString));
        }
        return "books/search";
    }
}
