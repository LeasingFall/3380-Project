import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class MontgomeryCrashesDB {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static void main(String[] args) {

        Properties prop = new Properties();
        String fileName = "auth.cfg";
        System.out.println("hello world");
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

        if (username == null || password == null) {
            System.out.println("Username or password not provided.");
            System.exit(1);
        }

        String connectionUrl = "jdbc:sqlserver://uranium.cs.umanitoba.ca:1433;"
                + "database=cs3380;"
                + "user=" + username + ";"
                + "password=" + password + ";"
                + "encrypt=false;"
                + "trustServerCertificate=false;"
                + "loginTimeout=30;";

        // ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            // Statement statement = connection.createStatement();
            MyDatabase db = new MyDatabase("MontgomeryCrashes.sql", connection); // Replace quotes with an sql file to
                                                                                           // fill entire database

            Scanner console = new Scanner(System.in);
            String line = "";
            Boolean validLogin = false;
            while (!validLogin && !line.equals("q")) {
                System.out.println(
                        "Welcome to Montgomery Police Record System!\nType u to login as a user or type p to login as a police.");
                System.out.print("db > ");
                line = console.nextLine();

                if (line.equals("p")) { // Police System Login
                    System.out.print("Welcome to Montgomery's Police System! Type h for help.");
                    System.out.print("db > ");
                    line = console.nextLine();
                    int defaultTop = 15;
                    String[] parts;
                    String arg = "";
                    String ar2 = "";
                    while (line != null && !line.equals("q")) {
                        // Get arguments
                        parts = line.split("\\s+");
                        if (parts.length == 2) {
                            arg = parts[1];
                        }
                        if (parts.length == 3) {
                            ar2 = parts[2];
                        }

                        // Commands
                        if (parts[0].equals("h")) {
                            printPoliceHelp();
                        } else if (parts[0].equals("tvc")) { // 1
                            db.ViolationsAndCollisions();
                        } else if (parts[0].equals("typeCar")) { // 2
                            try {
                                if (parts.length >= 2){
                                    if(Integer.parseInt(arg) <= 30 && Integer.parseInt(arg) >= 1){ // Validate between 1 and 30
                                        db.typeCarCollisions(Integer.parseInt(arg)); 
                                    } else {
                                        db.typeCarCollisions(defaultTop); 
                                    }
                                }else
                                    System.out.println("Required an argument for this command!");
                            } catch (NumberFormatException e) {
                                System.out.println("top must be an integer");
                            }
                        } else if (parts[0].equals("injury")) { // 3
                            try {
                                if(parts.length < 3){
                                    System.out.println("Required an argument for this command (not enough arguments)");
                                } else if(!(arg.equals("FATAL INJURY") || arg.equals("NO APPARENT INJURY") || arg.equals("POSSIBLE INJURY") || arg.equals("SUSPECTED MINOR INJURY") || arg.equals("SUSPECTED SERIOUS INJURY"))){
                                    System.out.println("Invalid injury type!");
                                } else { // Right length and a correct injury type has been selected
                                    db.injuryByCollision(arg, Integer.parseInt(ar2));
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("top must be an integer");
                            }
                        } else if (parts[0].equals("nma")) { // 4
                            try {
                                if (parts.length >= 2){
                                    if(Integer.parseInt(arg) <= 30 && Integer.parseInt(arg) >= 1){ // Validate between 1 and 30
                                        db.nonMotoristActions(Integer.parseInt(arg));
                                    } else {
                                        db.nonMotoristActions(defaultTop); 
                                    }
                                }else
                                    System.out.println("Required an argument for this command!");
                            } catch (NumberFormatException e) {
                                System.out.println("top must be an integer");
                            }
                        } else if (parts[0].equals("street")) { // 5
                            db.collisionsPerStreet();
                        } else if (parts[0].equals("mc")) { // 6
                            db.multipleCarCollisions();
                        } else if (parts[0].equals("weather")) { // 7
                            db.weatherCollisions();
                        } else if (parts[0].equals("lighting")) { // 8
                            db.lightingCollisions();
                        } else if (parts[0].equals("surface")) { // 9
                            db.surfaceCollisions();
                        } else if (parts[0].equals("speed")) { // 10
                            try {
                                if (parts.length >= 2){
                                    if(Integer.parseInt(arg) >= 1 && Integer.parseInt(arg) <= 75 && Integer.parseInt(arg) % 5 == 0){ // Validate between 1 and 30
                                        db.collisionsPerSpeed(Integer.parseInt(arg));
                                    } else {
                                        System.out.println("Invalid speed! (type h for more info)");
                                    }
                                }else
                                    System.out.println("Required an argument for this command!");
                            } catch (NumberFormatException e) {
                                System.out.println("speed must be an integer");
                            }
                        } else if (parts[0].equals("dbl")) { // 11
                            if(parts.length < 2){
                                System.out.println("Required an argument for this command");
                            } else if (!(arg.equals("Dark - Lighted") || arg.equals("Dark - Not Lighted") || arg.equals("Dark - Unknown Lighting") || arg.equals("DARK UNKNOWN LIGHTING") || arg.equals("DARK LIGHTS ON") || arg.equals("DARK NO LIGHTS") || arg.equals("DAWN") || arg.equals("DAYLIGHT") || arg.equals("DUSK") || arg.equals("OTHER") || arg.equals("UNKNOWN"))){
                                System.out.println("Invalid lighting type (type h for more info)");
                            }else
                                db.distractionsbyLighting(arg);
                        } else if (parts[0].equals("month")) { // 12
                            try {
                                if (parts.length >= 2 && Integer.parseInt(arg) >= 0) // Invalid years >=0 would just return empty 
                                    db.collisionsPerMonth(Integer.parseInt(arg));
                                else
                                    System.out.println("Required an argument for this command or invalid argument");
                            } catch (NumberFormatException e) {
                                System.out.println("year must be an integer");
                            }
                        } else if (parts[0].equals("collisions")) { // 13
                            try {
                                if (parts.length >= 2 && Integer.parseInt(arg) >= 0)
                                    db.collisionReport(Integer.parseInt(arg));
                                else
                                    System.out.println("Required an argument for this command or invalid argument");
                            } catch (NumberFormatException e) {
                                System.out.println("pid must be an integer");
                            }
                        } else if (parts[0].equals("violations")) { // 14
                            try {
                                if (parts.length >= 2 && Integer.parseInt(arg) >= 0)
                                    db.trafficViolations(Integer.parseInt(arg));
                                else
                                    System.out.println("Required an argument for this command or invalid argument");
                            } catch (NumberFormatException e) {
                                System.out.println("pid must be an integer");
                            }
                        } else {
                            System.out.println("Type and enter h for help.");
                        }

                        System.out.print("db > ");
                        line = console.nextLine();
                    }
                    validLogin = true;
                } else if (line.equals("u")) { // Public User
                    System.out.print("Welcome to the Public User System! Type h for help. ");
                    System.out.print("db > ");
                    line = console.nextLine();
                    String[] parts;
                    String arg = "";
                    String ar2 = "";
                    String ar3 = "";
                    while (line != null && !line.equals("q")) {
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
                            printPublicHelp();
                        }
                        // TODO: Add user queries

                        System.out.print("db > ");
                        line = console.nextLine();
                    }
                    validLogin = true;
                }

            }

            console.close();
            db.shutdown();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void printPoliceHelp() {
        System.out.println("tvc - List all drivers involved in collisions who were also involved in traffic violations");
        System.out.println("typeCar [top#(1-30)] - Lists the top [number] type of cars that most often get into collisions");
        System.out.println("injury [see below] [top#(1-30)] - Lists the top [number] type of collisions associated with [injury severity]");
        System.out.println("\t['FATAL INJURY' / 'NO APPARENT INJURY' / 'POSSIBLE INJURY' / 'SUSPECTED MINOR INJURY' / 'SUSPECTED SERIOUS INJURY']");
        System.out.println("nma [top#(1-30)] - Lists the top [number] non-motorist movements/actions at the time of the collision");
        System.out.println("street - Lists the number of collisions on each street");
        System.out.println("mc - Lists collisions involving more than two cars");
        System.out.println("weather - Lists collisions for each weather condition");
        System.out.println("lighting - Lists collisions for each lighting");
        System.out.println("surface - Lists collisions for each surface condition");
        System.out.println("speed [0/5/10/.../75] - Lists all collisions with [speedLimit] and associated collision types, and number of collisions for that collision type under that [speedLimit] ");
        System.out.println("dbl [see below] - Lists all driver distractions with [lighting]");
        System.out.println("\t['Dark - Lighted' / 'Dark - Not Lighted' / 'Dark - Unknown Lighting' / 'DARK UNKNOWN LIGHTING' /\n 'DARK LIGHTS ON' / 'DARK NO LIGHTS' / 'DAWN' / 'DAYLIGHT' / 'DUSK' / 'OTHER' / 'UNKNOWN']");
        System.out.println("month [year as YYYY] - Lists number of collisions for each month in a given [year]");
        System.out.println("collisions [pid] - Lists collision reports for person [personName] between [date1] and [date2]");
        System.out.println("violations [pid] - Lists Traffic Violations for person [personName] between [date1] and [date2]");
        System.out.println("q - quit\n");
    }

    public static void printPublicHelp() {
        // TODO: Correct for public users
        System.out.println("tvc - List all drivers involved in collisions who were also involved in traffic violations");
        System.out.println("typeCar [top#(1-30)] - Lists the top [number] type of cars that most often get into collisions");
        System.out.println("injury [see below] [top#(1-30)] - Lists the top [number] type of collisions associated with [injury severity]");
        System.out.println("\t['FATAL INJURY' / 'NO APPARENT INJURY' / 'POSSIBLE INJURY' / 'SUSPECTED MINOR INJURY' / 'SUSPECTED SERIOUS INJURY']");
        System.out.println("nma [top#(1-30)] - Lists the top [number] non-motorist movements/actions at the time of the collision");
        System.out.println("street - Lists the number of collisions on each street");
        System.out.println("mc - Lists collisions involving more than two cars");
        System.out.println("weather - Lists collisions for each weather condition");
        System.out.println("lighting - Lists collisions for each lighting");
        System.out.println("surface - Lists collisions for each surface condition");
        System.out.println("speed [0/5/10/.../75] - Lists all collisions with [speedLimit] and associated collision types, and number of collisions for that collision type under that [speedLimit] ");
        System.out.println("dbl [see below] - Lists all driver distractions with [lighting]");
        System.out.println("\t['Dark - Lighted' / 'Dark - Not Lighted' / 'Dark - Unknown Lighting' / 'DARK UNKNOWN LIGHTING' /\n 'DARK LIGHTS ON' / 'DARK NO LIGHTS' / 'DAWN' / 'DAYLIGHT' / 'DUSK' / 'OTHER' / 'UNKNOWN']");
        System.out.println("month [year as YYYY] - Lists number of collisions for each month in a given [year]");
        System.out.println("collisions [pid] - Lists collision reports for person [personName] between [date1] and [date2]");
        System.out.println("violations [pid] - Lists Traffic Violations for person [personName] between [date1] and [date2]");
        System.out.println("q - quit\n");
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

    // ------------- Queries Are Here ----------------

    // 1
    public void ViolationsAndCollisions() {
        // TODO!
        try {

            String sql = "Select distinct pid from driver natural join trafficViolations natural join collision limit 30;";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println(resultSet.getInt("pid"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    // 2
    public void typeCarCollisions(int top) {
        // TODO!
        try {
            String sql = "Select bodyType, make, count(cid) as numCollisions from vehicle natural join collision group By bodyType,make order by numCollisions desc limit ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, top);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "bodyType: " + resultSet.getString("bodyType") + "make: " + resultSet.getString("make")
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 3
    public void injuryByCollision(String severity, int top) { // Severity should be validated before passing to the
                                                              // function
        // TODO!

        try {
            String sql = "Select distinct collisionType, injurySeverity from collision join driver on collision.pid = Driver.pid join relatedWith on collision.CID = relatedWith.cid join nonMotorists on relatedWith.pid = nonMotorist.pid where injurySeverity = ? group By injurySeverity, collisionType order by count(collision.CID) desc limit ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, severity);
            statement.setInt(2, top);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "collisionType: " + resultSet.getString("collisionType") + "injurySeverity: "
                                + resultSet.getString("injurySeverity"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 4
    public void nonMotoristActions(int top) {
        // TODO!
        try {
            String sql = "select distinct actions, movement, count(collision.cid) as numCollisions from collision natural join relatedWith natural join nonMotorists group by actions,movement order by numCollisions desc limit ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, top);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "actions: " + resultSet.getString("actions") + "movement: "
                                + resultSet.getString("movement") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 5
    public void collisionsPerStreet() {
        try {
            String sql = "select roadName, count(collision.CID) as numCollisions from collision natural join location group By roadName order by numCollisions desc limit 30;";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "roadName: " + resultSet.getString("roadName") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 6
    public void multipleCarCollisions() {
        // TODO!
        try {
            String sql = "select distinct reportNumber, count(distinct collision.vid) as numCollisions from report natural join collision group by reportNumber having numCollisions > 2;";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "reportNumber: " + resultSet.getString("reportNumber") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    // 7
    public void weatherCollisions() {
        // TODO!
        try {
            String sql = "select distinct weather, count(collision.CID) as numCollisions from environmentalConditions natural join has natural join location natural join collision group by weather; ";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "Weather: " + resultSet.getString("weather") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    // 8
    public void lightingCollisions() {
        // TODO!
        try {
            String sql = "select distinct lightCondition, count(collision.CID) as numCollisions from environmentalConditions natural join has natural join location natural join collision group by lightCondition;";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "lightCondition: " + resultSet.getString("lightCondition") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 9
    public void surfaceCollisions() {
        // TODO!
        try {
            String sql = "select distinct surfaceCondition, count(collision.cid) as numCollisions from environmentalConditions natural join has natural join location natural join collision group by surfaceCondition; ";

            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "surfaceCondition: " + resultSet.getString("surfaceCondition") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 10
    public void collisionsPerSpeed(int speedLimit) {
        // TODO!
        try {
            String sql = "select distinct collisionType, count(collision.CID) as numCollisions where speedLimit = ? group by collisionType order by numCollisions desc;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, speedLimit);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "collisionType: " + resultSet.getString("collisionType") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }

    }

    // 11
    public void distractionsbyLighting(String lighting) { // Avoid injection
        // TODO!
        try {
            String sql = "select distractedBy, count(collision.cid) as numCollisions from collision natural join driver natural join location natural join has natural join environmentalConditions where environmentalConditions.lightCondition = ? group by distractedBy desc;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, lighting);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "distractedBy: " + resultSet.getString("distractedBy") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 12
    public void collisionsPerMonth(int year) {
        // TODO!
        try {
            String sql = "select month, count(collisionID) as numCollisions from collisions group by month order by numCollisions having year = ?;";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, year);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "month: " + resultSet.getString("month") + "numCollisions: "
                                + resultSet.getInt("numCollisions"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 13
    public void collisionReport(int PID) {
        // TODO!
        try {
            String sql = "select * from collision where pid = ? and crashDate between ? and ? limit 30; ";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, PID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "crashDate: " + resultSet.getString("crashDate") + "crashTime: "
                                + resultSet.getString("crashTime") + " firstImpactLocation: "
                                + resultSet.getString("firstImpactLocation") + " damageExtent: "
                                + resultSet.getString("damageExtent") + " collisionType: "
                                + resultSet.getString("collisionType") + " reportNumber: "
                                + resultSet.getInt("reportNumber"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

    // 14
    public void trafficViolations(int PID) {
        // TODO!
        try {
            String sql = "select * from trafficViolations where pid = ? and date between ? and ? limit 30; ";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, PID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {

                System.out.println(
                        "date: " + resultSet.getString("date") + " description: " + resultSet.getString("description") + " outcome: " + resultSet.getString("outcome"));

            }
            resultSet.close();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace(System.out);
        }
    }

}
