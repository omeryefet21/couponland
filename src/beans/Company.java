package beans;

import exceptions.CLInputException;
import local.CouponlandSystem;
import java.util.List;
import java.util.Objects;

public class Company {
    private Integer id;
    private String name;
    private String email;
    private String password;
    private List<Coupon> coupons;

    public Company(Integer id, String name, String email, String password, List<Coupon> coupons) throws CLInputException {
        this(name, email, password, coupons);
        setId(id);
    }

    public Company(String name, String email, String password, List<Coupon> coupons) throws CLInputException {
        setName(name);
        setEmail(email);
        setPassword(password);
        setCoupons(coupons);
        this.id = -1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws CLInputException {
        this.id = CouponlandSystem.getInstance().getInputValidation().validateId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws CLInputException {
        this.name = CouponlandSystem.getInstance().getInputValidation().validateCompanyName(name);
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
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", coupons=\n" + coupons +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(name, company.name) && Objects.equals(email, company.email) && Objects.equals(password, company.password) && Objects.equals(coupons, company.coupons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, coupons);
    }
}
