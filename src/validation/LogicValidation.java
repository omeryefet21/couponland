package validation;

import beans.Coupon;

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class LogicValidation {
    public final BiPredicate<Integer, Coupon> OWNED_COUPON = (companyID, coupon) -> Objects.equals(coupon.getCompanyID(), companyID);
    public final Predicate<Coupon> UNEXPIRED_COUPON = coupon -> coupon.getEndDate().isAfter(LocalDate.now());
    public final Predicate<Coupon> ACTIVE_COUPON = coupon -> coupon.getEndDate().isAfter(coupon.getStartDate());
    public final Predicate<Coupon> IN_STOCK_COUPON = coupon -> coupon.getAmount() > 0;

    public LogicValidation() {
    }
}
