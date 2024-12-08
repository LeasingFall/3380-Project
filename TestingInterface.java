import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;
import java.io.File;
import java.sql.PreparedStatement;

public class TestingInterface {
    public static void main(String[] args) {

        System.out.println("hello");
        String line = "";
        Scanner console = new Scanner(System.in);
        Boolean validLogin = false;
        while (!validLogin && !line.equals("q")) {
            System.out.println("Welcome! Type u to login as a user or type p to login as a police.");
            System.out.print("db > ");
            line = console.nextLine();

            if (line.equals("p")) {
                policeInterface(line, console);
                validLogin = true;
            } else if (line.equals("u")) {
                userInterface(line, console);
                validLogin = true;
            }

        }

    }

    public static void policeInterface(String line, Scanner console) {
        while (line != null && !line.equals("q")) {
            String[] parts;
            String arg = "";
            String ar2 = "";
            String ar3 = "";
            // Get arguments
            parts = line.split("\\s+");
            if (parts.length == 2) {
                arg = parts[1];
            }
            if (parts.length == 3) {
                ar2 = parts[2];
            }
            if (parts.length == 4) {
                ar3 = parts[3];
            }

            // Commands
            if (parts[0].equals("h")) {
                printPoliceHelp();
            } else if (parts[0].equals("mp")) {
                // db.Query1();
            } else if (parts[0].equals("s")) {
                if (parts.length >= 2)
                    // db.Query1();
                    System.out.println("Placeholder");
                else
                    System.out.println("Require an argument for this command");
            } else if (parts[0].equals("l")) {
                try {
                    if (parts.length >= 2)
                        System.out.println("Placeholder");
                    // db.lookupByID(arg);
                    else
                        System.out.println("Require an argument for this command");
                } catch (Exception e) {
                    System.out.println("id must be an integer");
                }
            } else if (parts[0].equals("sell")) {
                try {
                    if (parts.length >= 2)
                        System.out.println("Placeholder");
                    // db.lookupWhoSells(arg);
                    else
                        System.out.println("Require an argument for this command");
                } catch (Exception e) {
                    System.out.println("id must be an integer");
                }
            } else if (parts[0].equals("notsell")) {
                try {
                    if (parts.length >= 2)
                        System.out.println("Placeholder");
                    // db.whoDoesNotSell(arg);
                    else
                        System.out.println("Require an argument for this command");
                } catch (Exception e) {
                    System.out.println("id must be an integer");
                }
            } else if (parts[0].equals("mc")) {
                // db.mostCities();
            } else if (parts[0].equals("notread")) {
                // db.ownBooks();
            } else if (parts[0].equals("all")) {
                // db.readAll();
            } else if (parts[0].equals("mr")) {
                // db.mostReadPerCountry();
            } else {
                System.out.println("Type and enter h for help.");
            }

            System.out.print("db > ");
            line = console.nextLine();
        }
    }

    public static void printPoliceHelp() {
        System.out.println(
                "drivcolvil - List all drivers involved in collisions who were also involved in traffic violations");
        System.out.println("topcarcols - Lists the top [number] type of cars that most often get into collisions");
        System.out
                .println("topcolsinjury - Lists the top [number] type of collisions associated with [injury severity]");
        System.out.println(
                "topnonmact - Lists the top [number] non-motorist movements/actions at the time of the collision");
        System.out.println("numcolsstreet - Lists the number of collisions on each street");
        System.out.println("manycarcols - Lists collisions involving more than two cars"); 
        System.out.println("weathercols - Lists collisions for each weather condition");
        System.out.println("lightingcols - Lists collisions for each lighting");
        System.out.println("surfacecols - Lists collisions for each surface condition");
        System.out.println(
                "speedcols - Lists all collisions with [speedLimit] and associated collision types, and number of collisions for that collision type under that [speedLimit] ");
        System.out.println("distrlight - Lists all driver distractions with [lighting]");
        System.out.println("numcolsdate - Lists number of collisions for each month in a given [year]");
        System.out.println(
                "personcolreport - Lists collision reports for person [personName] between [date1] and [date2]");
        System.out.println(
                "persontrafreport - Lists Traffic Violations for person [personName] between [date1] and [date2]");
        System.out.println("q - quit");
        System.out.println();

    }

    public static void userInterface(String line, Scanner console) { // more
                                                                     // restricted
        // version
        String[] parts;
        String arg = "";
        String ar2 = "";
        String ar3 = "";

    }

    public static void printUserHelp() {

    }
}