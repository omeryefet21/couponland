package exceptions;

public class CLLogicException extends CLException {

    public CLLogicException(String message, Integer errorCode) {
        super(message,errorCode);
    }

}

//
//"Client is already logged in", 121
//""Incorrect username and/or password", 101
//"Coupon not found", 141
//"Company not found",143
//"Customer "+customer.getId()+" not found", 146
//"No customers found", 148
//"No coupons found for selected company",142
//"No coupons found for selected customer", 147
//"No companies found",144
//"No coupons found",145
//"Coupon title: " + coupon.getTitle() + " already exists for company "+coupon.getCompanyID(), 151
//"Purchase not found",181
//"Client id is already set", 191
//"Illegal operation - coupon not owned by company",221
//"Illegal operation - Can not update company name", 251
//"Illegal operation - Can not update a coupon's company id", 271
//"Illegal operation - can not delete a logged-in company",261
//"Illegal operation - can not delete a logged-in customer", 262
//"Company email: "+company.getEmail()+" already exist",311
//"Coupon "+coupon.getId()+" was already purchased by customer "+getCustomerID(),351
//"Coupon " + coupon.getId() + " is out of stock", 352
//"Coupon " + coupon.getId() + " is inactive", 353
//"Coupon " + coupon.getId() + " has expired", 354
//"Company name: "+company.getName()+" already exist",312
//"Couponland system not initialized",451
//"Installation creation failed",452
//"I/O exception: "+ioException.getMessage(),453
//"Serialization error: "+classNotFoundException.getMessage(),454
//"Installation "+id+" not found",455
//"Server not responding - check db url",456
//"Access denied - invalid db username and/or password",457
//"Connection pool not initialized",481
//"Operation interrupted - " + exception.getMessage(), 498
//"Unknown sql error: "+exception.getMessage(),499
