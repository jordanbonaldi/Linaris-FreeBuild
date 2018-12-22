package net.theuniverscraft.Freebuild.Managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbManager {
private Connection connection;
	
	private static final String BDD_VIP = "freebuild_vips";
	
	private static DbManager dbManager = null;
	
	public static DbManager getInstance() {
		if(dbManager == null) dbManager = new DbManager();
		return dbManager;
	}
	public static void closeInstance() {
		if(dbManager != null) {
			try { dbManager.connection.close(); }
			catch (Exception e) { e.printStackTrace(); }
		}
	}
	
	private DbManager() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://<ip>:3306/<db>";
			String user = "<user>";
			String password = "<password>";
			
			connection = DriverManager.getConnection(url, user, password);
			Statement state = connection.createStatement();
			
			String sql = new StringBuilder().append("CREATE TABLE IF NOT EXISTS `").append(BDD_VIP).append("` (")
					.append("`id` int(11) NOT NULL AUTO_INCREMENT,")
					.append("`pseudo` varchar(50) NOT NULL,")
					.append("`timestamp` bigint(20) NOT NULL,")
					.append("PRIMARY KEY (`id`),")
					.append("UNIQUE KEY `pseudo` (`pseudo`)")
					.append(") ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;").toString();
            
			state.executeUpdate(sql);
			
			state.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addVip(String player, long time) {
		try {
			isVip(player);
			String sql = "INSERT INTO "+BDD_VIP+"(pseudo, timestamp) VALUES(?, ?) "
					+ "ON DUPLICATE KEY UPDATE timestamp=timestamp+?";
			
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player);
			state.setLong(2, System.currentTimeMillis() + time);
			state.setLong(3, time);
			state.executeUpdate();
			
			state.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public void removeVip(String player) {
		try {
			String sql = "DELETE FROM "+BDD_VIP+" WHERE pseudo=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player);
			state.executeUpdate();
			
			state.close();
		}
		catch(Exception e) { e.printStackTrace(); }
	}
	
	public Boolean isVip(String player) {
		boolean vip = false;
		try {
			String sql = "SELECT * FROM "+BDD_VIP+" WHERE pseudo=?";
			PreparedStatement state = connection.prepareStatement(sql);
			
			state.setString(1, player);
			
			ResultSet result = state.executeQuery();
			if(result.next()) {
				if(result.getLong("timestamp") > System.currentTimeMillis()) {
					vip = true;
				}
				else {
					// Suppression
					PreparedStatement del = connection.prepareStatement("DELETE FROM "+BDD_VIP+" WHERE pseudo=?");
					
					del.setString(1, player);
					del.executeUpdate();
					
					del.close();
				}
			}
			
			state.close();
		}
		catch(Exception e) { e.printStackTrace(); }
		return vip;
	}
	
	public int getMaxZone(String player) {
		return isVip(player) ? 5 : 1;
	}
}
