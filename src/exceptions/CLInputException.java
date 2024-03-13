package exceptions;

public class CLInputException extends CLException{

    public CLInputException(String message, Integer errorCode) {
        super(message,errorCode);
    }

}



//"rs to list error",999
//"rs to list error-null/s sent",899
//"Invalid id value - not in range",701
//"Invalid email length and/or pattern",702
//"Invalid company name length and/or pattern",703
//"Invalid first name length and/or pattern",704
//"Invalid last name length and/or pattern",705
//"Invalid password length",706
//"Invalid coupon title length and/or pattern",707
//"Invalid coupon description length and/or pattern",708
//"Invalid coupon image(path) length and/or pattern", 709
//"Invalid category name length and/or pattern",710
//"Invalid amount value - not in range", 711
//"Invalid price value - not in range", 712
//"Invalid start date value", 721
//"Invalid end date value", 722
//"Invalid db username length and/or pattern", 731
//"Invalid db password length and/or pattern", 732
//"Invalid db URL length and/or pattern", 733
//"Invalid db schema name length and/or pattern", 734
//"Invalid category - does not exist on database", 735