package Utils;

import org.apache.thrift.TException;
import sos.Location;
import sos.User;
import sos.emergency_helper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kumar Ankit
 */
public class Server implements emergency_helper.Iface {
    @Override
    public void register_user(User user) {
        try {
            DBDelegate.register(user);
        } catch (Exception e){

        }

    }

    public static void main(String[]args) throws TException{
        Server sr = new Server();
        User user = new User();
        user.guid = "1111111";
        user.age=14;
        user.emergency_contact = "96638";
        user.emergency_name = "a";
        user.self_contact ="sss";
        user.gender="M";
        user.name = "abc";
        sr.register_user(user);

    }

    @Override
    public User getProfile(String guid) {
        User user = new User();
        try {
            user = DBDelegate.getProfile(guid);
        } catch (SQLException sqlEx) {
            //No-op
        }
        return user;
    }

    @Override
    public void uploadLocationData(String guid, List<Location> locationList) {
        try {
            DBDelegate.uploadLocationData(guid, locationList);
            System.out.println("Data successfully uploaded");
        } catch (Exception e) {
            System.out.println("Data not uploaded");
            e.printStackTrace();
        }
    }

    @Override
    public List<Location> getLocation(String guid) {
        //TODO:
        List<Location> locationList = new ArrayList<Location>();
        try {
            locationList = DBDelegate.getAllLocation(guid);
        } catch (Exception e) {
            //No-op
        }

        return locationList;
    }


    @Override
    public List<User> getAllUsersByname(String name) throws TException {
        List<User> users = new ArrayList<User>();
        try {
            users = DBDelegate.getAll(name);
        } catch (SQLException sqlEx) {
            //No-op
        }

        return users;
    }
}
