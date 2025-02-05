package ru.aston.lepd.readingclub.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookTest {



    @Test
    void addBook_shouldAddBook() {
        final Reader reader = new Reader();
        reader.setId(1L);
        reader.setName("Ivan");
        reader.setSurname("Ivanov");
        reader.setPhone("71111111111");
        reader.setAddress("Lenina 11");

        final Book book = new Book();
        book.setId(1L);
        book.setTitle("Title1");
        book.setInventoryNumber(11111L);
        book.setReader(reader);

        final Author author = new Author();
        author.setId(1L);
        author.setFullName("Author1");
        author.setPersonalInfo("likes dogs");

        book.addAuthor(author);

        assertEquals(1, book.getAuthors().size());
        assertEquals(1, author.getBooks().size());
    }
}