package dao;

import org.apache.log4j.Logger;

import java.sql.*;

public class Connector {
    private static Logger log=Logger.getLogger(Connector.class);
    private String driver="com.mysql.jdbc.Driver";
    private String uri="jdbc:mysql://localhost:3306/snc";
    private String name="root";
    private String passwd="123123";

    public Connection connect(){
        Connection conn=null;
        PreparedStatement psm=null;
        try {
            Class.forName(driver);
            conn=DriverManager.getConnection(uri,name,passwd);
            if (!conn.isClosed()){
//                System.out.println("mysql链接成功");
            }
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        return  conn;
    }
}
