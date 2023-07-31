package ru.berrington.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.berrington.library.models.Book;
import ru.berrington.library.models.Person;


import java.util.List;

@Repository
public interface BooksRepositories extends JpaRepository<Book, Integer> {
    public List<Book> findByOwner(Person person);

    public Book findBookByTitleStartingWith(String sample);
}
