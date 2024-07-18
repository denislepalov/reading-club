package ru.aston.lepd.readingclub.service;

import ru.aston.lepd.readingclub.dao.ReaderDao;
import ru.aston.lepd.readingclub.dto.ReaderDto;
import ru.aston.lepd.readingclub.entity.Reader;
import ru.aston.lepd.readingclub.exception.NotFoundException;
import ru.aston.lepd.readingclub.util.CustomMapper;

import java.util.List;
import java.util.Optional;

public class ReaderService {

    private final ReaderDao readerDao;
    private final CustomMapper mapper;
    private static final String NOT_FOUND = "There is no reader with id=%d in database";

    public ReaderService(ReaderDao readerDao, CustomMapper mapper) {
        this.readerDao = readerDao;
        this.mapper = mapper;
    }




    public ReaderDto getById(Long readerId) {
        Optional<Reader> readerOptional = readerDao.findById(readerId);
        // Lazy loading of books
        readerOptional.ifPresent(reader -> reader.setBooks(readerDao.getBooksForReader(readerId)));
        return readerOptional.map(mapper::readerToReaderDto)
                .orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, readerId)));
    }



    public Reader getReaderById(Long readerId) {
        Optional<Reader> readerOptional = readerDao.findById(readerId);
        readerOptional.ifPresent(reader -> reader.setBooks(readerDao.getBooksForReader(readerId)));
        return readerOptional.orElseThrow(() -> new NotFoundException(String.format(NOT_FOUND, readerId)));
    }



    public List<ReaderDto> getAll() {
        List<Reader> readers = readerDao.findAll();
        readers.forEach(reader -> reader.setBooks(readerDao.getBooksForReader(reader.getId())));
        return readers.stream()
                .map(mapper::readerToReaderDto)
                .toList();
    }



    public ReaderDto save(ReaderDto readerDto) {
        Reader reader = mapper.readerDtoToReader(readerDto);
        Reader savedReader = readerDao.save(reader);
        return mapper.readerToReaderDto(savedReader);
    }



    public boolean update(ReaderDto readerDto, Long readerId) {
        Reader updating = getReaderById(readerId);
        Optional.ofNullable(readerDto.getName()).ifPresent(updating::setName);
        Optional.ofNullable(readerDto.getSurname()).ifPresent(updating::setSurname);
        Optional.ofNullable(readerDto.getPhone()).ifPresent(updating::setPhone);
        Optional.ofNullable(readerDto.getAddress()).ifPresent(updating::setAddress);
        return readerDao.update(updating);
    }



    public boolean delete(Long readerId) {
        isContainById(readerId);
        return readerDao.delete(readerId);
    }



    public boolean isContainById(Long readerId) {
        boolean result = readerDao.isContainById(readerId);
        if (!result) throw new NotFoundException(String.format(NOT_FOUND, readerId));
        return true;
    }


}































