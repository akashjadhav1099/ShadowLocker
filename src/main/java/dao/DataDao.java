package dao;

import db.MyConnection;
import model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataDao {
    public static List<Data> getAllFiles(String email) throws SQLException{
        Connection connection= MyConnection.getConnection();
        PreparedStatement ps= connection.prepareStatement("select * from data where email=?");
        ps.setString(1, email);
        ResultSet rs= ps.executeQuery();
        List<Data> files= new ArrayList<>();
        while(rs.next()){
            int id= rs.getInt(1);
            String name= rs.getString(2);
            String path= rs.getString(3);
            files.add(new Data(id, name, path));
        }
        return files;
    }

    public static int hideFile(Data file)throws SQLException, IOException {
        Connection connection = MyConnection.getConnection();
        PreparedStatement ps = connection.prepareStatement(
                "insert into data(name, path, email, bin_data) values(?,?,?,?)");
        ps.setString(1, file.getFileName());
        ps.setString(2, file.getPath());
        ps.setString(3, file.getEmail());

        int ans;
        try (FileReader fr = new FileReader(new File(file.getPath()))) {
            ps.setCharacterStream(4, fr, new File(file.getPath()).length());

            ans = ps.executeUpdate();
        } // The FileReader is automatically closed here

        // Now, attempt to delete the file
        File f = new File(file.getPath());
        boolean deleted = f.delete();


        if (!deleted) {
            System.out.println("Could not delete the file: " + file.getPath());
        }

        return ans;
    }

    public static void unhide(int id) throws SQLException, IOException{
        Connection connection= MyConnection.getConnection();
        PreparedStatement ps= connection.prepareStatement("select path, bin_data from data where id = ?");
        ps.setInt(1, id);
        ResultSet rs= ps.executeQuery();
        rs.next();
        String path= rs.getString("path");
        Clob c= rs.getClob("bin_data");

        Reader r= c.getCharacterStream();
        FileWriter fw= new FileWriter(path);
        int i;
        while ((i = r.read()) != -1) {
            fw.write((char) i);
        }
        fw.close();
        ps= connection.prepareStatement("delete from data where id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Successfully Unhidden");
    }
}
