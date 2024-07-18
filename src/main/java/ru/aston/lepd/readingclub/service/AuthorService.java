package ru.aston.lepd.readingclub.service;

import ru.aston.lepd.readingclub.dao.AuthorDao;
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.util.CustomMapper;

import java.util.List;
import java.util.Optional;

public class AuthorService {

    private final AuthorDao authorDao;
    private final CustomMapper mapper;
    private static final String NOT_FOUND = "There is no author with id=%d in database";


    public AuthorService(AuthorDao authorDao, CustomMapper mapper) {
        this.authorDao = authorDao;
        this.mapper = mapper;
    }




    public AuthorDto getById(Long authorId) {
        Optional<Author> authorOptional = authorDao.findById(authorId);
        // Lazy loading of books
        authorOptional.ifPresent(author -> author.setBooks(authorDao.getBooksForAuthor(authorId)));
        return authorOptional.map(mapper::authorToAuthorDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, authorId)));
    }



    public Author getAuthorById(Long authorId) {
        Optional<Author> authorOptional = authorDao.findById(authorId);
        authorOptional.ifPresent(author -> author.setBooks(authorDao.getBooksForAuthor(authorId)));
        return authorOptional.orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, authorId)));
    }



    public List<AuthorDto> getAll() {
        List<Author> authors = authorDao.findAll();
        authors.forEach(author -> author.setBooks(authorDao.getBooksForAuthor(author.getId())));
        return authors.stream()
                .map(mapper::authorToAuthorDto)
                .toList();
    }



    public AuthorDto save(AuthorDto authorDto) {
        Author author = mapper.authorDtoToAuthor(authorDto);
        Author savedAuthor = authorDao.save(author);
        return mapper.authorToAuthorDto(savedAuthor);
    }



    public boolean update(AuthorDto authorDto, Long authorId) {
        Author updating = getAuthorById(authorId);
        Optional.ofNullable(authorDto.getFullName()).ifPresent(updating::setFullName);
        Optional.ofNullable(authorDto.getPersonalInfo()).ifPresent(updating::setPersonalInfo);
        return authorDao.update(updating);
    }



    public boolean delete(Long authorId) {
        isContainById(authorId);
        return authorDao.delete(authorId);
    }



    public boolean isContainById(Long authorId) {
        boolean result = authorDao.isContainById(authorId);
        if (!result) throw new NotFoundException(String.format(NOT_FOUND, authorId));
        return true;
    }


}
