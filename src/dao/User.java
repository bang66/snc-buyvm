package dao;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class User {
    private static Logger log=Logger.getLogger(User.class);
    private String pid;
    private int account;

    public int getAccount(String pid) {
        String sql = "select account from player where pid="+pid;
        log.info("sql----->"+sql);
        int account=0;
        try {
            Connector conn=new Connector();
            ResultSet rs=null;
            PreparedStatement psm=conn.connect().prepareCall(sql);
            rs=psm.executeQuery();
            while (rs.next()) {
                account=rs.getInt("account");
            }
            log.info("mysql 查到的account:"+account);
            psm.close();
            conn.connect().close();

        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return account;
    }

    public void updateAccount(int account,String pid){
        String sql ="update player set account = "+account+" where id= '"+pid+"'";
        log.info("sql----->"+sql);
        PreparedStatement pstmt;
        int i;
        try {
            Connector conn=new Connector();
            Connection con=conn.connect();
            pstmt = con.prepareStatement(sql);
            i = pstmt.executeUpdate();
            log.info("mysql 修改account成功  pid:"+pid);
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }

    public void insertMessage( String pid,String cpu,String mem,String disk,String payDate){

        String sql = "insert into buyVm (pid,cpu,mem,disk,payDate) values(?,?,?,?,?)";
        log.info("sql----->"+sql);
        PreparedStatement pstmt;
        try {
            Connector conn=new Connector();
            Connection con=conn.connect();
            pstmt=(PreparedStatement) con.prepareStatement(sql);
            pstmt.setString(1, pid);
            pstmt.setString(2, cpu);
            pstmt.setString(3, mem);
            pstmt.setString(4, disk);
            pstmt.setString(5, payDate);
            pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }
}
