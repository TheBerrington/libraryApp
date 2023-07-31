package ru.berrington.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berrington.library.models.Book;
import ru.berrington.library.models.Person;
import ru.berrington.library.repositories.BooksRepositories;
import ru.berrington.library.repositories.PeopleRepositories;


import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class BooksService {
    public final BooksRepositories booksRepositories;
    public final PeopleRepositories peopleRepositories;

    @Autowired
    public BooksService(BooksRepositories booksRepositories, PeopleRepositories peopleRepositories) {
        this.booksRepositories = booksRepositories;
        this.peopleRepositories = peopleRepositories;
    }

    @Transactional
    public void save(Book book){
        booksRepositories.save(book);
    }
    @Transactional
    public void update(int id, Book updatingBook){
        updatingBook.setBook_id(id);
        booksRepositories.save(updatingBook);
    }
    @Transactional
    public void delete(int id){
        booksRepositories.deleteById(id);
    }

    public List<Book> findAll(String pages, String booksPerPage, boolean sortByYear){
        if(pages==null || booksPerPage == null) {
            if(sortByYear){
                return booksRepositories.findAll(Sort.by("year"));
            }
            return booksRepositories.findAll();
        }
        if(sortByYear){
            return booksRepositories.findAll(PageRequest.of((Integer.parseInt(pages)),(Integer.parseInt(booksPerPage)), Sort.by("year"))).getContent();
        }
        return booksRepositories.findAll(PageRequest.of((Integer.parseInt(pages)),(Integer.parseInt(booksPerPage)))).getContent();

    }
    public Book findById(int book_id){
        return booksRepositories.findById(book_id).orElse(null);
    }
    public Person findOwnerById(int book_id){
        return booksRepositories.findById(book_id).orElse(null).getOwner();
    }

    @Transactional
    public void assign(int book_id, Person person){
        Book book = booksRepositories.findById(book_id).orElse(null);
        book.setAssignAt(new Date());
        book.setOwner(person);
        booksRepositories.save(book);
    }
    @Transactional
    public void release(int book_id){

        Book book = booksRepositories.findById(book_id).orElse(null);
        book.setOwner(null);
        book.setAssignAt(null);
        booksRepositories.save(book);
    }

    public List<Book> findBooksByPersonId(int id){
        Person owner = peopleRepositories.findById(id).orElse(null);
        return booksRepositories.findByOwner(owner);
    }

    public Book findBookLike(String sample){
        return booksRepositories.findBookByTitleStartingWith(sample);
    }

}
