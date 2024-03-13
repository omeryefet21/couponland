package utilities;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import db.*;
import dbdao.CompaniesDBDAO;
import dbdao.CouponsDBDAO;
import dbdao.CustomersDBDAO;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import facades.AdminFacade;
import facades.CompanyFacade;
import local.CouponlandSystem;
import loginmanager.ClientType;
import loginmanager.LoginManager;
import validation.Constants;
import validation.InputValidation;


import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import static db.QueryReturnType.*;

public class Program {
    public static void main(String[] args) throws CLInputException, CLLogicException {
        Test.testAll();
  //      CouponlandSystem.getInstance().initializeCouponlandSystem();


        //        String a = "omom";
//        String b = "eeee";
//        String c = "2321";
//        LocalDate d = LocalDate.of(2010, 8, 22);


//
//        System.out.println(GeneralUtils.getParamsMap(List.of(a,b,c,d,432,324,32,423,4,32,324.443,324)));
//        System.out.println(DBUtils.runQuery(SQLCommands.CREATE_COUPONLAND_SCHEMA));
//
//        SQLCommands.getInstallationSQL().stream().map(DBUtils::runQuery).forEach(result-> System.out.println(result?"Done!":"!!!!"));
//        try {
//
//            ConnectionPool connectionPool = ConnectionPool.getInstance();
//        } catch (NullPointerException exception) {
//            System.out.println("make sure connection pool is initialized");
//        }

//        DBManager dbManager = new DBManager();
//        ResultSet resultSet = null;
//        ConnectionPool.initializeConnectionPool(dbManager);
//        try {
//
//
//            resultSet = ConnectionPool.getInstance().getConnection().prepareStatement("select count(*) from information_schema.schemata where schema_name='couponland'").executeQuery();
//
//            resultSet.next();
//            if (resultSet.getInt(1) == 0)
//                System.out.println("no schema");
//            else
//                System.out.println("yes schema");
//        } catch (SQLException | InterruptedException exception) {
//            System.out.println(exception.getMessage());
//        }
//        System.out.println();
//        CouponlandSystem.Installation installation = new CouponlandSystem.Installation();
        //    installation.getSqlCommands().getInstallationStack().stream().map(DBUtils::runQuery).forEach(result-> System.out.println(result?"Done!":"!!!!"));
//        List<Object> list = GeneralUtils.resultSetToList(COMPANY, DBUtils.runQueryFroResult("SELECT * FROM couponland.companies;", new HashMap<>()));
//        System.out.println(list);
//        List<Company> list2;
//
//        CouponlandSystem.getInstance().initializeCouponlandSystem();
//        CouponlandSystem.getInstance().loadInstallationAndRun(1);

//        System.out.println(CouponlandSystem.getInstance().createAndSaveInstallation(new String[]{"jdbc:mysql://localhost:3306", "root", "1234", "test32"}));

//        System.out.println(CouponlandSystem.getInstallations());
//        System.out.println(CouponlandSystem.getInstance().createAndSaveInstallation(new String[]{null, null, null, "1234"}));
//        System.out.println(CouponlandSystem.getInstallations());
//        CouponlandSystem.getInstance().getInstallationsFromFile();
//        System.out.println(CouponlandSystem.getInstallations());
//        CouponlandSystem.getInstance().deleteInstallation(5, 6, 7, 8, 7, 18);
//        System.out.println(CouponlandSystem.getInstallations());
//        CouponlandSystem.getInstance().updateInstallation(2, new String[]{"jdbc:mysql://localhost:3306", "root", "1234"});
//        System.out.println(CouponlandSystem.getInstallations());

//        Object[] params=new Object[]{CouponFilter.ID,">=?",CouponFilter.AMOUNT,"=?",CouponFilter.CATEGORY_ID,"IS NOT NULL"};
//        Object[] params2=new Object[]{};

//        map.put(CouponFilter.ID,">?");
//        map.put(CouponFilter.AMOUNT,"=?");
//        map.put(CouponFilter.CATEGORY_ID,"IS NOT NULL");

//        System.out.println(CouponlandSystem.getInstance().getLoadedSqlCommands().getGetFilteredCommand(COUPON,params ));
//        System.out.println(CouponlandSystem.getInstance().getLoadedSqlCommands().getCVCCouponsFilteredCommand(params2 ));
//
//        System.out.println(DBUtils.runQueryForResultList(COUPON,CouponlandSystem.getInstance().getLoadedSqlCommands().getGetFilteredCommand(COUPON,params ),GeneralUtils.getParamsMap(18,5)));
//        System.out.println(DBUtils.runQueryForResultList(COUPON,CouponlandSystem.getInstance().getLoadedSqlCommands().getCVCCouponsFilteredCommand(null ),
//                GeneralUtils.getParamsMap(1)));
//
//
//        System.out.println(CouponlandSystem.getInstance().getCategoryMap());
//        System.out.println(new CompaniesDBDAO().isCompanyExists("omer1", "om1"));
//        System.out.println(new CustomersDBDAO().isCustomerExists("cus1", "cuspass1"));

        //      new AdminFacade().addCompany(new Company("omer666d","dasaf","tesr",null));
//        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306","root","12334");
//        try {

//            CompanyFacade companyFacade = (CompanyFacade) LoginManager.getInstance().login("few", "few", ClientType.COMPANY);
//            System.out.println(companyFacade.getCompanyID());
//            companyFacade.addCoupon(new Coupon(1, Category.FOOD, "title3", "description1", LocalDate.of(2005, 12, 28), LocalDate.of(2005, 12, 30), 20, 19.90, "imagepath1"));
//            System.out.println(companyFacade.getCompanyCoupons());
//            companyFacade.deleteCoupon(null);
//            GeneralUtils.resultSetToList(null, null);
//            CouponlandSystem.getInstance().loadInstallationAndRun(1);


//
//    Matcher matcher= InputValidation.EMAIL_PATTERN.matcher("bDFBDFBdfbDBs@vsrewfrfebfdD.FBb");
//    System.out.println(matcher.matches());
        //System.out.println(exception.getErrorCode());
//            InputValidation.validateEmail("bDFBbDBs@vsrawdgfeweb.FBb");
//            InputValidation.validateCompanyName("sfdsdfewfewfegdfgfdgewsvg");
//            InputValidation.validateFirstName("boooo");
//            InputValidation.validateCompanyName("dawdd ");
//            new AdminFacade().addCompany(new Company("company6", "companymail9@gmail.com", "vdvsvsv", null));
//            new AdminFacade().updateCompany(new Company(12,"company7", "companyemail1@gmail.com", "vdv777", null));
//            CouponlandSystem.updateInstallation(1,new String[]{"jdbc:mysql://localhost:3306", "root", "1234"});
//            System.out.println(CouponlandSystem.getInstallations());
//
//        } catch (CLInputException exception) {
//            System.out.println(exception.getMessage());
//        } catch (CLLogicException e) {
//            System.out.println(e.getErrorCode());
//            System.out.println(e.getMessage());
//        }


//        new CouponsDBDAO().addCoupon(new Coupon(18, Category.FOOD, "123444", "desc", LocalDate.now(), LocalDate.now(), 22, 22.5, "ima"));

//        System.out.println(new CouponsDBDAO().getOneCoupon(1));
//        System.out.println(new CouponsDBDAO().getAllCompanyCoupons(1));
//        System.out.println(new CompaniesDBDAO().getAllCompanies());
//
//
//        System.out.println(new CompaniesDBDAO().isCompanyExists("omer15", "omerpass4"));
//        System.out.println(new CompaniesDBDAO().isCompanyExists(18));
//        new CompaniesDBDAO().addCompany(new Company("omer16", "omer165", "omerpass4", null));
//        new CompaniesDBDAO().updateCompany(new Company(1, "omer14", "omer15", "newpassdd", null));
//        new CompaniesDBDAO().deleteCompany(49);
//        System.out.println(new CompaniesDBDAO().getOneCompany(81));
//        System.out.println(new CompaniesDBDAO().getAllCompanies());
//        System.out.println(new CustomersDBDAO().isCustomerExists("453", "cuspass2"));
//        System.out.println(new CustomersDBDAO().isCustomerExists(1));
//        new CustomersDBDAO().addCustomer(new Customer("shop1", "shopy1", "shop321", "pass1", null));
//        new CustomersDBDAO().updateCustomer(new Customer(36, "shop2", "shopy1", "shop31", "pass1", null));
//        new CustomersDBDAO().deleteCustomer(36);
//        System.out.println(new CustomersDBDAO().getOneCustomer(4));
//        System.out.println(new CustomersDBDAO().getAllCustomers());
//        new CouponsDBDAO().addCoupon(new Coupon(1, Category.FOOD, "t32de444d", null, LocalDate.now(), LocalDate.now(), 22, 22.5, null));
//        new CouponsDBDAO().updateCoupon(new Coupon(110, 1, Category.FURNITURE, "t32ddqwe444d", null, LocalDate.now(), LocalDate.now(), -1, 22.5, null));
//        new CouponsDBDAO().deleteCoupon(102);
//        System.out.println(new CouponsDBDAO().getOneCoupon(110));
//        System.out.println(new CouponsDBDAO().getAllCoupons());
//        System.out.println(new CouponsDBDAO().getAllCompanyCoupons(14412));
//        System.out.println(new CouponsDBDAO().getAllCustomerCoupons(1));
//        new CouponsDBDAO().addCouponPurchase(38, 83);
//        new CouponsDBDAO().deleteCouponPurchase(1, 77);
//        new CustomersDBDAO().deleteCustomer(1);
//        new CompaniesDBDAO().deleteCompany(81);
//        new CouponsDBDAO().updateCoupon(new Coupon(110, 1, Category.FURNITURE, "t32ddqwe444d", null, LocalDate.now(), LocalDate.now(), -1, 300.0, null));


//
//        CouponlandSystem system1 = CouponlandSystem.getInstance();
//        system1.loadInstallationAndRun(new String[]{"jdbc:mysql://localhost:3306", "root", "1234", "couponland"});
//        ResultSet resultSet;
//        try {
//            resultSet = system1.getConnectionPool().getConnection().prepareStatement("select count(*) from information_schema.schemata where schema_name='couponland4444'").executeQuery();
//            resultSet.next();
//            if (resultSet.getInt(1) == 0)
//                System.out.println("no schema");
//            else
//                System.out.println("yes schema");
//        } catch (SQLException | InterruptedException exception) {
//            System.out.println(exception.getMessage());
//        }
//        List<Object> list = GeneralUtils.resultSetToList(CATEGORY, DBUtils.runQueryFroResult(CouponlandSystem.getInstance().getLoadedSqlCommands().GET_CATEGORIES));
//        System.out.println(list);
//        System.out.println(GeneralUtils.mapCurrentCategories(system1.getLoadedDbManager()));


    }


}
