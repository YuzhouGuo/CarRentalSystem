
import java.sql.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;

public class CarRentalSystem {
    public static void main(String[] args) throws SQLException {
        // Unique table names. Either the user supplies a unique identifier as a command
        // line argument, or the program makes one up.
        String tableName = "";
        int sqlCode = 0; // Variable to hold SQLCODE
        String sqlState = "00000"; // Variable to hold SQLSTATE

        if (args.length > 0) {
            tableName += args[0];
        } else {
            tableName += "example3.tbl";
        }

        // Register the driver. You must register the driver before you can use it.
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (Exception cnfe) {
            System.out.println("Class not found");
        }

        // This is the url you must use for Postgresql.
        // Note: This url may not valid now !
        String url = "jdbc:postgresql://comp421.cs.mcgill.ca:5432/cs421";
        String usernamestring = "cs421g04";
        String passwordstring = "wearegroup4";
        Connection con = DriverManager.getConnection(url, usernamestring, passwordstring);
        Statement statement = con.createStatement();

        app();

        // Finally but importantly close the statement and connection
        statement.close();
        con.close();
    }

    private static void app(){

    }
}
