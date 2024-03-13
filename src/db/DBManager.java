package db;

import exceptions.CLInputException;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBManager implements Serializable {
    public final String DEFAULT_SCHEMA_NAME = "couponland";
    public final String DEFAULT_URL = "jdbc:mysql://localhost:3306";
    public final String DEFAULT_SQL_USER = "root";
    public final String DEFAULT_SQL_PASSWORD = "1234";
    public final Integer DB_USERNAME_MIN_LENGTH = 1;
    public final Integer DB_USERNAME_MAX_LENGTH = 32;
    public final Integer DB_PASSWORD_MIN_LENGTH = 4;
    public final Integer DB_PASSWORD_MAX_LENGTH = 32;
    public final Integer DB_URL_MIN_LENGTH = 1;
    public final Integer DB_URL_MAX_LENGTH = Integer.MAX_VALUE;
    public final Integer DB_SCHEMA_NAME_MIN_LENGTH = 1;
    public final Integer DB_SCHEMA_NAME_MAX_LENGTH = 64;
    private String url;
    private String sqlUser;
    private String sqlPassword;
    private final String SCHEMA_NAME;


    public DBManager(String url, String sqlUser, String sqlPassword, String schemaName) throws CLInputException {
        this.url = url;
        this.sqlUser = sqlUser;
        this.sqlPassword = sqlPassword;
        this.SCHEMA_NAME = validateDbSchemaName(schemaName);
    }

    public DBManager() throws CLInputException {
        setUrl(DEFAULT_URL);
        setSqlUser(DEFAULT_SQL_USER);
        setSqlPassword(DEFAULT_SQL_PASSWORD);
        this.SCHEMA_NAME = validateDbSchemaName(DEFAULT_SCHEMA_NAME);
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) throws CLInputException {
        this.url = validateDbUrl(url);
    }

    public String getSqlUser() {
        return sqlUser;
    }

    public void setSqlUser(String sqlUser) throws CLInputException {
        this.sqlUser = validateDbUsername(sqlUser);
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public void setSqlPassword(String sqlPassword) throws CLInputException {
        this.sqlPassword = validateDbPassword(sqlPassword);
    }

    public String getSchemaNAme() {
        return SCHEMA_NAME;
    }

    public final Pattern DB_USERNAME_PATTERN = Pattern.compile("^[\\S]{" + DB_USERNAME_MIN_LENGTH + "," + DB_USERNAME_MAX_LENGTH + "}$");
    public final Pattern DB_PASSWORD_PATTERN = Pattern.compile("^[\\S]{" + DB_PASSWORD_MIN_LENGTH + "," + DB_PASSWORD_MAX_LENGTH + "}$");
    public final Pattern DB_URL_PATTERN = Pattern.compile("^[\\S]{" + DB_URL_MIN_LENGTH + "," + DB_URL_MAX_LENGTH + "}$");
    public final Pattern DB_SCHEMA_NAME_PATTERN = Pattern.compile("^[\\S]{" + DB_SCHEMA_NAME_MIN_LENGTH + "," + DB_SCHEMA_NAME_MAX_LENGTH + "}$");


    public String validateDbUsername(String dbUsername) throws CLInputException {
        nullCheck(dbUsername);
        Matcher matcher = DB_USERNAME_PATTERN.matcher(dbUsername);
        if (!matcher.matches()) {
            throw new CLInputException("Invalid db username length and/or pattern", 731);
        }
        return dbUsername;
    }

    public String validateDbPassword(String dbPassword) throws CLInputException {
        nullCheck(dbPassword);
        Matcher matcher = DB_PASSWORD_PATTERN.matcher(dbPassword);
        if (!matcher.matches()) {
            throw new CLInputException("Invalid db password length and/or pattern", 732);
        }
        return dbPassword;
    }

    public String validateDbUrl(String dbUrl) throws CLInputException {
        nullCheck(dbUrl);
        Matcher matcher = DB_URL_PATTERN.matcher(dbUrl);
        if (!matcher.matches()) {
            throw new CLInputException("Invalid db URL length and/or pattern", 733);
        }
        return dbUrl;
    }

    public String validateDbSchemaName(String dbSchemaName) throws CLInputException {
        Matcher matcher = DB_SCHEMA_NAME_PATTERN.matcher(dbSchemaName);
        if (!matcher.matches()) {
            throw new CLInputException("Invalid db schema name length and/or pattern", 734);
        }
        return dbSchemaName;
    }

    public void nullCheck(Object object) throws CLInputException {
        if (object == null) {
            throw new CLInputException("UnNullable parameter", 899);
        }
    }

    @Override
    public String toString() {
        return "DBManager{" +
                "url='" + url + '\'' +
                ", sqlUser='" + sqlUser + '\'' +
                ", sqlPassword='" + sqlPassword + '\'' +
                ", SCHEMA_NAME='" + SCHEMA_NAME + '\'' +
                '}';
    }
}

