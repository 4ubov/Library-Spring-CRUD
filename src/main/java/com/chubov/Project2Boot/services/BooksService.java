package com.chubov.Project2Boot.services;


import com.chubov.Project2Boot.model.Book;
import com.chubov.Project2Boot.model.Person;
import com.chubov.Project2Boot.repositories.BooksRepository;
import com.chubov.Project2Boot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public BooksService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    @Transactional
    public Page<Book> findAll(Pageable var1) {
        return booksRepository.findAll(var1);
    }

    @Transactional
    public List<Book> findAll(Sort var1) {
        return booksRepository.findAll(var1);
    }


    public Book findOne(int book_id) {
        Optional<Book> foundedBook = booksRepository.findById(book_id);
        return foundedBook.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int book_id, Book updatedBook) {
        updatedBook.setBookId(book_id);
        updatedBook.setOwner(booksRepository.findById(book_id).get().getOwner());
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int book_id) {
        booksRepository.deleteById(book_id);
    }


    public Optional<Person> getBookOwner(int book_id) {
        return Optional.ofNullable(booksRepository.getReferenceById(book_id).getOwner());
    }

    @Transactional
    public void release(int book_id) {
        Book book = booksRepository.findById(book_id).get();
        book.setOwner(null);
        book.setReceivingDate(null);
//        jdbcTemplate.update("Update book set person_id=NULL where book_id=?", book_id);
    }

    @Transactional
    public void assign(int book_id, Person selectedPerson) {
        Book book = booksRepository.findById(book_id).get();
        selectedPerson = peopleRepository.findById(selectedPerson.getPersonId()).get();
        book.setOwner(selectedPerson);
        selectedPerson.setBooks(book);
        book.setReceivingDate(new Date());
    }

    public List<Book> searchByTitle(String searchQuery) {
        return booksRepository.findByTitleStartingWithIgnoreCase(searchQuery);
    }
}
