package ru.aston.lepd.readingclub.dao;

import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AuthorDao implements Dao<Long, Author> {


    private BookDao bookDao;
    private final AuthorBookDao authorBookDao;


    public AuthorDao(AuthorBookDao authorBookDao) {
        this.authorBookDao = authorBookDao;
    }

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }



    public static final String FIND_BY_ID_SQL = """
            SELECT id, full_name, personal_info
            FROM authors
            WHERE id = ?
            """;

    public static final String FIND_ALL_SQL = """
            SELECT id, full_name, personal_info
            FROM authors
            """;

    public static final String FIND_ALL_BY_BOOK_ID_SQL = """
            SELECT a.id, a.full_name, a.personal_info FROM authors a
            JOIN author_book ab ON a.id = ab.author_id
            WHERE ab.book_id = ?
            """;


    public static final String INSERT_SQL = """
            INSERT INTO authors (full_name, personal_info) VALUES (?, ?)
            """;

    public static final String UPDATE_SQL = """
            UPDATE authors
            SET full_name = ?, personal_info = ?
            WHERE id = ?
            """;

    public static final String DELETE_SQL = """
            DELETE FROM authors WHERE id = ?
            """;






    @Override
    public Optional<Author> findById(Long authorId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Author author = null;
            if (resultSet.next()) {
                author = buildAuthor(resultSet);
            }
            return Optional.ofNullable(author);

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public List<Author> findAll() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                authors.add(buildAuthor(resultSet));
            }
            return authors;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public List<Author> findAllByBookId(Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_BOOK_ID_SQL)) {

            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                authors.add(buildAuthor(resultSet));
            }
            return authors;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    private Author buildAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getLong("id"));
        author.setFullName(resultSet.getString("full_name"));
        author.setPersonalInfo(resultSet.getString("personal_info"));
        // Lazy loading of books
        author.setBooks(new ArrayList<>());
        return author;
    }

    public List<Book> getBooksForAuthor(Long authorId) {
        return bookDao.findAllByAuthorId(authorId);
    }


    public Author save(Author author) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, author.getFullName());
            preparedStatement.setString(2, author.getPersonalInfo());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                author.setId(generatedKeys.getLong("id"));
                for (Book book : author.getBooks()) {
                    authorBookDao.save(author.getId(), book.getId());
                }
            }
            return author;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public boolean update(Author author) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, author.getFullName());
            preparedStatement.setString(2, author.getPersonalInfo());
            preparedStatement.setLong(3, author.getId());
            int updateResult = preparedStatement.executeUpdate();
            if (updateResult > 0) {
                authorBookDao.deleteAllByAuthorId(author.getId());
                for (Book book : author.getBooks()) {
                    authorBookDao.save(author.getId(), book.getId());
                }
            }
            return updateResult > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public boolean delete(Long authorId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, authorId);
            authorBookDao.deleteAllByAuthorId(authorId);
            int deleteResult = preparedStatement.executeUpdate();
            return deleteResult > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public boolean isContainById(Long authorId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }





}
