package model;

import java.util.Objects;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;

    public Customer() {
    }

    public Customer(String firstName, String lastName, String email) {
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Customer email is not valid!");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    private boolean isEmailValid(String email) {
        final String emailRegex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9]+\\.com$";
        return email.matches(emailRegex);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
