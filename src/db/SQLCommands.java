package db;

import beans.Category;
import validation.Constants;

import java.io.Serializable;
import java.util.Stack;
import java.util.stream.IntStream;


public class SQLCommands implements Serializable {


    private final String SCHEMA_NAME;
    private final Stack<String> installationStack;

    public final String GET_CATEGORIES;

    public final String COMPANY_EXISTS;
    public final String COMPANY_EXISTS_ID;

    public final String CUSTOMER_EXISTS;
    public final String CUSTOMER_EXISTS_ID;

    public final String ADD_COMPANY;
    public final String ADD_CUSTOMER;
    public final String ADD_COUPON;
    public final String ADD_CVC_ENTRY;

    public final String UPDATE_COMPANY;
    public final String UPDATE_CUSTOMER;
    public final String UPDATE_COUPON;
    public final String UPDATE_COUPON_PURCHASE;

    public final String DELETE_COMPANY;
    public final String DELETE_CUSTOMER;
    public final String DELETE_COUPON;
    public final String DELETE_EXPIRED_COUPONS;
    public final String DELETE_CVC_ENTRY;

    public final String GET_COMPANY;
    public final String GET_COMPANY_BY_LOGIN;
    public final String GET_ALL_COMPANIES;

    public final String GET_CUSTOMER;
    public final String GET_CUSTOMER_BY_LOGIN;
    public final String GET_ALL_CUSTOMERS;

    public final String GET_COUPON;
    public final String GET_COUPONS_BY_COMPANY_ID;
    public final String GET_COUPONS_BY_CUSTOMER_ID;
    public final String GET_ALL_COUPONS;

    public final String GET_CVC_ENTRY;
    public final String GET_ALL_CVC;

    public final String FILTER_BY_ID;
    public final String FILTER_BY_COUPON_ID;
    public final String FILTER_BY_CUSTOMER_ID;

    public final String FILTER_BY_COMPANY_ID;
    public final String FILTER_BY_CATEGORY_ID;
    public final String FILTER_BY_TITLE;
    public final String FILTER_BY_DESCRIPTION;
    public final String FILTER_BY_AMOUNT;
    public final String FILTER_BY_PRICE;
    public final String FILTER_BY_START_DATE;
    public final String FILTER_BY_END_DATE;
    public final String GET_COUPONS_BY_CVC_ENTRY;
    public final String FILTER_BY_IMAGE;


