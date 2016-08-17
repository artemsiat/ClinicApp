package tests;

import data_base.Administrators;
import data_base.DataBase;
import programm.Programm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Artem Siatchinov on 7/23/2016.
 */
public class DataBaseTest {

    public void loadConnection(){
        try {
            Class.forName("org.h2.Driver");

            //Created file in the same derictory as the jar file or project file
            Connection conn = DriverManager.getConnection("jdbc:h2:./testh2");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadRealDB(){
        DataBase dataBase = new DataBase(new Programm());
        Administrators admins = dataBase.getAdministrators();

        System.out.println("Checking if table is created ");
        System.out.println(admins.checkTable());

        System.out.println(admins.getStatus());
        System.out.println("Is table created ");
        System.out.println(admins.isTableCreated());



    }

}
