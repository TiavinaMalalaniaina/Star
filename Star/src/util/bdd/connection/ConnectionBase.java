package util.bdd.connection;

import java.sql.*;

public class ConnectionBase {
// Field of ConnectionBase
    Connection co;
    Statement stmt;
    String url;
    String dbDriver;
    String user;
    String password;

// Function GET
    public Connection get_co() { return co; }
    public Statement get_stmt() { return stmt; }
    public String get_url() { return url; }
    public String get_dbDriver() { return dbDriver; }
    public String get_user() { return user; }
    public String get_password() { return password; }

// Function SET
    public void set_co(Connection co) { this.co = co; }
    public void set_stmt(Statement stmt) { this.stmt = stmt; }
    public void set_url(String url) { this.url = url; }
    public void set_dbDriver(String dbDriver) { this.dbDriver = dbDriver; }
    public void set_user(String user) { this.user = user; }
    public void set_password(String password) { this.password = password; }

// Constructor
    public ConnectionBase(String url, String dbDriver, String user, String password) {
        set_co(null);
        set_stmt(null);
        set_user(user);
        set_password(password);
        set_url(url);
        set_dbDriver(dbDriver);
    }

// Open and close connection
    //Open connection
    public void openConnection() throws Exception{
        if (get_co()!=null) {
            throw new Exception("Erreur de connexion: impossible de se connecter car la connection est deja ouverte");
        }
        Class.forName(dbDriver);
        set_co(DriverManager.getConnection(url, user, password));
    }

    //close the connection
    public void closeConnection() throws Exception {
        if (get_co()==null) {
            throw new Exception("Erreur de connexion: impossible de fermer la connexion puisqu'elle n'est pas encore ouverte");
        }
        get_co().close();
        set_co(null);
    }

    //create the statement
    public void creatingStatement() throws Exception{
        if (get_co()==null) {
            throw new Exception("Erreur de connexion: impossible de creer la statement puisque la connection n'est pas encore ouverte");
        }
        set_stmt(get_co().createStatement());
    }


    //open connection and create Statement
    public void preparingConnection() throws Exception {
        openConnection();
        creatingStatement();
    }   
}
