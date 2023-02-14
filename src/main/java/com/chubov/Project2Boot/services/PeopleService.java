package com.chubov.Project2Boot.services;


import com.chubov.Project2Boot.model.Book;
import com.chubov.Project2Boot.model.Person;
import com.chubov.Project2Boot.repositories.BooksRepository;
import com.chubov.Project2Boot.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;
    private final BooksRepository booksRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, BooksRepository booksRepository) {
        this.peopleRepository = peopleRepository;
        this.booksRepository = booksRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findOne(int person_id) {
        Optional<Person> foundedPerson = peopleRepository.findById(person_id);
        return foundedPerson.orElse(null);
    }

    public Optional<Person> findOne(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int person_id, Person updatedPerson) {
        updatedPerson.setPersonId(person_id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int person_id) {
        Person person = peopleRepository.getOne(person_id);
        List<Book> books = booksRepository.findByOwner(person);
        for (Book book : books) {
            book.setReceivingDate(null);
        }
        peopleRepository.deleteById(person_id);

    }

    @Transactional
    public List<Book> findByOwner(int person_id) {
        return booksRepository.findByOwner(peopleRepository.findById(person_id).get());

    }
}
