package ru.aston.lepd.readingclub.service;

import ru.aston.lepd.readingclub.dao.BookDao;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.util.CustomMapper;

import java.util.List;
import java.util.Optional;

public class BookService {


    private final BookDao bookDao;
    private final CustomMapper mapper;
    private static final String NOT_FOUND = "There is no book with id=%d in database";

    public BookService(BookDao bookDao, CustomMapper mapper) {
        this.bookDao = bookDao;
        this.mapper = mapper;
    }





    public BookDto getById(Long bookId) {
        Optional<Book> bookOptional = bookDao.findById(bookId);
        // Lazy loading of authors
        bookOptional.ifPresent(book -> book.setAuthors(bookDao.getAuthorsForBook(bookId)));
        return bookOptional.map(mapper::bookToBookDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, bookId)));
    }



    public Book getBookById(Long bookId) {
        Optional<Book> bookOptional = bookDao.findById(bookId);
        bookOptional.ifPresent(book -> book.setAuthors(bookDao.getAuthorsForBook(bookId)));
        return bookOptional.orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, bookId)));
    }



    public List<BookDto> getAll() {
        List<Book> books = bookDao.findAll();
        books.forEach(book -> book.setAuthors(bookDao.getAuthorsForBook(book.getId())));
        return books.stream()
                .map(mapper::bookToBookDto)
                .toList();
    }



    public List<BookDto> getAllByReaderId(Long readerId) {
        List<Book> books = bookDao.findAllByReaderId(readerId);
        books.forEach(book -> book.setAuthors(bookDao.getAuthorsForBook(book.getId())));
        return books.stream()
                .map(mapper::bookToBookDto)
                .toList();
    }



    public List<BookDto> getAllByAuthorId(Long authorId) {
        List<Book> books = bookDao.findAllByAuthorId(authorId);
        books.forEach(book -> book.setAuthors(bookDao.getAuthorsForBook(book.getId())));
        return books.stream()
                .map(mapper::bookToBookDto)
                .toList();
    }



    public BookDto save(BookDto bookDto) {
        Book book = mapper.bookDtoToBook(bookDto);
        Book savedBook = bookDao.save(book);
        return mapper.bookToBookDto(savedBook);
    }



    public boolean update(BookDto bookDto, Long bookId) {
        Book updating = getBookById(bookId);
        Optional.ofNullable(bookDto.getTitle()).ifPresent(updating::setTitle);
        Optional.ofNullable(bookDto.getInventoryNumber()).ifPresent(updating::setInventoryNumber);

        if (!bookDto.getAuthorIds().isEmpty()) {
            updating.getAuthors().clear();
            bookDto.getAuthorIds()
                    .forEach(authorId -> {
                        Author author = new Author();
                        author.setId(authorId);
                        updating.getAuthors().add(author);
                    });
        }
        if (bookDto.getReaderId() != null) {
            Reader reader = new Reader();
            reader.setId(bookDto.getReaderId());
            updating.setReader(reader);
        }
        return bookDao.update(updating);
    }



    public boolean delete(Long readerId) {
        isContainById(readerId);
        return bookDao.delete(readerId);
    }



    public boolean isContainById(Long bookId) {
        boolean result = bookDao.isContainById(bookId);
        if (!result) throw new NotFoundException(String.format(NOT_FOUND, bookId));
        return true;
    }

}































