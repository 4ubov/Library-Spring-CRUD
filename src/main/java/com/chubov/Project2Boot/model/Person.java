package com.chubov.Project2Boot.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int person_id;

    @NotEmpty(message = "Поле ФИО, не может быть пустым")
    @Size(min = 2, max = 60, message = "Длина ФИО, должна быть в пределах [2:60]")
    @Column(name = "full_name")
    private String fullName;

    @NotEmpty(message = "Поле ФИО, не может быть пустым")
    @Pattern(regexp = ("^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$"),
            message = "Используйте верный формат даты - (dd.mm.yyyy)")
    @Column(name = "year_of_birth")
    private String yearOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {
    }

    public Person(int person_id, String fullName, String yearOfBirth) {
        this.person_id = person_id;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

    public int getPersonId() {
        return person_id;
    }

    public void setPersonId(int person_id) {
        this.person_id = person_id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(Book book) {
        if (books.isEmpty()) {
            this.books = Collections.singletonList(book);
        } else {
            books.add(book);
        }
    }
}
