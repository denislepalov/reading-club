package ru.aston.lepd.readingclub.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Reader implements Cloneable{

    private Long id;
    private String name;
    private String surname;
    private String phone;
    private String address;
    private List<Book> books = new ArrayList<>();


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        books.add(book);
        book.setReader(this);
    }

//    @Override
//    public String toString() {
//        return "Reader{" +
//               "id=" + id +
//               ", name='" + name + '\'' +
//               ", surname='" + surname + '\'' +
//               ", phone='" + phone + '\'' +
//               ", address='" + address + '\'' +
//               '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Reader reader = (Reader) o;
//        return Objects.equals(id, reader.id) && Objects.equals(name, reader.name) && Objects.equals(surname, reader.surname) && Objects.equals(phone, reader.phone) && Objects.equals(address, reader.address);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, name, surname, phone, address);
//    }

    @Override
    public Reader clone() {
        try {
            return (Reader) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
