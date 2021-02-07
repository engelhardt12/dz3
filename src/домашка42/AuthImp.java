package домашка42;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;




public class AuthImp implements Auth{
    public Map<String,String> users= new HashMap<>();
    public AuthImp(){
        try{
            users=LoginPassw.mapLoginPass();
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean authUser(String username, String pass) {
        String pwd= users.get(username);
        return pwd!=null&&pwd.equals(pass);
    }
}
