package utilities;

import beans.Category;
import beans.Company;
import beans.Coupon;
import beans.Customer;
import db.QueryReturnType;
import exceptions.CLInputException;
import local.CouponlandSystem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class GeneralUtils {

    /**
     * Creates a parameter map to be used to set PreparedStatement's values.
     * @param param value for index 1.
     * @param params additional values.
     * @return a hashmap containing indexes as keys and parameters as values.
     */
    public static Map<Integer, Object> getParamsMap(Object param,Object... params) {
        Map <Integer,Object> result=new HashMap<>();
        result.put(1,param);
        Arrays.stream(params).forEach(object->result.put(result.size()+1,object ));
        return result;
    }

    /**
     * Returns a list of result objects produced from the received ResultSet instance.
     * @param type Type of result objects contained by the returned list.
     * @param resultSet ResultSet instance to be processed.
     * @return a list containing the result objects extracted from the ResultSet instance.
     * @throws CLInputException if an invalid input is used creating the result objects.
     */
    public static List<Object> resultSetToList(QueryReturnType type, ResultSet resultSet) throws CLInputException {
        List<Object> result = new ArrayList<>();
        try {
            switch (type) {
                case COMPANY:
                    while (resultSet.next()) {
                        result.add(new Company(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                                resultSet.getString(4), new ArrayList<>()));
                    }
                    break;
                case CUSTOMER:
                    while (resultSet.next()) {
                        result.add(new Customer(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                                resultSet.getString(4), resultSet.getString(5), new ArrayList<>()));
                    }
                    break;
                case COUPON:
                    while (resultSet.next()) {
                        result.add(new Coupon(resultSet.getInt(1), resultSet.getInt(2), CouponlandSystem.getInstance().getCategoryMap().get(resultSet.getInt(3)),
                                resultSet.getString(4), resultSet.getString(5), resultSet.getDate(6).toLocalDate(), resultSet.getDate(7).toLocalDate(),
                                resultSet.getInt(8), resultSet.getDouble(9), resultSet.getString(10)));
                    }
                    break;
                case C_VS_C:
                    while (resultSet.next()) {
                        result.add(resultSet.getInt(1));
                        result.add(resultSet.getInt(2));
                    }
                    break;
                case BOOLEAN:
                    while (resultSet.next()) {
                        result.add(resultSet.getInt(1) == 1);
                    }
                    break;
                case CATEGORY:
                    while (resultSet.next()) {
                        result.add(resultSet.getInt(1));
                        result.add(resultSet.getString(2));
                    }
                default:
            }
        }
        catch (SQLException exception) {
            throw new CLInputException("rs to list error",999);
        }
        catch (NullPointerException exception) {
            throw new CLInputException("rs to list error-null/s sent",899);
        }
        return result;
    }

    /**
     * Returns the id value (on DB) of a Category.
     * @param category Category to be searched for in 'categoryMap'
     * @return the id value of the received Category.
     */
    public static Integer getCategoryId (Category category) {
       return CouponlandSystem.getInstance().getCategoryMap().entrySet().stream().filter(entry->entry.getValue().equals(category)).findFirst().map(Map.Entry::getKey).orElse(-1);
    }

    /**
     * Removes spaces from 2 end of a String.
     * @param string String to be processed.
     * @return processed String.
     */
    public static String removeSpaces(String string) {
        StringBuilder builder=new StringBuilder(string);
        while (builder.length()>0&&builder.charAt(0)==' ') {
            builder.delete(0,1);
        }
        while (builder.length()>0&&builder.charAt(builder.length()-1)==' ') {
            builder.delete(builder.length()-1,builder.length());
        }
        return builder.toString();
    }
}

