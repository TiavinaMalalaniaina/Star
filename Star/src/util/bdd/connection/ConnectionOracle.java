package util.bdd.connection;

public class ConnectionOracle extends ConnectionBase{

	/* CONSTRUCTOR */
	    public ConnectionOracle(String user, String password) {
	        super("jdbc:oracle:thin:@localhost:1521:DBTITI", "oracle.jdbc.driver.OracleDriver", user, password);
	    }
	}


