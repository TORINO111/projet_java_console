package dette.boutique.core.database.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dette.boutique.core.database.DataBase;

public abstract class DataBaseImpl<T> implements DataBase<T> {
    private final String URL = "jdbc:mysql://localhost:3306/dette_boutique";
    private final String USER = "root";
    private final String PASSWORD = "";
    protected Connection connection = null;
    protected PreparedStatement ps = null;

    @Override
    public Connection connexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("Driver JDBC non trouvé.");
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void deconnexion() throws SQLException {
        if (connection != null && connection.isClosed()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ResultSet executeQuery() throws SQLException {
        return ps.executeQuery();
    }

    @Override
    public int executeUpdate() throws SQLException {
        return ps.executeUpdate();
    }

    @Override
    public void init(String sql) throws SQLException {
        String sqlUpperCase = sql.toUpperCase().trim();

        if (sqlUpperCase.startsWith("INSERT")) {
            ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        } else {
            ps = connection.prepareStatement(sql);
        }
    }

}