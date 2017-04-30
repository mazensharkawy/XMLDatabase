/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eg.edu.alexu.csd.oop.db;

import java.sql.Connection;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 *
 * @author HP-
 */
public class Driver implements java.sql.Driver{
    public static void main(String[] args) {
        String workingDirectory = "C://Users//HP-//Documents//NetBeansProjects//Database//src//eg//edu//alexu//csd//oop//db";
        try{
            Connection connection =new Driver().connect(workingDirectory, null);
            java.sql.Statement statement= connection.createStatement();
            System.out.println(statement.execute("CREATE TABLE test (Course varchar, Grade varchar , Marks int);"));
            System.out.println(statement.executeUpdate("INSERT INTO test (Course, Grade) VALUES (Programming_II,A);"));
            java.sql.ResultSet rSet = statement.executeQuery("SELECT (COURSE , GRADE) FROM test WHERE Marks < 50;");
            System.out.println("Done query @ Driver class");
            
            System.out.println(rSet.getObject(0));
        }catch(Exception e){System.out.println(e.toString());}
    }
    @Override
    public Connection connect(String string, Properties prprts) throws SQLException {
        
        return new eg.edu.alexu.csd.oop.db.Connection(string);
    }
    

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String string, Properties prprts) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean jdbcCompliant() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean acceptsURL(String string) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
