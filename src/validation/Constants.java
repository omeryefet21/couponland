package validation;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class Constants implements Serializable {

    public final String DEFAULT_SCHEMA_NAME = "couponland";
    public final String DEFAULT_URL = "jdbc:mysql://localhost:3306";
    public final String DEFAULT_SQL_USER = "root";
    public final String DEFAULT_SQL_PASSWORD = "1234";
    public final String DEFAULT_COUPON_DESCRIPTION ="A detailed description will be added soon!";
    public final String DEFAULT_COUPON_IMAGE ="An image will be added soon!";

    public final Integer DB_USERNAME_MIN_LENGTH = 1;
    public final Integer DB_USERNAME_MAX_LENGTH = 32;
    public final Integer DB_PASSWORD_MIN_LENGTH = 4;
    public final Integer DB_PASSWORD_MAX_LENGTH = 32;
    public final Integer DB_URL_MIN_LENGTH = 1;
    public final Integer DB_URL_MAX_LENGTH = Integer.MAX_VALUE;
    public final Integer DB_SCHEMA_NAME_MIN_LENGTH = 1;
    public final Integer DB_SCHEMA_NAME_MAX_LENGTH = 64;
    public final Integer ID_MIN_VALUE = 1;
    public final Integer ID_MAX_VALUE = Integer.MAX_VALUE;
    public final String EMAIL_MIN_LENGTH = "6";
    public final String EMAIL_MAX_LENGTH = "30";
    public final Integer COMPANY_NAME_MIN_LENGTH = 1;
    public final Integer COMPANY_NAME_MAX_LENGTH = 30;
    public final Integer FIRST_NAME_MIN_LENGTH = 2;
    public final Integer FIRST_NAME_MAX_LENGTH = 12;
    public final Integer LAST_NAME_MIN_LENGTH = 2;
    public final Integer LAST_NAME_MAX_LENGTH = 20;
    public final Integer PASSWORD_MIN_LENGTH = 4;
    public final Integer PASSWORD_MAX_LENGTH = 12;
    public final Integer COUPON_TITLE_MIN_LENGTH = 1;
    public final Integer COUPON_TITLE_MAX_LENGTH = 20;
    public final Integer COUPON_DESCRIPTION_MIN_LENGTH = 0;
    public final Integer COUPON_DESCRIPTION_MAX_LENGTH = 120;
    public final LocalDate COUPON_START_DATE_MIN_VALUE = LocalDate.MIN;
    public final LocalDate COUPON_START_DATE_MAX_VALUE = LocalDate.MAX;
    public final LocalDate COUPON_END_DATE_MIN_VALUE = LocalDate.MIN;
    public final LocalDate COUPON_END_DATE_MAX_VALUE = LocalDate.MAX;
    public final Integer COUPON_IMAGE_MIN_LENGTH = 3;
    public final Integer COUPON_IMAGE_MAX_LENGTH = 255;
    public final Integer AMOUNT_MIN_VALUE = 0;
    public final Integer AMOUNT_MAX_VALUE = Integer.MAX_VALUE;
    public final Double PRICE_MIN_VALUE = 0.0;
    public final Double PRICE_MAX_VALUE = Double.MAX_VALUE;
    public final Integer CATEGORY_NAME_MIN_LENGTH = 1;
    public final Integer CATEGORY_NAME_MAX_LENGTH = 24;

    public final Pattern EMAIL_PATTERN = Pattern.compile("^[((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])]" + "{" + EMAIL_MIN_LENGTH + "," + EMAIL_MAX_LENGTH + "}$");
    public final Pattern COMPANY_NAME_PATTERN = Pattern.compile("^[\\s\\S]{" + COMPANY_NAME_MIN_LENGTH + "," + COMPANY_NAME_MAX_LENGTH + "}$");
    public final Pattern FIRST_NAME_PATTERN = Pattern.compile("^[^\\n0-9_!¡?÷¿\\+=@#$%ˆ&*(){}|~<>;:\\]]{" + FIRST_NAME_MIN_LENGTH + "," + FIRST_NAME_MAX_LENGTH + "}$");
    public final Pattern LAST_NAME_PATTERN = Pattern.compile("^[^\\n0-9_!¡?÷¿\\+=@#$%ˆ&*(){}|~<>;:\\]]{" + LAST_NAME_MIN_LENGTH + "," + LAST_NAME_MAX_LENGTH + "}$");
    public final Pattern PASSWORD_PATTERN = Pattern.compile("^[\\S]{" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "}$");
    public final Pattern COUPON_TITLE_PATTERN = Pattern.compile("^[\\s\\S]{" + COUPON_TITLE_MIN_LENGTH + "," + COUPON_TITLE_MAX_LENGTH + "}$");
    public final Pattern COUPON_DESCRIPTION_PATTERN = Pattern.compile("^[\\s\\S]{" + COUPON_DESCRIPTION_MIN_LENGTH + "," + COUPON_DESCRIPTION_MAX_LENGTH + "}$");
    public final Pattern COUPON_IMAGE_PATTERN = Pattern.compile("^[\\S]{" + COUPON_IMAGE_MIN_LENGTH + "," + COUPON_IMAGE_MAX_LENGTH + "}$");
    public final Pattern CATEGORY_NAME_PATTERN = Pattern.compile("^[\\s\\S]{" + CATEGORY_NAME_MIN_LENGTH + "," + CATEGORY_NAME_MAX_LENGTH + "}$");

    public final Pattern DB_USERNAME_PATTERN = Pattern.compile("^[\\S]{" + DB_USERNAME_MIN_LENGTH + "," + DB_USERNAME_MAX_LENGTH + "}$");
    public final Pattern DB_PASSWORD_PATTERN = Pattern.compile("^[\\S]{" + DB_PASSWORD_MIN_LENGTH + "," + DB_PASSWORD_MAX_LENGTH + "}$");
    public final Pattern DB_URL_PATTERN = Pattern.compile("^[\\S]{" + DB_URL_MIN_LENGTH + "," + DB_URL_MAX_LENGTH + "}$");
    public final Pattern DB_SCHEMA_NAME_PATTERN = Pattern.compile("^[\\S]{" + DB_SCHEMA_NAME_MIN_LENGTH + "," + DB_SCHEMA_NAME_MAX_LENGTH + "}$");

    public Constants() {
    }
}
