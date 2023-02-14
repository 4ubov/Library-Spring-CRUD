package com.chubov.Project2Boot.repositories;


import com.chubov.Project2Boot.model.Book;
import com.chubov.Project2Boot.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);

    List<Book> findByTitleStartingWithIgnoreCase(String startString);
}
