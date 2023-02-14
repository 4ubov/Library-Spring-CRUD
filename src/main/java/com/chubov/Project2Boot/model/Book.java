package com.chubov.Project2Boot.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int book_id;

    @NotEmpty(message = "Поле Название, не может быть пустым")
    @Size(min = 2, max = 60, message = "Длина Названия, должна быть в пределах [2:60]")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Поле Автор, не может быть пустым")
    @Length(min = 2, max = 60, message = "Длина поля Автор, должна быть в пределах [2:60]")
    @Column(name = "author")
    private String author;
    @NotEmpty(message = "Поле Дата выпуска, не может быть пустым")
    @Pattern(regexp = ("^([0-2][0-9]||3[0-1]).(0[0-9]||1[0-2]).([0-9][0-9])?[0-9][0-9]$"),
            message = "Используйте верный формат даты - (dd.mm.yyyy)")
    @Column(name = "realise_date")
    private String realiseDate;

    @Column(name = "receiving_date")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date receivingDate;

    @Transient
    private boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;


    public Book() {
    }

    public Book(int book_id, String title, String author, String realiseDate) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.realiseDate = realiseDate;
    }

    public int getBookId() {
        return book_id;
    }

    public void setBookId(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getRealiseDate() {
        return realiseDate;
    }

    public void setRealiseDate(String realiseDate) {
        this.realiseDate = realiseDate;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getReceivingDate() {
        return receivingDate;
    }

    public void setReceivingDate(Date receivingDate) {
        this.receivingDate = receivingDate;
    }

    public boolean isOverdue() {
        return (new Date().getTime() - receivingDate.getTime()) / 86400000 < -10;
    }

    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
}
