package utilities;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import facades.AdminFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;
import local.CouponlandSystem;
import loginmanager.ClientType;
import loginmanager.LoginManager;

import java.time.LocalDate;

public class Test {
    public static void testAll() {
        try {
            CouponlandSystem.initializeCouponlandSystem();
            CouponlandSystem.createAndSaveInstallation(new String[]{"jdbc:mysql://localhost:3306", "root", "1234", "couponland"});
            CouponlandSystem.getInstance().loadInstallationAndRun(1);


            AdminFacade adminFacade = (AdminFacade) LoginManager.getInstance().login("admin@admin.com", "admin", ClientType.ADMINISTRATOR);

            adminFacade.addCompany(new Company("company1","email1@mail.com","pass1",null));
            adminFacade.addCompany(new Company("company2","email2@mail.com","pass1",null));
            adminFacade.addCompany(new Company("company3","email3@mail.com","pass1",null));
            adminFacade.updateCompany(new Company(1, "company1", "email1@mail.com", "pass29", null));
            adminFacade.deleteCompany(3);
            System.out.println(adminFacade.getAllCompanies());
            System.out.println(adminFacade.getOneCompany(1));

            adminFacade.addCustomer(new Customer("custA","omer","customer1@mail.com","password1",null));
            adminFacade.addCustomer(new Customer("custB","omer","customer2@mail.com","password1",null));
            adminFacade.addCustomer(new Customer("custC","omer","customer3@mail.com","password1",null));
            adminFacade.updateCustomer(new Customer(2,"custD","omer","customer2@mail.com","password11",null));
            adminFacade.deleteCustomer(1);
            System.out.println(adminFacade.getAllCustomers());
            System.out.println(adminFacade.getOneCustomer(3));


            CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("email1@mail.com", "pass29", ClientType.COMPANY);

            companyFacade.addCoupon(new Coupon(1, Category.FOOD, "coupon1", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 1, 199.99, "imagepath1"));
            companyFacade.addCoupon(new Coupon(1, Category.FOOD, "coupon2", null, LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 20, 19.90, "imagepath1"));
            companyFacade.addCoupon(new Coupon(1, Category.FURNITURE, "coupon3", null, LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 4, 19.90, "imagepath1"));
            companyFacade.addCoupon(new Coupon(1, Category.ACTIVITY, "coupon4", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 2, 80.0, "imagepath1"));

            CompanyFacade companyFacade2 = (CompanyFacade) LoginManager.getInstance().login("email2@mail.com","pass1", ClientType.COMPANY);
            companyFacade2.addCoupon(new Coupon(2, Category.FOOD, "coupon1", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 20, 119.90, "imagepath1"));
            companyFacade2.addCoupon(new Coupon(2, Category.ACTIVITY, "coupon2", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 8, 9.90, "imagepath1"));
            companyFacade2.addCoupon(new Coupon(2, Category.FURNITURE, "coupon3", null, LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 0, 25.90, null));

            companyFacade.updateCoupon(new Coupon(3,1, Category.FURNITURE, "coupon3", "description3", LocalDate.of(2005, 12, 28), LocalDate.of(2024, 12, 30), 4, 19.90, "imagepath1"));
            companyFacade.deleteCoupon(1);
            System.out.println(companyFacade.getCompanyCoupons());
            System.out.println(companyFacade.getCompanyCoupons(Category.FOOD));
            System.out.println(companyFacade.getCompanyCoupons(70.0));
            System.out.println(companyFacade.getCompanyDetails());


            CustomerFacade customerFacade= (CustomerFacade) LoginManager.getInstance().login("customer3@mail.com","password1",ClientType.CUSTOMER);

            customerFacade.purchaseCoupon(new Coupon(2,1, Category.FOOD, "coupon2", "A detailed description will be added soon!", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 20, 19.90, "imagepath1"));
            customerFacade.purchaseCoupon(new Coupon(3,1, Category.FURNITURE, "coupon3", "description3", LocalDate.of(2005, 12, 28), LocalDate.of(2024, 12, 30), 4, 19.90, "imagepath1"));
            customerFacade.purchaseCoupon(new Coupon(4,1, Category.ACTIVITY, "coupon4", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 2, 80.0, "imagepath1"));
            customerFacade.purchaseCoupon(new Coupon(5,2, Category.FOOD, "coupon1", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2025, 12, 30), 20, 119.90, "imagepath1"));

            System.out.println(customerFacade.getCustomerCoupons());
            System.out.println(customerFacade.getCustomerCoupons(Category.FOOD));
            System.out.println(customerFacade.getCustomerCoupons(100.0));

            System.out.println(customerFacade.getCustomerDetails());
            CouponlandSystem.getInstance().shutDownCouponlandSystem();

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }
}
