package org.example;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class Jdbc_mysqlTest {
    private static Connection sqlConnection ;


    public Connection getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }


    private static final Logger log = LogManager.getLogger(Jdbc_mysqlTest.class);
    @Test
    void test_connection ()throws SQLException{
         Connection connection = (Connection) new DatabaseConnection().initConnectionToBb(DatabaseType.MYSQL, "school_management","root", "123456");
       // log.debug("Connection is: ", getSqlConnection());
    }

    @BeforeAll
    public static void setUpEnviroment() throws  SQLException {
    //    Connection connection = (Connection) new DatabaseConnection().initConnectionToBb(DatabaseType.MYSQL,
     //           "school_management","root", "123456");
        sqlConnection = (Connection) new DatabaseConnection()
                .initConnectionToBb(
                        DatabaseType.MYSQL,
                        "school_management",
                        "root",
                        "123456"
                );
    }

    @Test
    void test_create_table() throws  SQLException{
        Statement statement = sqlConnection.createStatement();
        statement.execute("CREATE TABLE schools ( \n " +
                " school_id INT AUTO_INCREMENT PRIMARY KEY, \n " +
                " school_name VARCHAR(150) NOT NULL, \n " +
                "address VARCHAR(255), city VARCHAR(100), \n " +
                "state VARCHAR(50), postal_code VARCHAR(20), \n" +
                " phone_number VARCHAR(20), \n " +
                "email VARCHAR(100) UNIQUE, \n " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP );");
    }
    @Test
    void test_insert_into() throws  SQLException{
        // user prpareStatemant to void SL injecttion && increase prerformance
        String insertSql = "INSERT INTO schools (school_name, address, city, state, postal_code, phone_number, email)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?);\n";
        PreparedStatement  preparedStatement = sqlConnection.prepareStatement(insertSql);
        preparedStatement.setString(1, "ABC School");
        preparedStatement.setString(2, "123 Main Street");
        preparedStatement.setString(3, "Hanoi");
        preparedStatement.setString(4, "Hanoi");
        preparedStatement.setString(5, "100000");
        preparedStatement.setString(6, "0123456789");
        preparedStatement.setString(7, "abc@school.com");
        preparedStatement.execute();

    }
    @Test
    void    test_delete() throws  SQLException{
        String deleteSql = "Delete from schools where city = ? ";
        PreparedStatement statement = sqlConnection.prepareStatement(deleteSql);
        statement.setString(1,"Hanoi");
        statement.executeUpdate();
    }
    @Test
    void    test_update() throws  SQLException{
        String update = "Update schools set school_name = ? , email = ? ";
        PreparedStatement statement = sqlConnection.prepareStatement(update);
        statement.setString(1,"Havert ");
        statement.setString(2,"infor@gmail.com");
        statement.executeUpdate();
    }
    @Test
    void    test_query() throws SQLException{
        String query = " select  s.first_name , s.last_name , s.email, g.points_earned\n" +
                "from students s \n" +
                "inner join grades g on g.student_id = s.student_id ";
        Statement statement = sqlConnection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
             resultSet.getString("first_name");
             resultSet.getString("last_name");
             resultSet.getString("email");
             resultSet.getInt("points_earned");

        }
    }
    @AfterAll
    static  void tearDown() throws SQLException{
        sqlConnection.close();
    }

}
