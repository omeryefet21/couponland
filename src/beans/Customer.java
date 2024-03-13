package beans;

import exceptions.CLInputException;
import local.CouponlandSystem;

import java.util.List;
import java.util.Objects;

public class Customer {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Coupon> coupons;

    public Customer(Integer id, String firstName, String lastName, String email, String password, List<Coupon> coupons) throws CLInputException {
        this(firstName, lastName, email, password, coupons);
        setId(id);
    }

    public Customer(String firstName, String lastName, String email, String password, List<Coupon> coupons) throws CLInputException {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(email);
        setPassword(password);
        setCoupons(coupons);
        this.id=-1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws CLInputException {

        this.id = CouponlandSystem.getInstance().getInputValidation().validateId(id);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws CLInputException {
        this.firstName = CouponlandSystem.getInstance().getInputValidation().validateFirstName(firstName);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws CLInputException {
        this.lastName = CouponlandSystem.getInstance().getInputValidation().validateLastName(lastName);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws CLInputException {
        this.email = CouponlandSystem.getInstance().getInputValidation().validateEmail(email);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws CLInputException {
        this.password = CouponlandSystem.getInstance().getInputValidation().validatePassword(password);
    }

    public List<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<Coupon> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=\n" + coupons +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(firstName, customer.firstName) && Objects.equals(lastName, customer.lastName) && Objects.equals(email, customer.email) && Objects.equals(password, customer.password) && Objects.equals(coupons, customer.coupons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, password, coupons);
    }
}


