import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class MontgomeryCrashesDB {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {

        Properties prop = new Properties();
        String fileName = "auth.cfg";
        try {
            FileInputStream configFile = new FileInputStream(fileName);
            prop.load(configFile);
            configFile.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Could not find config file.");
            System.exit(1);
        } catch (IOException ex) {
            System.out.println("Error reading config file.");
            System.exit(1);
        }
        String username = (prop.getProperty("username"));
        String password = (prop.getProperty("password"));

        if (username == null || password == null){
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl =
                "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password="+ password +";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        //ResultSet resultSet = null;



        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            // Statement statement = connection.createStatement();
            MyDatabase db = new MyDatabase("MontgomeryCrashes.sql", connection); //Replace quotes with an sql file to fill entire database

            Scanner console = new Scanner(System.in);
            System.out.print("Welcome! Type h for help. ");
            System.out.print("db > ");
            String line = console.nextLine();
            String[] parts;
            String arg = "";
            String ar2 = "";
            String ar3 = "";
            
            while (line != null && !line.equals("q")) {
                //Get arguments
                parts = line.split("\\s+");
                if (parts.length == 2){
                    arg = parts[1];
                }
                if (parts.length == 3){
                    ar2 = parts[2];
                }
                if (parts.length == 4){
                    ar3 = parts[3];
                }
                
                // Commands
                switch (parts[0]) { // Using switch expressions
                    case "h" -> // help
                        //printHelp();
                        System.out.println("Placeholder");
                    case "tvc" -> // 1
                        db.ViolationsAndCollisions();
                    case "typeCar" -> {
                        // 2
                        try {
                            if (parts.length >= 2)
                                db.typeCarCollisions(Integer.parseInt(arg));
                            else
                                System.out.println("Required an argument for this command!");
                        } catch (NumberFormatException e) {
                            System.out.println("top must be an integer");
                        }
                    }
                    case "injury" -> {
                        // 3
                        try {
                            if (parts.length >= 3 && (arg.equals("") || arg.equals("") || arg.equals("") || arg.equals("")))
                                db.injuryByCollision(arg, Integer.parseInt(ar2));
                            else
                                System.out.println("Required an argument for this command");
                        } catch (NumberFormatException e) {
                            System.out.println("top must be an integer");
                        }
                    }
                    case "nma" -> {
                        // 4
                        try {
                            if (parts.length >= 2)
                                db.nonMotoristActions(Integer.parseInt(arg));
                            else
                                System.out.println("Required an argument for this command!");
                        } catch (NumberFormatException e) {
                            System.out.println("top must be an integer");
                        }
                    }
                    case "street" -> {
                        // 5
                        if (parts.length >= 2)
                            db.collisionsPerStreet(arg);
                        else
                            System.out.println("Required an argument for this command");
                    }
                    case "mc" -> // 6
                        db.multipleCarCollisions();
                    case "weather" -> // 7
                        db.weatherCollisions();
                    case "lighting" -> // 8
                        db.lightingCollisions();
                    case "surface" -> // 9
                        db.surfaceCollisions();
                    case "speed" -> {
                        // 10
                        try {
                            if (parts.length >= 2)
                                db.collisionsPerSpeed(Integer.parseInt(arg));
                            else
                                System.out.println("Required an argument for this command!");
                        } catch (NumberFormatException e) {
                            System.out.println("speed must be an integer");
                        }
                    }
                    case "dbl" -> {
                        // 11
                        if (parts.length >= 2)
                            db.distractionsbyLighting(arg); // Injection handed internally
                        else
                            System.out.println("Required an argument for this command");
                    }
                    case "month" -> {
                        // 12
                        try {
                            if (parts.length >= 2)
                                db.collisionsPerMonth(Integer.parseInt(arg));
                            else
                                System.out.println("Required an argument for this command");
                        } catch (NumberFormatException e) {
                            System.out.println("year must be an integer");
                        }
                    }
                    case "collisions" -> {
                        // 13
                        try {
                            if (parts.length >= 2)
                                db.collisionReport(Integer.parseInt(arg));
                            else
                                System.out.println("Required an argument for this command");
                        } catch (NumberFormatException e) {
                            System.out.println("pid must be an integer");
                        }
                    }
                    case "violations" -> {
                        // 14
                        try {
                            if (parts.length >= 2)
                                db.trafficViolations(Integer.parseInt(arg));
                            else
                                System.out.println("Required an argument for this command");
                        } catch (NumberFormatException e) {
                            System.out.println("pid must be an integer");
                        }
                    }
                    default -> System.out.println("Type and enter h for help.");
                }

                System.out.print("db > ");
                line = console.nextLine();
            }

            console.close();
            db.shutdown();
            
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

class MyDatabase {
    private Connection connection;

    public MyDatabase(String initScript, Connection c) {
        connection = null;
        try {
            connection = c;

            System.out.println("Connection to MS SQL has been established.");

            if (initScript != null)
                this.loadData(initScript);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        } catch (IOException fnf) {
            System.out.println(fnf.getMessage());
            System.exit(2);
        }

    }

    public void loadData(String script) throws IOException, SQLException {
        BufferedReader reader = new BufferedReader(new FileReader(script));
        String line = reader.readLine();
        // assumes each query is its own line
        while (line != null) {
            System.out.println(line);
            this.connection.createStatement().execute(line);
            line = reader.readLine();
        }
    }

    public void shutdown() {
        try {
            loadData("MontgomeryCrashes.sql"); // Replace quotes with an sql file to delete entire database
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------- Add Queries Here ----------------

      //1
    public void ViolationsAndCollisions() {
      // TODO!

    }

      //2
    public void typeCarCollisions(int top) {
          // TODO!
    }

      //3
    public void injuryByCollision(String severity, int top) { // Severity should be validated before passing to the function
      // TODO!
    }

      //4
    public void nonMotoristActions(int top) {
      // TODO!
    }

      //5
    public void collisionsPerStreet(String streetName) { // Avoid injection 
      // TODO!
    }

    //6
    public void multipleCarCollisions() {
        // TODO!
    }

    //7
    public void weatherCollisions() {
        // TODO!
    }

    //8
    public void lightingCollisions() {
        // TODO!
    }

    //9
    public void surfaceCollisions() {
        // TODO!
    }

    //10
    public void collisionsPerSpeed(int speedLimit) {
        // TODO!
    }

    //11
    public void distractionsbyLighting(String lighting) { // Avoid injection
        // TODO!
    }

    //12
    public void collisionsPerMonth(int year) {
        // TODO!
    }

    //13
    public void collisionReport(int PID) {
        // TODO!
    }

    //14
    public void trafficViolations(int PID) {
        // TODO!
    }

    
}
