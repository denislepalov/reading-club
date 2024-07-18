package ru.aston.lepd.readingclub.dao;

import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDao implements Dao<Long, Book> {


    private final AuthorBookDao authorBookDao;
    private AuthorDao authorDao;
    private ReaderDao readerDao;


    public BookDao(AuthorBookDao authorBookDao) {
        this.authorBookDao = authorBookDao;
    }

    public void setAuthorDao(AuthorDao authorDao) {
        this.authorDao = authorDao;
    }

    public void setReaderDao(ReaderDao readerDao) {
        this.readerDao = readerDao;
    }

    public AuthorDao getAuthorDao() {
        return authorDao;
    }

    public ReaderDao getReaderDao() {
        return readerDao;
    }

    public static final String FIND_BY_ID_SQL = """
            SELECT id, title, inventory_number, reader_id
            FROM books
            WHERE id = ?
            """;

    public static final String FIND_ALL_SQL = """
            SELECT id, title, inventory_number, reader_id
            FROM books
            """;

    public static final String FIND_ALL_BY_READER_ID_SQL = """
            SELECT id, title, inventory_number, reader_id
            FROM books
            WHERE reader_id = ?
            """;

    public static final String FIND_ALL_BY_AUTHOR_ID_SQL = """
            SELECT b.id, b.title, b.inventory_number, b.reader_id FROM books b
            JOIN author_book ab ON b.id = ab.book_id
            WHERE ab.author_id = ?
            """;

    public static final String INSERT_SQL = """
            INSERT INTO books (title, inventory_number, reader_id) VALUES (?, ?, ?)
            """;

    public static final String UPDATE_SQL = """
            UPDATE books
            SET title = ?, inventory_number = ?, reader_id = ?
            WHERE id = ?
            """;

    public static final String DELETE_SQL = """
            DELETE FROM books WHERE id = ?
            """;

    public static final String DELETE_BY_READER_ID = """
            DELETE FROM books WHERE reader_id = ?
            """;





    @Override
    public Optional<Book> findById(Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Book book = null;
            if (resultSet.next()) {
                book = buildBook(resultSet);
            }
            return Optional.ofNullable(book);

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public List<Book> findAll() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public List<Book> findAllByReaderId(Connection connection, Long readerId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_READER_ID_SQL)) {

            preparedStatement.setLong(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public List<Book> findAllByReaderId(Long readerId) {
        try (Connection connection = DataSource.getConnection()) {
            return findAllByReaderId(connection, readerId);

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public List<Book> findAllByAuthorId(Long authorId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_BY_AUTHOR_ID_SQL)) {

            preparedStatement.setLong(1, authorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> books = new ArrayList<>();
            while (resultSet.next()) {
                books.add(buildBook(resultSet));
            }
            return books;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    private Book buildBook(ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getLong("id"));
        book.setTitle(resultSet.getString("title"));
        book.setInventoryNumber(resultSet.getLong("inventory_number"));
        Optional<Reader> optionalReader = readerDao.findById(resultSet.getLong("reader_id"));
        optionalReader.ifPresent(book::setReader);
        // Lazy loading of authors
        book.setAuthors(new ArrayList<>());
        return book;
    }

    public List<Author> getAuthorsForBook(Long bookId) {
        return authorDao.findAllByBookId(bookId);
    }


    @Override
    public Book save(Book book) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setLong(2, book.getInventoryNumber());
            preparedStatement.setLong(3, book.getReader().getId());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                book.setId(generatedKeys.getLong("id"));
                for (Author author : book.getAuthors()) {
                    authorBookDao.save(author.getId(), book.getId());
                }
            }
            return book;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public boolean update(Book book) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setLong(2, book.getInventoryNumber());
            preparedStatement.setLong(3, book.getReader().getId());
            preparedStatement.setLong(4, book.getId());
            int updateResult = preparedStatement.executeUpdate();
            if (updateResult > 0) {
                authorBookDao.deleteAllByBookId(book.getId());
                for (Author author : book.getAuthors()) {
                    authorBookDao.save(author.getId(), book.getId());
                }
            }
            return updateResult > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }



    @Override
    public boolean delete(Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, bookId);
            authorBookDao.deleteAllByBookId(bookId);
            int deleteResult = preparedStatement.executeUpdate();
            return deleteResult > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    public boolean deleteAllByReaderId(Long readerId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_BY_READER_ID)) {

            preparedStatement.setLong(1, readerId);
            List<Book> books = findAllByReaderId(connection, readerId);
            for (Book book : books) {
                authorBookDao.deleteAllByBookId(book.getId());
            }
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public boolean isContainById(Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


}
