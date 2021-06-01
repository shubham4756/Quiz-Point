package com.team.quizpoint.model;

import com.team.quizpoint.model.token.SecureToken;
import org.bson.codecs.pojo.annotations.BsonId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.*;

@Document(collection = "users")
public class User {

    @MongoId(FieldType.OBJECT_ID)
    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String gender;
    private String contactNumber;
    private String city;
    private String password;

    private boolean active;

    private List<String> roles;
    private List<String> createdQuizzes;
    private List<AttendedQuiz> attendedQuizzes;

    public User() {
    }

    public User(String firstname, String lastname, String email, String gender, String contactNumber, String city, String password, boolean active, List<String> roles, List<String> createdQuizzes, List<AttendedQuiz> attendedQuizzes) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.city = city;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.createdQuizzes = createdQuizzes;
        this.attendedQuizzes = attendedQuizzes;
    }

    public User(String userId, String firstname, String lastname, String email, String gender, String contactNumber, String city, String password, boolean active, List<String> roles, List<String> createdQuizzes, List<AttendedQuiz> attendedQuizzes) {
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.city = city;
        this.password = password;
        this.active = active;
        this.roles = roles;
        this.createdQuizzes = createdQuizzes;
        this.attendedQuizzes = attendedQuizzes;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getCreatedQuizzes() {
        return createdQuizzes;
    }

    public void setCreatedQuizzes(List<String> createdQuizzes) {
        this.createdQuizzes = createdQuizzes;
    }

    public List<AttendedQuiz> getAttendedQuizzes() {
        return attendedQuizzes;
    }

    public void setAttendedQuizzes(List<AttendedQuiz> attendedQuizzes) {
        this.attendedQuizzes = attendedQuizzes;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                ", city='" + city + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", roles=" + roles +
                ", createdQuizzes=" + createdQuizzes +
                ", attendedQuizzes=" + attendedQuizzes +
                '}';
    }
}
