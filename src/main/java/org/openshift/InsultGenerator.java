package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class InsultGenerator {
	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult="";
		try {
			String mysqlHost=System.getenv("MYSQL_HOST");
			String mysqlDB=System.getenv("MYSQL_DATABASE");
			String mysqlUser=System.getenv("MYSQL_USER");
			String mysqlPassword=System.getenv("MYSQL_PASSWORD");
			String jdbcConnectString="jdbc:mariadb://"+mysqlHost+":3306/"+mysqlDB+"?user="+mysqlUser+"&password="+mysqlPassword;
			System.out.println("Connecting to mysql serever: "+jdbcConnectString);
			Connection connection = DriverManager.getConnection(jdbcConnectString);
			if (connection != null) {
				String SQL = "select a.string as first,b.string as second, c.string as noun from short_adjective a, long_adjective b, noun c order by rand() limit 1";
				Statement stmt = connection.createStatement();
				ResultSet rs=stmt.executeQuery(SQL);
				while(rs.next() ) {
					if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) article="a";
					theInsult=String.format("Thou art %s %s %s %s!", article,rs.getString("first"),rs.getString("second"),rs.getString("noun"));
				}
				rs.close();
				connection.close();
			}
		} catch(Exception e) {
			System.out.println("Exception on dbinit: "+e.getMessage());
			e.printStackTrace();
			return e.getMessage();
		}
		return theInsult;
	}

}
