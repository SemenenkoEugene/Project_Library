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
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book show(int bookId) {
        return jdbcTemplate.query("SELECT  * FROM book WHERE id=?", new Object[]{bookId},
                new BeanPropertyRowMapper<>(Book.class))
                .stream().findAny().orElse(null);
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES (?,?,?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void update(int bookId, Book updateBook) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?",
                updateBook.getTitle(), updateBook.getAuthor(), updateBook.getYear(), bookId);
    }

    public void delete(int bookId) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", bookId);
    }

    //Join'им таблицы Book и Person и получаем человека, которому принадлежит книга с указанным id
    public Optional<Person> getBookOwner(int id) {
        //Выбираем все колонки таблицы Person из объединенной таблицы
        return jdbcTemplate.query("SELECT Person.* FROM book JOIN person ON book.person_id = person.id WHERE book.id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
    }

    //Освобождаем книгу (Этот метод вызывается, когда человек возвращает книгу в библиотеку)
    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=NULL WHERE id=?", id);
    }

    //Назначаем книгу человеку (это метод вызывается, когда человек забирает книгу из библиотеки)
    public void assign(int id, Person selectedPerson) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE id=?", selectedPerson.getId(), id);
    }
}
