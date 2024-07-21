package ru.aston.lepd.readingclub.util;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.aston.lepd.readingclub.dto.AuthorDto;
import ru.aston.lepd.readingclub.dto.BookDto;
import ru.aston.lepd.readingclub.dto.ReaderDto;
import ru.aston.lepd.readingclub.entity.Author;
import ru.aston.lepd.readingclub.entity.Book;
import ru.aston.lepd.readingclub.entity.Reader;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface CustomMapper {


    CustomMapper INSTANCE = Mappers.getMapper(CustomMapper.class);


    ReaderDto readerToReaderDto(Reader reader);
    Reader readerDtoToReader(ReaderDto readerDto);

    AuthorDto authorToAuthorDto(Author author);
    Author authorDtoToAuthor(AuthorDto authorDto);


    @Mapping(source = "authors", target = "authorIds")
    @Mapping(source = "reader", target = "readerId")
    BookDto bookToBookDto(Book book);

    default List<Long> mapAuthors(List<Author> authors) {
        return authors.stream()
                .map(Author::getId)
                .toList();
    }

    default Long mapReader(Reader reader) {
        return reader.getId();
    }



    @Mapping(source = "authorIds", target = "authors")
    @Mapping(source = "readerId", target = "reader")
    Book bookDtoToBook(BookDto bookDto);

    default List<Author> mapAuthorIds(List<Long> authorIds) {
        List<Author> authors = new ArrayList<>();
        if (authorIds != null) {
            authors = authorIds.stream()
                    .map(id -> {
                        Author author = new Author();
                        author.setId(id);
                        return author;
                    }).toList();
        }
        return authors;
    }

    default Reader mapReaderId(Long readerId) {
        Reader reader = null;
        if (readerId != null) {
            reader = new Reader();
            reader.setId(readerId);
        }
        return reader;
    }






}