    public SQLCommands(String schemaName,Constants constants) {
        SCHEMA_NAME = schemaName == null ? constants.DEFAULT_SCHEMA_NAME : schemaName;
        String CREATE_COUPONLAND_SCHEMA = "CREATE SCHEMA `" + SCHEMA_NAME + "` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;";
        installationStack = new Stack<>();
        installationStack.push(CREATE_COUPONLAND_SCHEMA);
        String CREATE_COMPANIES_TABLE = "CREATE TABLE IF NOT EXISTS `" + SCHEMA_NAME + "`.`companies` (" +
                " `id` INT NOT NULL AUTO_INCREMENT," +
                " `name` VARCHAR("+constants.COMPANY_NAME_MAX_LENGTH+") NOT NULL," +
                " `email` VARCHAR("+constants.EMAIL_MAX_LENGTH+") NOT NULL," +
                " `password` VARCHAR("+constants.PASSWORD_MAX_LENGTH+") NOT NULL," +
                " PRIMARY KEY (`id`)," +
                " UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE," +
                " UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);";
        installationStack.push(CREATE_COMPANIES_TABLE);
        String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS `" + SCHEMA_NAME + "`.`customers` (" +
                " `id` INT NOT NULL AUTO_INCREMENT," +
                " `first_name` VARCHAR("+constants.FIRST_NAME_MAX_LENGTH+") NOT NULL," +
                " `last_name` VARCHAR("+constants.LAST_NAME_MAX_LENGTH+") NOT NULL," +
                " `email` VARCHAR("+constants.EMAIL_MAX_LENGTH+") NOT NULL," +
                " `password` VARCHAR("+constants.PASSWORD_MAX_LENGTH+") NOT NULL," +
                " PRIMARY KEY (`id`)," +
                " UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);";
        installationStack.push(CREATE_CUSTOMERS_TABLE);
        String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS `" + SCHEMA_NAME + "`.`categories` (" +
                " `id` INT NOT NULL AUTO_INCREMENT," +
                " `name` VARCHAR("+constants.CATEGORY_NAME_MAX_LENGTH+") NOT NULL," +
                " PRIMARY KEY (`id`)," +
                " UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);";
        installationStack.push(CREATE_CATEGORIES_TABLE);
        String CREATE_COUPONS_TABLE = "CREATE TABLE IF NOT EXISTS `" + SCHEMA_NAME + "`.`coupons` (" +
                " `id` INT NOT NULL AUTO_INCREMENT," +
                " `company_id` INT NOT NULL," +
                " `category_id` INT NOT NULL," +
                " `title` VARCHAR("+constants.COUPON_TITLE_MAX_LENGTH+") NOT NULL," +
                " `description` VARCHAR("+constants.COUPON_DESCRIPTION_MAX_LENGTH+") NULL DEFAULT '"+constants.DEFAULT_COUPON_DESCRIPTION+"'," +
                " `start_date` DATE NOT NULL," +
                " `end_date` DATE NOT NULL," +
                " `amount` INT UNSIGNED NOT NULL," +
                " `price` DOUBLE UNSIGNED NOT NULL," +
                " `image` VARCHAR("+constants.COUPON_IMAGE_MAX_LENGTH+") NULL DEFAULT '"+constants.DEFAULT_COUPON_IMAGE+"'," +
                " PRIMARY KEY (`id`)," +
                " UNIQUE INDEX `unique coupon name per company` (`company_id` ASC, `title` ASC) INVISIBLE);";
        installationStack.push(CREATE_COUPONS_TABLE);
        String CREATE_CUSTOMERS_VS_COUPONS_TABLE = "CREATE TABLE IF NOT EXISTS `" + SCHEMA_NAME + "`.`customers_vs_coupons` (" +
                " `customer_id` INT NOT NULL," +
                " `coupon_id` INT NOT NULL," +
                " PRIMARY KEY (`customer_id`, `coupon_id`));";
        installationStack.push(CREATE_CUSTOMERS_VS_COUPONS_TABLE);
        String SET_FK_COUPONS_TABLE1 = "ALTER TABLE `" + SCHEMA_NAME + "`.`coupons`" +
                " ADD CONSTRAINT `fk_company_id`" +
                " FOREIGN KEY (`company_id`)" +
                " REFERENCES `" + SCHEMA_NAME + "`.`companies` (`id`)" +
                " ON DELETE CASCADE" +
                " ON UPDATE CASCADE;";
        installationStack.push(SET_FK_COUPONS_TABLE1);
        String SET_FK_COUPONS_TABLE2 = "ALTER TABLE `" + SCHEMA_NAME + "`.`coupons` " +
                "ADD INDEX `fk_category_id_idx` (`category_id` ASC) VISIBLE;" +
                ";";
        installationStack.push(SET_FK_COUPONS_TABLE2);
        String SET_FK_COUPONS_TABLE3 = "ALTER TABLE `" + SCHEMA_NAME + "`.`coupons`" +
                " ADD CONSTRAINT `fk_category_id`" +
                " FOREIGN KEY (`category_id`)" +
                " REFERENCES `" + SCHEMA_NAME + "`.`categories` (`id`)" +
                " ON DELETE CASCADE" +
                " ON UPDATE CASCADE;";
        installationStack.push(SET_FK_COUPONS_TABLE3);
        String SET_FK_CUSTOMERS_VS_COUPONS1 = "ALTER TABLE `" + SCHEMA_NAME + "`.`customers_vs_coupons` " +
                "ADD INDEX `fk_coupon_id_idx` (`coupon_id` ASC) VISIBLE;" +
                ";";
        installationStack.push(SET_FK_CUSTOMERS_VS_COUPONS1);
        String SET_FK_CUSTOMERS_VS_COUPONS2 = "ALTER TABLE `" + SCHEMA_NAME + "`.`customers_vs_coupons` " +
                "ADD CONSTRAINT `fk_coupon_id`" +
                "  FOREIGN KEY (`coupon_id`)" +
                "  REFERENCES `" + SCHEMA_NAME + "`.`coupons` (`id`)" +
                "  ON DELETE CASCADE" +
                "  ON UPDATE CASCADE," +
                "ADD CONSTRAINT `fk_customer_id`" +
                "  FOREIGN KEY (`customer_id`)" +
                "  REFERENCES `" + SCHEMA_NAME + "`.`customers` (`id`)" +
                "  ON DELETE CASCADE" +
                "  ON UPDATE CASCADE;";
        installationStack.push(SET_FK_CUSTOMERS_VS_COUPONS2);
        String SET_TRIGGER_COMPANIES1 = "CREATE DEFINER = CURRENT_USER TRIGGER `" + SCHEMA_NAME + "`.`lock_name` BEFORE UPDATE ON `companies` FOR EACH ROW" +
                " BEGIN" +
                "        IF (old.name != new.name) THEN" +
                "            SIGNAL SQLSTATE '45000'" +
                "                SET MESSAGE_TEXT = 'CANNOT CHANGE COMPANY NAME';" +
                "        END IF;" +
                "    END";
        installationStack.push(SET_TRIGGER_COMPANIES1);
        String SET_TRIGGER_COMPANIES2 = "CREATE DEFINER = CURRENT_USER TRIGGER `" + SCHEMA_NAME + "`.`lock_id_companies` BEFORE UPDATE ON `companies` FOR EACH ROW" +
                " BEGIN" +
                "        IF (old.id != new.id) THEN" +
                "            SIGNAL SQLSTATE '45000'" +
                "                SET MESSAGE_TEXT = 'CANNOT CHANGE COMPANY ID';" +
                "        END IF;" +
                "    END";
        installationStack.push(SET_TRIGGER_COMPANIES2);
        String SET_TRIGGER_COUPONS1 = "CREATE DEFINER = CURRENT_USER TRIGGER `" + SCHEMA_NAME + "`.`lock_company_id` BEFORE UPDATE ON `coupons` FOR EACH ROW" +
                " BEGIN" +
                "        IF (old.company_id != new.company_id) THEN" +
                "            SIGNAL SQLSTATE '45000'" +
                "                SET MESSAGE_TEXT = 'CANNOT CHANGE COUPON`S COMPANY ID';" +
                "        END IF;" +
                "    END";
        installationStack.push(SET_TRIGGER_COUPONS1);
        String SET_TRIGGER_COUPONS2 = "CREATE DEFINER = CURRENT_USER TRIGGER `" + SCHEMA_NAME + "`.`lock_id_coupons` BEFORE UPDATE ON `coupons` FOR EACH ROW" +
                " BEGIN" +
                "        IF (old.id != new.id) THEN" +
                "            SIGNAL SQLSTATE '45000'" +
                "                SET MESSAGE_TEXT = 'CANNOT CHANGE COUPON ID';" +
                "        END IF;" +
                "    END";
        installationStack.push(SET_TRIGGER_COUPONS2);
        String SET_TRIGGER_CUSTOMERS1 = "CREATE DEFINER = CURRENT_USER TRIGGER `" + SCHEMA_NAME + "`.`lock_id_customers` BEFORE UPDATE ON `customers` FOR EACH ROW" +
                " BEGIN" +
                "        IF (old.id != new.id) THEN" +
                "            SIGNAL SQLSTATE '45000'" +
                "                SET MESSAGE_TEXT = 'CANNOT CUSTOMER ID';" +
                "        END IF;" +
                "    END";
        installationStack.push(SET_TRIGGER_CUSTOMERS1);
        Category.getCategoryNames().forEach(name -> installationStack.push("INSERT INTO `" + SCHEMA_NAME + "`.`categories` (`name`) VALUES ('" + name + "');"));


        GET_CATEGORIES = "SELECT * FROM `" + SCHEMA_NAME + "`.categories;";

        COMPANY_EXISTS = "SELECT count(*) FROM `" + SCHEMA_NAME + "`.`companies` WHERE (`email`=?) AND (`password`=?)";
        COMPANY_EXISTS_ID = "SELECT count(*) FROM `" + SCHEMA_NAME + "`.`companies` WHERE (`id` = ?)";

        CUSTOMER_EXISTS = "SELECT count(*) FROM `" + SCHEMA_NAME + "`.customers WHERE (`email`=?) AND (`password`=?)";
        CUSTOMER_EXISTS_ID = "SELECT count(*) FROM `" + SCHEMA_NAME + "`.`customers` WHERE (`id` = ?)";

        ADD_COMPANY = "INSERT INTO `" + SCHEMA_NAME + "`.`companies` (`name`, `email`, `password`) VALUES (?,?,?);";
        ADD_CUSTOMER = "INSERT INTO `" + SCHEMA_NAME + "`.`customers` (`first_name`, `last_name`, `email`, `password`) VALUES (?,?,?,?);";
        ADD_COUPON = "INSERT INTO `" + SCHEMA_NAME + "`.`coupons` (`company_id`, `category_id`, `title`, `description`, `start_date`, `end_date`, `amount`, `price`, `image`) VALUES (?,?,?,?,?,?,?,?,?);";
        ADD_CVC_ENTRY = "INSERT INTO `" + SCHEMA_NAME + "`.`customers_vs_coupons` (`customer_id`, `coupon_id`) VALUES (?, ?);";

        UPDATE_COMPANY = "UPDATE `" + SCHEMA_NAME + "`.`companies` SET `name` = ?, `email` = ?, `password` = ? WHERE (`id` = ?);";
        UPDATE_CUSTOMER = "UPDATE `" + SCHEMA_NAME + "`.`customers` SET `first_name` = ?, `last_name` = ?, `email` = ?, `password` = ? WHERE (`id` = ?);";
        UPDATE_COUPON = "UPDATE `" + SCHEMA_NAME + "`.`coupons` SET `company_id` = ?, `category_id` = ?, `title` = ?, `description` = ?, `start_date` = ?, `end_date` = ?, `amount` = ?, `price` = ?, `image` = ? WHERE (`id` = ?);";
        UPDATE_COUPON_PURCHASE =  "UPDATE `" + SCHEMA_NAME + "`.`coupons` SET `amount` = `amount`-1 WHERE (`id` = ?);";
        DELETE_COMPANY = "DELETE FROM `" + SCHEMA_NAME + "`.`companies` WHERE (`id` = ?);";
        DELETE_CUSTOMER = "DELETE FROM `" + SCHEMA_NAME + "`.`customers` WHERE (`id` = ?);";
        DELETE_COUPON = "DELETE FROM `" + SCHEMA_NAME + "`.`coupons` WHERE (`id` = ?);";
        DELETE_CVC_ENTRY = "DELETE FROM `" + SCHEMA_NAME + "`.`customers_vs_coupons` WHERE (`customer_id` = ?) AND (`coupon_id` = ?);";
        DELETE_EXPIRED_COUPONS= "DELETE FROM `" + SCHEMA_NAME + "`.`coupons` WHERE id>0 AND (end_date=current_date() OR end_date<current_date());";

        GET_COMPANY = "SELECT * FROM `" + SCHEMA_NAME + "`.`companies` WHERE (`id` = ?);";
        GET_COMPANY_BY_LOGIN = "SELECT * FROM `" + SCHEMA_NAME + "`.`companies` WHERE (`email`=?) AND (`password`=?)";
        GET_ALL_COMPANIES = "SELECT * FROM `" + SCHEMA_NAME + "`.`companies`;";

        GET_CUSTOMER = "SELECT * FROM `" + SCHEMA_NAME + "`.`customers` WHERE (`id` = ?);";
        GET_CUSTOMER_BY_LOGIN = "SELECT * FROM `" + SCHEMA_NAME + "`.`customers` WHERE (`email`=?) AND (`password`=?)";
        GET_ALL_CUSTOMERS = "SELECT * FROM `" + SCHEMA_NAME + "`.`customers`;";

        GET_COUPON = "SELECT * FROM `" + SCHEMA_NAME + "`.`coupons` WHERE (`id` = ?);";
        GET_COUPONS_BY_COMPANY_ID = "SELECT * FROM `" + SCHEMA_NAME + "`.`coupons` WHERE (`company_id` = ?);";
        GET_COUPONS_BY_CUSTOMER_ID = "SELECT c.* FROM " + SCHEMA_NAME + ".`coupons` c JOIN  " + SCHEMA_NAME + ".`customers_vs_coupons` v ON c.`id`=v.`coupon_id` WHERE v.`customer_id`=?;";
        GET_COUPONS_BY_CVC_ENTRY = "SELECT c.* FROM " + SCHEMA_NAME + ".`coupons` c JOIN  " + SCHEMA_NAME + ".`customers_vs_coupons` v ON c.`id`=v.`coupon_id`";
        GET_ALL_COUPONS = "SELECT * FROM `" + SCHEMA_NAME + "`.`coupons`;";

        GET_CVC_ENTRY = "SELECT * FROM `" + SCHEMA_NAME + "`.`customers_vs_coupons` WHERE (`customer_id` = ?) AND (`coupon_id` = ?);";
        GET_ALL_CVC= "SELECT * FROM `" + SCHEMA_NAME + "`.`customers_vs_coupons`;";

        FILTER_BY_ID = " (`id` ";
        FILTER_BY_COUPON_ID = " (`coupon_id` ";
        FILTER_BY_CUSTOMER_ID= " (`customer_id` ";
        FILTER_BY_COMPANY_ID = " (`company_id` ";
        FILTER_BY_CATEGORY_ID = " (`category_id` ";
        FILTER_BY_TITLE = " (`title` ";
        FILTER_BY_DESCRIPTION = " (`description` ";
        FILTER_BY_AMOUNT = " (`amount` ";
        FILTER_BY_PRICE = " (`price` ";
        FILTER_BY_START_DATE = " (`start_date` ";
        FILTER_BY_END_DATE = " (`end_date` ";
        FILTER_BY_IMAGE = " (`image` ";

    }

    /**
     * Returns a String to be used as a filtered retrieve command.
     * @param returnType Designated ReturnType.
     * @param params A sequence pairs of filters and operators.
     * @return a String to be used as a filtered retrieve command.
     */
    public String getGetFilteredCommand(QueryReturnType returnType, Object[] params) {
        StringBuilder result = new StringBuilder(returnType.getFilterCommand());
        result.delete(result.length() - 1, result.length());
        result.append(" WHERE");
        IntStream.range(0, params.length).filter(i -> i % 2 == 0).forEach(i -> {
            result.append(((Filter) params[i]).getFilterCommand());
            result.append(params[i + 1]).append(") AND");
        });
        result.delete(result.length() - 4, result.length());
        result.append(";");
        return result.toString();
    }

    /**
     * Returns a String to be used as a filtered purchased coupons retrieve command.
     * @param params A sequence of pairs of coupon filters and operators.
     * @return a String to be used as a filtered purchased coupons retrieve command.
     */
    public String getCVCCouponsFilteredCommand(Object[] params) {
        StringBuilder result = new StringBuilder(GET_COUPONS_BY_CVC_ENTRY);
        result.append(" WHERE");
        if (params != null) {
            IntStream.range(0, params.length).filter(i -> i % 2 == 0).forEach(i -> {
                result.append(" (c.`");
                result.append(((Filter) params[i]).getFilterCommand().substring(3));
                result.append(params[i + 1]).append(") AND");
            });
        }
        result.append(" (v.`customer_id`=?);");
        return result.toString();
    }

    public Stack<String> getInstallationStack() {
        return installationStack;
    }

//    private static String SCHEMA_NAME = "couponland3";
//    private final String CREATE_COUPONLAND_SCHEMA = "CREATE SCHEMA IF NOT EXISTS " + SCHEMA_NAME;
//    private final String CREATE_COMPANIES_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + ".`companies` (" +
//            " `id` INT NOT NULL AUTO_INCREMENT," +
//            " `name` VARCHAR(30) NOT NULL," +
//            " `email` VARCHAR(30) NOT NULL," +
//            " `password` VARCHAR(12) NOT NULL," +
//            " PRIMARY KEY (`id`)," +
//            " UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE," +
//            " UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);";
//
//
//    private final String CREATE_CUSTOMERS_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + ".`customers` (" +
//            " `id` INT NOT NULL AUTO_INCREMENT," +
//            " `first_name` VARCHAR(12) NOT NULL," +
//            " `last_name` VARCHAR(20) NOT NULL," +
//            " `email` VARCHAR(30) NOT NULL," +
//            " `password` VARCHAR(12) NOT NULL," +
//            " PRIMARY KEY (`id`)," +
//            " UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE);";
//
//    public static final String CREATE_CATEGORIES_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + ".`categories` (" +
//            " `id` INT NOT NULL AUTO_INCREMENT," +
//            " `name` VARCHAR(24) NOT NULL," +
//            " PRIMARY KEY (`id`)," +
//            " UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);";
//
//    public static final String CREATE_COUPONS_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + ".`coupons` (" +
//            " `id` INT NOT NULL AUTO_INCREMENT," +
//            " `company_id` INT NOT NULL," +
//            " `category_id` INT NOT NULL," +
//            " `title` VARCHAR(20) NOT NULL," +
//            " `description` VARCHAR(120) NULL DEFAULT 'a detailed description will be added soon!'," +
//            " `start_date` DATE NOT NULL," +
//            " `end_date` DATE NOT NULL," +
//            " `amount` INT UNSIGNED NOT NULL," +
//            " `price` DOUBLE UNSIGNED NOT NULL," +
//            " `image` VARCHAR(255) NULL DEFAULT 'an image will be added soon!'," +
//            " PRIMARY KEY (`id`)," +
//            " UNIQUE INDEX `unique coupon name per company` (`company_id` ASC, `title` ASC) INVISIBLE);";
//
//    public static final String CREATE_CUSTOMERS_VS_COUPONS_TABLE = "CREATE TABLE IF NOT EXISTS " + SCHEMA_NAME + ".`customers_vs_coupons` (" +
//            " `customer_id` INT NOT NULL," +
//            " `coupon_id` INT NOT NULL," +
//            " PRIMARY KEY (`customer_id`, `coupon_id`));";
//    public static final String SET_FK_COUPONS_TABLE1 = "ALTER TABLE " + SCHEMA_NAME + ".`coupons`" +
//            " ADD CONSTRAINT `fk_company_id`" +
//            " FOREIGN KEY (`company_id`)" +
//            " REFERENCES `couponland`.`companies` (`id`)" +
//            " ON DELETE CASCADE" +
//            " ON UPDATE CASCADE;";
//    public static final String SET_FK_COUPONS_TABLE2 = "ALTER TABLE " + SCHEMA_NAME + ".`coupons` " +
//            "ADD INDEX `fk_category_id_idx` (`category_id` ASC) VISIBLE;" +
//            ";";
//    public static final String SET_FK_COUPONS_TABLE3 = "ALTER TABLE " + SCHEMA_NAME + ".`coupons`" +
//            " ADD CONSTRAINT `fk_category_id`" +
//            " FOREIGN KEY (`category_id`)" +
//            " REFERENCES `couponland`.`categories` (`id`)" +
//            " ON DELETE CASCADE" +
//            " ON UPDATE CASCADE;";
//
//    public static final String SET_FK_CUSTOMERS_VS_COUPONS1 = "ALTER TABLE " + SCHEMA_NAME + ".`customers_vs_coupons` " +
//            "ADD INDEX `fk_coupon_id_idx` (`coupon_id` ASC) VISIBLE;" +
//            ";";
//    public static final String SET_FK_CUSTOMERS_VS_COUPONS2 = "ALTER TABLE " + SCHEMA_NAME + ".`customers_vs_coupons` " +
//            "ADD CONSTRAINT `fk_coupon_id`" +
//            "  FOREIGN KEY (`coupon_id`)" +
//            "  REFERENCES `couponland`.`coupons` (`id`)" +
//            "  ON DELETE CASCADE" +
//            "  ON UPDATE CASCADE," +
//            "ADD CONSTRAINT `fk_customer_id`" +
//            "  FOREIGN KEY (`customer_id`)" +
//            "  REFERENCES `couponland`.`customers` (`id`)" +
//            "  ON DELETE CASCADE" +
//            "  ON UPDATE CASCADE;";
//
//

}