package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    public static Connection connection;
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/filehiderdb?useSSL=false", "root", "Akash@10");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Conncetion Established Successfully");
        return  connection;
    }

    public  static void closeConnection(){
        if(connection!=null){
            try {
                connection.close();
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        }
    }
}
