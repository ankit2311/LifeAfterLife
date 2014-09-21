package Utils;

import sos.Location;
import sos.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kumar Ankit
 */
public class DBDelegate {
    private static Connection connection;
    private static Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {

            System.out.println("Where is your PostgreSQL JDBC Driver? "
                    + "Include in your library path!");
            e.printStackTrace();

        }
        try {
            if(connection == null) {
                connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/lifeafterlife", "kumarankit", "abc123");
            }

            System.out.println("Obtaining connection");
        } catch (SQLException e) {

            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();

        }
        return connection;
    }

    private DBDelegate() {

    }
    public static void register(User user) throws SQLException {

        String sql1 = "select * from registered_users where guid=\'"+user.guid + "\';";
        Connection connection1 = DBDelegate.getConnection();
        PreparedStatement ps2 = connection1.prepareStatement(sql1);
        ResultSet rs = ps2.executeQuery();
        if(rs.next()) {
            System.out.println("The user already exists");
            return;
        }
        String insertTableSQL = "INSERT INTO registered_users"
                + "(guid, name, age, gender, self_contact, emergency_name, emergency_contact) values"
                + "(?,?,?,?,?,?,?);";
        System.out.println(insertTableSQL);
        System.out.println(user);
        System.out.println("Print: "+ user.guid);
        PreparedStatement ps = connection1.prepareStatement(insertTableSQL);
        System.out.println(user.guid);
        ps.setString(1,user.guid);
        System.out.println(user.guid);
        ps.setString(2, user.name);
        ps.setInt(3,user.age);
        System.out.println(user.age);
        ps.setString(4, user.gender);
        System.out.println(user.gender);
        ps.setString(5, user.self_contact);
        ps.setString(6, user.emergency_name);
        System.out.println(user.emergency_name);

        ps.setString(7, user.emergency_contact);
        System.out.println(user.emergency_contact);
        System.out.println(ps.toString());
        ps.execute();
    }

    public static void uploadLocationData(String guid, List<Location> locationList) throws SQLException{
        String [] loc = new String[locationList.size()];
        List<StringBuilder> listLoc = new ArrayList<StringBuilder>();
        int i=0;
        for(Location location : locationList) {
            StringBuilder sb = new StringBuilder(Double.valueOf(location.getLatitude()).toString());
            sb.append(":");
            sb.append(Double.valueOf(location.getLongitude()).toString());
            sb.append(":");
            sb.append(Long.valueOf(location.getTimestamp()).toString());
            loc[i++] = sb.toString();
        }

        //System.out.println(loc[1]);
        Connection connection1 = DBDelegate.getConnection();
        String query1 = "select * from user_location where guid = \'"+ guid + "\'";
        System.out.println(query1);
        PreparedStatement ps1 = connection1.prepareStatement(query1);
        ResultSet rs1 = ps1.executeQuery();
        if(!rs1.next()) {
            System.out.println("New Information");
            StringBuilder subquery1 = new StringBuilder();
            for(String location : loc) {
                subquery1.append(location);
                subquery1.append(",");
            }
            subquery1.deleteCharAt(subquery1.length()-1);
            System.out.println("Sub-query:" + subquery1);
            String query2 = "INSERT into user_location values(\'"+ guid + "\'" + "," +"\'{" +subquery1 +"}" +"\');";
            System.out.println(query2);
            PreparedStatement ps2 = connection1.prepareStatement(query2);
            ps2.execute();
        } else {
            System.out.println("data to be updated");
            Array array = rs1.getArray(2);
            String[] a = (String[])array.getArray();
            System.out.println(a[0]);

            StringBuilder subquery1 = new StringBuilder();
            for(String location : loc) {
                //subquery1.append("\'");
                subquery1.append(location);
                //subquery1.append("\'");
                subquery1.append(",");
            }

            for(String location : loc) {
                //subquery1.append("\'");
                subquery1.append(location);
                //subquery1.append("\'");
                subquery1.append(",");
            }
            subquery1.deleteCharAt(subquery1.length()-1);
            System.out.println(subquery1);
            String query2 = "update user_location set location_list=\'{"+subquery1 +"}\'" + " where guid=\'" + guid +"\';";
            System.out.println(query2);
            PreparedStatement ps3 = connection1.prepareStatement(query2);
            ps3.execute();

        }

    }

    public static User getProfile(String guid) throws SQLException {
        String query = "select * from registered_users where guid=\'" +guid+"\'";
        Connection connection1 = DBDelegate.getConnection();
        PreparedStatement ps = connection1.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        User user = new User();
        if(null != rs) {
            rs.next();
            user.gender = rs.getString("gender");
            user.name = rs.getString("name");
            user.age = rs.getInt("age");
            user.emergency_contact = rs.getString("emergency_contact");
            user.emergency_name = rs.getString("emergency_name");
        }

        return user;
    }

    public static List<User> getAll(String name) throws SQLException {
        String query = "select * from registered_users where name=\'"+name+"\'";
        Connection connection1 = DBDelegate.getConnection();
        PreparedStatement ps = connection1.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<User>();
        while(rs.next()) {
            User user = new User();
            user.gender = rs.getString("gender");
            user.name = rs.getString("name");
            user.age = rs.getInt("age");
            user.emergency_contact = rs.getString("emergency_contact");
            user.emergency_name = rs.getString("emergency_name");
            users.add(user);
        }
        return users;
    }

    public static List<Location> getAllLocation(String guid) throws SQLException{
        String query = "select * from user_location where guid=\'"+guid+"\'";
        System.out.println(query);
        Connection connection1 = DBDelegate.getConnection();
        PreparedStatement preparedStatement = connection1.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Location> locations = new ArrayList<Location>();
        if(resultSet.next()) {
            String[] strings = (String[])resultSet.getArray(2).getArray();
            for(String locationS : strings){
                System.out.println(locationS);
                String[] tokens = locationS.split(":");
                Location location = new Location();
                location.latitude = Double.valueOf(tokens[0]);
                location.longitude = Double.valueOf(tokens[1]);
                location.timestamp= Long.valueOf(tokens[2]);
                locations.add(location);
            }
        } else {
            System.out.println("No location-record found");
        }

        return locations;
    }
}
