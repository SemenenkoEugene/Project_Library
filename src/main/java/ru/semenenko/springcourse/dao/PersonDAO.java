package ru.semenenko.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.semenenko.springcourse.models.Book;
import ru.semenenko.springcourse.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?", new Object[]{id},
                        new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person(name, year) VALUES(?, ?)",
                person.getName(),
                person.getYear());
    }

    public void update(int id, Person updatedPerson) {
        jdbcTemplate.update("UPDATE person SET name=?, year=?  WHERE id=?",
                updatedPerson.getName(),
                updatedPerson.getYear(),
                id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    //Для валидации уникальности ФИО
    public Optional<Person> getPersonByFullName(String name) {
        return jdbcTemplate.query("SELECT  * FROM person WHERE name=?", new Object[]{name},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    public List<Book> getBooksByPersonId(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE person_id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }
}
