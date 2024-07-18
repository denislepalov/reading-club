package ru.aston.lepd.readingclub.dao;

import ru.aston.lepd.readingclub.exception.DaoException;
import ru.aston.lepd.readingclub.util.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AuthorBookDao {




    public static final String INSERT_SQL = """
            INSERT INTO author_book (author_id, book_id) VALUES (?, ?)
            """;

    public static final String DELETE_SQL = """
            DELETE FROM author_book WHERE author_id = ? AND book_id = ?
            """;

    public static final String DELETE_ALL_BY_AUTHOR_ID_SQL = """
            DELETE FROM author_book WHERE author_id = ?
            """;

    public static final String DELETE_ALL_BY_BOOK_ID_SQL = """
            DELETE FROM author_book WHERE book_id = ?
            """;




    public boolean save(Long authorId, Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setLong(1, authorId);
            preparedStatement.setLong(2, bookId);
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public boolean deleteAllByAuthorId(Long authorId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_BY_AUTHOR_ID_SQL)) {

            preparedStatement.setLong(1, authorId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public boolean deleteAllByBookId(Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_BY_BOOK_ID_SQL)) {

            preparedStatement.setLong(1, bookId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }


    public boolean delete(Long authorId, Long bookId) {
        try (Connection connection = DataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setLong(1, authorId);
            preparedStatement.setLong(2, bookId);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }





}
