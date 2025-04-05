package com.staymaster.models;

import java.sql.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String firstName;
    
    private String lastName;

    private Date dateOfBirth;
    
    private String phoneNo;
    
    private String email;

    private String password;
    
    private String address;
    
    private String gender;
    private String userType; 

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    @OneToMany(mappedBy = "user")
    private List<Review> reviews;
    
    
    
    

	public User() {
		super();
	}

	public User(Long userId, String firstName, String lastName, Date dateOfBirth, String phoneNo, String email,
			String password, String address, String gender, String userType) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNo = phoneNo;
		this.email = email;
		this.password = password;
		this.address = address;
		this.gender = gender;
		this.userType = userType;
	}

	public User(Long userId, String firstName, String lastName, Date dateOfBirth, String phoneNo, String email,
			String password, String address, List<Booking> bookings, List<Review> reviews, String gender) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.phoneNo = phoneNo;
		this.email = email;
		this.password = password;
		this.address = address;
		this.bookings = bookings;
		this.reviews = reviews;
		this.gender = gender;
		this.userType = userType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
	
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	
	

    // Getters and setters
    
}
