package beans;

import exceptions.CLInputException;
import local.CouponlandSystem;

import java.time.LocalDate;
import java.util.Objects;

public class Coupon {

    private Integer id;
    private Integer companyID;
    private Category category;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer amount;
    private Double price;
    private String image;

    public Coupon(Integer id, Integer companyID, Category category, String title, String description, LocalDate startDate, LocalDate endDate, Integer amount, Double price, String image) throws CLInputException {
       this(companyID, category, title, description, startDate, endDate, amount, price, image);
       setId(id);
    }

    public Coupon(Integer companyID, Category category, String title, String description, LocalDate startDate, LocalDate endDate, Integer amount, Double price, String image) throws CLInputException {
        setCompanyID(companyID);
        setCategory(category);
        setTitle(title);
        setDescription(description);
        setStartDate(startDate);
        setEndDate(endDate);
        setAmount(amount);
        setPrice(price);
        setImage(image);
        this.id=-1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) throws CLInputException {
        this.id = CouponlandSystem.getInstance().getInputValidation().validateId(id);
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) throws CLInputException {
        this.companyID = CouponlandSystem.getInstance().getInputValidation().validateId(companyID);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) throws CLInputException {
        this.category = CouponlandSystem.getInstance().getInputValidation().validateCategory(category);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws CLInputException {
        this.title = CouponlandSystem.getInstance().getInputValidation().validateCouponTitle(title);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) throws CLInputException {
        this.description = CouponlandSystem.getInstance().getInputValidation().validateCouponDescription(description);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) throws CLInputException {
        this.startDate = CouponlandSystem.getInstance().getInputValidation().validateStartDate(startDate);
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) throws CLInputException {
        this.endDate = CouponlandSystem.getInstance().getInputValidation().validateEndDate(endDate);
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) throws CLInputException {
        this.amount = CouponlandSystem.getInstance().getInputValidation().validateAmount(amount);
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) throws CLInputException {
        this.price = CouponlandSystem.getInstance().getInputValidation().validatePrice(price);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) throws CLInputException {
        this.image = CouponlandSystem.getInstance().getInputValidation().validateCouponImage(image);
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + id +
                ", companyID=" + companyID +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate.format(CouponlandSystem.getInstance().getDateFormatter()) +
                ", endDate=" + endDate.format(CouponlandSystem.getInstance().getDateFormatter()) +
                ", amount=" + amount +
                ", price=" + price +
                ", image='" + image + '\'' +
                "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coupon coupon = (Coupon) o;
        return Objects.equals(id, coupon.id) && Objects.equals(companyID, coupon.companyID) && category == coupon.category && Objects.equals(title, coupon.title) && Objects.equals(description, coupon.description) && Objects.equals(startDate, coupon.startDate) && Objects.equals(endDate, coupon.endDate) && Objects.equals(amount, coupon.amount) && Objects.equals(price, coupon.price) && Objects.equals(image, coupon.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyID, category, title, description, startDate, endDate, amount, price, image);
    }
}
