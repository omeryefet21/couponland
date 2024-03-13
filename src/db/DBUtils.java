package db;

import beans.Category;
import exceptions.CLInputException;
import exceptions.CLLogicException;
import local.CouponlandSystem;
import utilities.GeneralUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static db.QueryReturnType.CATEGORY;

public class DBUtils {

    private final ConnectionPool connectionPool;

    public DBUtils(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    /**
     * Returns a ResultSet produced by the query.
     *
     * @param sql SQL query to be executed.
     * @return a ResultSet object that contains the data produced by the query.
     * @throws CLLogicException if the operation was interrupted or if a connection could not be obtained.
     * @throws SQLException if a database access error occurs or the SQL statement does not return a ResultSet object.
     */
    public ResultSet runQueryFroResult(String sql) throws CLLogicException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return preparedStatement.executeQuery();
        } catch (InterruptedException exception) {
            throw new CLLogicException("Operation interrupted - " + exception.getMessage(), 498);
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    /**
     * Sets the received parameters and returns a ResultSet produced by the query.
     * @param sql SQL query to be executed.
     * @param params a map containing pairs of indexes and values to be used setting the query's values.
     * @return a ResultSet object that contains the data produced by the query.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * @throws SQLException if a database access error occurs or the SQL statement does not return a ResultSet object.
     */
    public ResultSet runQueryForResult(String sql, Map<Integer, Object> params) throws CLLogicException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            return setPreparedStatement(preparedStatement, params).executeQuery();
        } catch (InterruptedException exception) {
            throw new CLLogicException("Operation interrupted - " + exception.getMessage(), 498);
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    /**
     * Sets the received parameters and returns a row count produced by the query.
     * @param sql SQL query to be executed.
     * @param params a map containing pairs of indexes and values to be used setting the query's values.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * @throws SQLException if a database access error occurs or the SQL statement does not return a ResultSet object.
     */
    public Integer runQueryForUpdateCount(String sql, Map<Integer, Object> params) throws CLLogicException, SQLException {
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            if (params == null) {
                return preparedStatement.executeUpdate();
            } else {
                return setPreparedStatement(preparedStatement, params).executeUpdate();
            }
        } catch (InterruptedException exception) {
            System.out.println(exception.getMessage());
        } finally {
            connectionPool.returnConnection(connection);
        }
        return null;
    }

    /**
     * Sets the received parameters and returns a list of result objects produced by the query.
     * @param returnType Type of result objects contained by the returned list.
     * @param sql SQL query to be executed.
     * @param params a map containing pairs of indexes and values to be used setting the query's values.
     * @return a list containing the result objects produced by the query.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained or an error occurred setting the query's values.
     * @throws SQLException if a database access error occurs or the SQL statement does not return a ResultSet object.
     */
    public List<Object> runQueryForResultList(QueryReturnType returnType, String sql, Map<Integer, Object> params) throws CLInputException, CLLogicException, SQLException {
        return GeneralUtils.resultSetToList(returnType, runQueryForResult(sql, params));
    }

    /**
     * Returns a list of result objects produced by the query.
     * @param returnType Type of result objects contained by the returned list.
     * @param sql SQL query to be executed.
     * @return a list containing the result objects produced by the query.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted; if a connection could not be obtained.
     * @throws SQLException if a database access error occurs or the SQL statement does not return a ResultSet object.
     */
    public List<Object> runQueryForResultList(QueryReturnType returnType, String sql) throws CLInputException, CLLogicException, SQLException {
        return GeneralUtils.resultSetToList(returnType, runQueryFroResult(sql));
    }


    /**
     * Retrieves the current categories from DB and their id values.
     * @return a map containing categoryId values as keys and categories as values.
     * @throws CLInputException if an invalid input is used creating the result objects.
     * @throws CLLogicException if the operation was interrupted or if a connection could not be obtained.
     */
    public Map<Integer, Category> mapCurrentCategories() throws CLInputException, CLLogicException {
        List<Object> list;
        try {
            list = GeneralUtils.resultSetToList(CATEGORY, runQueryFroResult(CouponlandSystem.getInstance().getLoadedSqlCommands().GET_CATEGORIES));
        } catch (SQLException exception) {
            throw new CLLogicException("Unknown sql error: " + exception.getMessage(), 499);
        }
        return list.stream().reduce(new HashMap<>(), (map, object) -> {
                    if (object instanceof Integer) {
                        map.put((Integer) object, null);
                    } else {
                        map.put(map.keySet().stream().filter(key -> map.get(key) == null).findFirst().orElse(-1), Category.valueOf(((String) (object)).toUpperCase()));
                    }
                    return map;
                }
                , (a, b) -> a);
    }

    /**
     * Sets values for the received PreparedStatement instance and returns it.
     * @param preparedStatement a PreparedStatement instance.
     * @param params a map containing pairs of indexes and values to be used setting the PreparedStatement.
     * @return a PreparedStatement instance with set values.
     * @throws CLLogicException if an error occurred setting the values.
     */
    private PreparedStatement setPreparedStatement(PreparedStatement preparedStatement, Map<Integer, Object> params) throws CLLogicException {
        var ref = new Object() {
            SQLException exception = null;
        };
        params.forEach((key, value) -> {
            try {
                if (value == null) {
                    preparedStatement.setNull(key, Types.VARCHAR);
                } else if (value instanceof Integer) {
                    preparedStatement.setInt(key, (Integer) value);
                } else if (value instanceof String) {
                    preparedStatement.setString(key, String.valueOf(value));
                } else if (value instanceof Date) {
                    preparedStatement.setDate(key, (Date) value);
                } else if (value instanceof Double) {
                    preparedStatement.setDouble(key, (Double) value);
                } else if (value instanceof Boolean) {
                    preparedStatement.setBoolean(key, (Boolean) value);
                } else if (value instanceof Float) {
                    preparedStatement.setFloat(key, (Float) value);
                } else if (value instanceof Long) {
                    preparedStatement.setLong(key, (Long) value);
                }
            } catch (SQLException sqlException) {
                ref.exception = sqlException;
            }
        });
        if (ref.exception != null) {
            throw new CLLogicException("Unknown sql error: " + ref.exception.getMessage(), 499);
        }
        return preparedStatement;
    }
}