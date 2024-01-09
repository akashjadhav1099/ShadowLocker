package dao;

import db.MyConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public static boolean isExists(String email) throws SQLException{
        Connection connection= MyConnection.getConnection();
        PreparedStatement ps= connection.prepareStatement("select email from user");
        ResultSet rs= ps.executeQuery();
        while(rs.next()){
            String s= rs.getString(1);
            if(s.equals(email)){
                return true;
            }
        }
        return false;
    }

    public static int saveUser(User user) throws SQLException{
        //1 insertion successful
        //0 insertion not successful
        Connection connection= MyConnection.getConnection();
        PreparedStatement ps= connection.prepareStatement("insert into user value(default, ?, ?)");
        ps.setString(1, user.getName());
        ps.setString(2, user.getEmail());
        return ps.executeUpdate();
    }
}
