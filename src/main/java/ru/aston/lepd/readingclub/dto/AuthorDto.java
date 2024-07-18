package ru.aston.lepd.readingclub.dto;

public class AuthorDto implements Cloneable {

    private String fullName;
    private String personalInfo;



    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(String personalInfo) {
        this.personalInfo = personalInfo;
    }


    @Override
    public AuthorDto clone() {
        try {
            return (AuthorDto) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
