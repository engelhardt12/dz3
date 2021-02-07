package домашка42;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LoginPassw {
    static{
        try{
            Class.forName("org.sqlite.JDBC");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    private static Connection connect;
    private static Statement stat;
    private static PreparedStatement updateLog;

    public static Map<String,String> mapLoginPass() throws ClassNotFoundException, SQLException{
        Map<String,String>stringStringMap=new HashMap<>();
        stringStringMap.put("","");
        try(Connection connect= DriverManager.getConnection("jdbc:sqlite:login_password.db")){
            stat=connect.createStatement();
            ResultSet resultSet=stat.executeQuery("Select * from login_password");
            while(resultSet.next()){
                String login=resultSet.getString("Login");
                String password=resultSet.getString("Password");
                stringStringMap.put(login,password);
            }
        }catch(Exception e){
            e.printStackTrace();

        }
        return stringStringMap;
    }
    public static void changeNick(String nick,String username) throws ClassNotFoundException{
        Map<String,String> stringStringMap=new HashMap<>();
        stringStringMap.put("","");
        try(Connection connect= DriverManager.getConnection("\"jdbc:sqlite:login_password.db")){
            updateLog=connect.prepareStatement("UPDATE login_password SET Login=? WHERE login=?");
            updateLog.setString(1,nick);
            updateLog.setString(2,username);
            System.out.println("\nUpdate rows: "+ updateLog.executeUpdate());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
