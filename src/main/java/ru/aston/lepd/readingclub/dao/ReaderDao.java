package ru.aston.lepd.readingclub.dao;

import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReaderDao implements Dao<Long, Reader> {


    private BookDao bookDao;

    public void setBookDao(BookDao bookDao) {
        this.bookDao = bookDao;
    }

    public BookDao getBookDao() {
        return bookDao;
    }



    public static final String FIND_BY_ID_SQL = """
            SELECT id, name, surname, phone, address
            FROM readers
            WHERE id = ?
            """;

    public static final String FIND_ALL_SQL = """
            SELECT id, name, surname, phone, address
            FROM readers
            """;

    public static final String INSERT_SQL = """
            INSERT INTO readers (name, surname, phone, address) VALUES (?, ?, ?, ?)
            """;

    public static final String UPDATE_SQL = """
            UPDATE readers
            SET name = ?, surname = ?, phone = ?, address = ?
            WHERE id = ?
            """;

    public static final String DELETE_SQL = """
            DELETE FROM readers WHERE id = ?
            """;





    @Override
    public Optional<Reader> findById(Long readerId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Reader reader = null;
            if (resultSet.next()) {
                reader = buildReader(resultSet);
            }
            return Optional.ofNullable(reader);

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public List<Reader> findAll() {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<Reader> readers = new ArrayList<>();
            while (resultSet.next()) {
                readers.add(buildReader(resultSet));
            }
            return readers;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    private Reader buildReader(ResultSet resultSet) throws SQLException {
        Reader reader = new Reader();
        reader.setId(resultSet.getLong("id"));
        reader.setName(resultSet.getString("name"));
        reader.setSurname(resultSet.getString("surname"));
        reader.setPhone(resultSet.getString("phone"));
        reader.setAddress(resultSet.getString("address"));
        // Lazy loading of books
        reader.setBooks(new ArrayList<>());
        return reader;
    }

    public List<Book> getBooksForReader(Long readerId) {
        return bookDao.findAllByReaderId(readerId);
    }


    @Override
    public Reader save(Reader reader) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, reader.getName());
            preparedStatement.setString(2, reader.getSurname());
            preparedStatement.setString(3, reader.getPhone());
            preparedStatement.setString(4, reader.getAddress());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                reader.setId(generatedKeys.getLong("id"));
                for (Book book : reader.getBooks()) {
                    book.setReader(reader);
                    bookDao.save(book);
                }
            }
            return reader;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public boolean update(Reader reader) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, reader.getName());
            preparedStatement.setString(2, reader.getSurname());
            preparedStatement.setString(3, reader.getPhone());
            preparedStatement.setString(4, reader.getAddress());
            preparedStatement.setLong(5, reader.getId());
            int updateResult = preparedStatement.executeUpdate();
            if (updateResult > 0) {
                for (Book book : reader.getBooks()) {
                    book.setReader(reader);
                    if (book.getId() != null) {
                        bookDao.update(book);
                    } else {
                        bookDao.save(book);
                    }
                }
            }
            return updateResult > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    @Override
    public boolean delete(Long readerId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, readerId);
            bookDao.deleteAllByReaderId(readerId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public boolean isContainById(Long readerId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            preparedStatement.setLong(1, readerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }



}