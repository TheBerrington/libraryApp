package ru.berrington.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.berrington.library.models.Book;
import ru.berrington.library.models.Person;
import ru.berrington.library.repositories.PeopleRepositories;


import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    public final PeopleRepositories peopleRepositories;

    @Autowired
    public PeopleService(PeopleRepositories peopleRepositories) {
        this.peopleRepositories = peopleRepositories;
    }
    @Transactional
    public void save(Person person){
        peopleRepositories.save(person);
    }
    @Transactional
    public void update(int id, Person updatingPerson){
        updatingPerson.setId(id);
        peopleRepositories.save(updatingPerson);
    }
    @Transactional
    public void delete(int id){
        peopleRepositories.deleteById(id);
    }

    public Optional<Person> findPersonByName(String name){
        return peopleRepositories.findPersonByName(name);
    }

    public Person findById(int id){
        return peopleRepositories.findById(id).orElse(null);
    }
    public List<Person> findAll(){
        return peopleRepositories.findAll();
    }
    public List<Book> findBooksByPersonId(int id){
        return peopleRepositories.findById(id).orElse(null).getBooks();
    }



}
