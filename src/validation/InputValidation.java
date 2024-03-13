package validation;

import beans.Category;
import exceptions.CLInputException;
import utilities.GeneralUtils;

import java.time.LocalDate;
import java.util.function.Predicate;
import java.util.regex.Matcher;

public class InputValidation {

    private final Constants constants;
    public final Predicate<Integer> VALID_ID;
    public final Predicate<Integer> VALID_AMOUNT;
    public final Predicate<Double> VALID_PRICE;
    public final Predicate<LocalDate> VALID_START_DATE;
    public final Predicate<LocalDate> VALID_END_DATE;

    public InputValidation(Constants constants) {
        this.constants = constants;
        VALID_ID = id -> id >= constants.ID_MIN_VALUE && id <= constants.ID_MAX_VALUE;
        VALID_AMOUNT = amount -> amount >= constants.AMOUNT_MIN_VALUE && amount <= constants.AMOUNT_MAX_VALUE;
        VALID_PRICE = price -> price >= constants.PRICE_MIN_VALUE && price <= constants.PRICE_MAX_VALUE;
        VALID_START_DATE = startDate -> (startDate.equals(constants.COUPON_START_DATE_MIN_VALUE) | startDate.isAfter(constants.COUPON_START_DATE_MIN_VALUE)) &&
                (startDate.equals(constants.COUPON_START_DATE_MAX_VALUE) | startDate.isBefore(constants.COUPON_START_DATE_MAX_VALUE));
        VALID_END_DATE = endDate -> (endDate.equals(constants.COUPON_END_DATE_MIN_VALUE) | endDate.isAfter(constants.COUPON_END_DATE_MIN_VALUE)) &&
                (endDate.equals(constants.COUPON_END_DATE_MAX_VALUE) | endDate.isBefore(constants.COUPON_END_DATE_MAX_VALUE));
    }


    public Integer validateId(Integer id) throws CLInputException {
        nullCheck(id);
        if (!VALID_ID.test(id)) {
            throw new CLInputException("Invalid id value - not in range", 701);
        }
        return id;
    }

    public Integer validateAmount(Integer amount) throws CLInputException {
        nullCheck(amount);
        if (!VALID_AMOUNT.test(amount)) {
            throw new CLInputException("Invalid amount value - not in range", 711);
        }
        return amount;
    }

    public Double validatePrice(Double price) throws CLInputException {
        nullCheck(price);
        if (!VALID_PRICE.test(price)) {
            throw new CLInputException("Invalid price value - not in range", 712);
        }
        return price;
    }

    public String validateEmail(String email) throws CLInputException {
        nullCheck(email);
        Matcher matcher = constants.EMAIL_PATTERN.matcher(email);
        if (!matcher.matches()) {
            throw new CLInputException("Invalid email length and/or pattern", 702);
        }
        return email;
    }

    public String validateCompanyName(String companyName) throws CLInputException {
        nullCheck(companyName);
        Matcher matcher = constants.COMPANY_NAME_PATTERN.matcher(GeneralUtils.removeSpaces(companyName));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid company name length and/or pattern", 703);
        }
        return GeneralUtils.removeSpaces(companyName);
    }

    public String validateFirstName(String firstName) throws CLInputException {
        nullCheck(firstName);
        Matcher matcher = constants.FIRST_NAME_PATTERN.matcher(GeneralUtils.removeSpaces(firstName));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid first name length and/or pattern", 704);
        }
        return GeneralUtils.removeSpaces(firstName);
    }

    public String validateLastName(String lastName) throws CLInputException {
        nullCheck(lastName);
        Matcher matcher = constants.LAST_NAME_PATTERN.matcher(GeneralUtils.removeSpaces(lastName));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid last name length and/or pattern", 705);
        }
        return GeneralUtils.removeSpaces(lastName);
    }

    public String validatePassword(String password) throws CLInputException {
        nullCheck(password);
        Matcher matcher = constants.PASSWORD_PATTERN.matcher(password);
        if (!matcher.matches()) {
            throw new CLInputException("Invalid password length and/or pattern", 706);
        }
        return password;
    }

    public String validateCouponTitle(String couponTitle) throws CLInputException {
        nullCheck(couponTitle);
        Matcher matcher = constants.COUPON_TITLE_PATTERN.matcher(GeneralUtils.removeSpaces(couponTitle));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid coupon title length and/or pattern", 707);
        }
        return GeneralUtils.removeSpaces(couponTitle);
    }

    public String validateCouponDescription(String couponDescription) throws CLInputException {
        if (couponDescription==null) {
            return constants.DEFAULT_COUPON_DESCRIPTION;
        }
        Matcher matcher = constants.COUPON_DESCRIPTION_PATTERN.matcher(GeneralUtils.removeSpaces(couponDescription));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid coupon description length and/or pattern", 708);
        }
        return GeneralUtils.removeSpaces(couponDescription);
    }

    public String validateCouponImage(String couponImage) throws CLInputException {
        if (couponImage==null) {
            return constants.DEFAULT_COUPON_IMAGE;
        }
        Matcher matcher = constants.COUPON_IMAGE_PATTERN.matcher(GeneralUtils.removeSpaces(couponImage));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid coupon image(path) length and/or pattern", 709);
        }
        return GeneralUtils.removeSpaces(couponImage);
    }

    public String validateCategoryName(String categoryName) throws CLInputException {
        nullCheck(categoryName);
        Matcher matcher = constants.CATEGORY_NAME_PATTERN.matcher(GeneralUtils.removeSpaces(categoryName));
        if (!matcher.matches()) {
            throw new CLInputException("Invalid category name length and/or pattern", 710);
        }
        return GeneralUtils.removeSpaces(categoryName);
    }

    public LocalDate validateStartDate(LocalDate startDate) throws CLInputException {
        nullCheck(startDate);
        if (!VALID_START_DATE.test(startDate)) {
            throw new CLInputException("Invalid start date value", 721);
        } else return startDate;
    }

    public LocalDate validateEndDate(LocalDate endDate) throws CLInputException {
        nullCheck(endDate);
        if (!VALID_END_DATE.test(endDate)) {
            throw new CLInputException("Invalid end date value", 722);
        } else return endDate;
    }


    public Category validateCategory(Category category) throws CLInputException {
        nullCheck(category);
        if (GeneralUtils.getCategoryId(category)==-1) {
            throw new CLInputException("Category does not exist on database", 735);
        }
        return category;
    }

    public void nullCheck(Object object) throws CLInputException {
        if (object == null) {
            throw new CLInputException("UnNullable parameter", 899);
        }
    }


}
