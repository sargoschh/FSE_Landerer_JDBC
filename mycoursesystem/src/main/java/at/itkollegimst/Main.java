package at.itkollegimst;

import dataaccess.MysqlDatabaseConnection;
import ui.CLI;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/kurssystem";
        String user = "root";
        String pwd = "";

        CLI myCli = new CLI();
        myCli.start();

        try {
            Connection myConnection = MysqlDatabaseConnection.getConnection(url, user, pwd);
            System.out.println("Verbindung aufgebaut!");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }
}