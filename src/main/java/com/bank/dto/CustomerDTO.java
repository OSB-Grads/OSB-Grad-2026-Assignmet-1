package com.bank.dto;

/**
 * Data Transfer Object for Customer information.
 *
 * <p>Represents a person who holds accounts with the bank, plus the role used
 * for authorisation. Fields follow the assignment spec: first name, last name,
 * date of birth, email, phone, address and a national ID. Email must be unique
 * and the customer must be 18 or over &mdash; enforce those rules in the
 * service layer, not here.</p>
 *
 * <p>The password hash is deliberately NOT part of this DTO &mdash; credentials
 * should never leak into layers that only need profile data.</p>
 */
public class CustomerDTO {
    private Long id;
    private String username;
    private String role;          // CUSTOMER or ADMIN
    private String firstName;
    private String lastName;
    private String dateOfBirth;   // ISO date string, e.g. "1990-05-21"
    private String email;
    private String phone;
    private String address;
    private String nationalId;
    private String createdAt;
    private String updatedAt;

    /** Default constructor. */
    public CustomerDTO() {}

    /** Constructor with all fields. */
    public CustomerDTO(Long id, String firstName, String lastName,
                       String dateOfBirth, String email, String phone, String address,
                       String nationalId, String createdAt, String updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.nationalId = nationalId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
                "id=" + id +'\''+
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", nationalId='" + nationalId + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
