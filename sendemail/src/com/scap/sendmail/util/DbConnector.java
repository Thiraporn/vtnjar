package com.scap.sendmail.util; 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import java.util.TreeMap; 


 


public class DbConnector {
    private Connection conn;
    private String conn_class;
    private String conn_url;
    private String conn_user;
    private String conn_password;
    private Statement stm;
    private PreparedStatement pstm;
    
    
    
     public static void main(String[] args){ 
    	//System.out.println("=======connect=====   "+DbConnector.getDBConnection());  
    }  
    

    public DbConnector() {
    	ReadProperties prop = new ReadProperties();
       
        try {
        	Map<String, String>  propData = prop.getDataReadPropertiesFile("db.properties");
        	this.conn_url = propData.get("url");
        	this.conn_class = propData.get("driverClassName");
        	this.conn_user = propData.get("user");
        	this.conn_password = propData.get("password");       
            Class.forName(conn_class);
            conn = DriverManager.getConnection(conn_url,conn_user,conn_password);
            conn.setAutoCommit(false);
        } 
        catch (ClassNotFoundException e) { System.out.println(e); } 
        catch (SQLException e) { System.out.println(e); } 
        catch (Exception e) { System.out.println(e); }
    }
    
    public Connection getConnection(){
    	return this.conn;
    }
    public static Connection getDBConnection() {
		Connection connection = null;
		ReadProperties prop = new ReadProperties();
	       
        try {
        	Map<String, String>  propData = prop.getDataReadPropertiesFile("db.properties");
        	String conn_url = propData.get("url");
        	String conn_class = propData.get("driverClassName");
        	String conn_user = propData.get("user");
        	String conn_password = propData.get("password");       
            Class.forName(conn_class);
            connection = DriverManager.getConnection(conn_url,conn_user,conn_password);
            
        } 
        catch (ClassNotFoundException e) { System.out.println(e); } 
        catch (SQLException e) { System.out.println(e); } 
        catch (Exception e) { System.out.println(e); }
		return connection;
	}

    public void doPrepareConnect(String sql){
    	try {
			pstm = conn.prepareStatement(sql);
		} catch (SQLException e) {
			System.out.println(e);
		}
    }
    public void doConnect(){
    	try {
            stm = conn.createStatement();    	    		
    	} catch (SQLException e) {
			System.out.println(e);	
    	}
    }
    public void doDisconnect(){
    	try{
    		if(!stm.isClosed()){ stm.close(); }else{ /*not implement*/ }
    		if(!pstm.isClosed()){ pstm.close(); }else{ /*not implement*/ }
    		if(!conn.isClosed()){ conn.close(); }else{ /*not implement*/ }
    	} catch (Exception e){}
    }
    
    public void setPrepareStatement(String sql){
    	try { this.conn.prepareStatement(sql); } catch (SQLException e) {}
    }
    public PreparedStatement getPrepareStatement(){
    	return this.pstm;
    }
    
    public boolean doSave(String sql){
        try {
			stm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
    }
    public boolean doDelete(String sql){
        try {
			stm.executeUpdate(sql);
			return true;
		} catch (SQLException e) {
			System.out.println(e);
			return false;
		}
    }
    public boolean doCommit(){
        try {
            conn.commit();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }  
    public boolean doRollback(){
        try {
            conn.rollback();
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
     
    
	 
     
     

	public ArrayList<HashMap<String, Object>> getData() {
		//for prepare statement select
		ResultSet rs = null;		
		try {
			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
			rs = pstm.executeQuery();
			ResultSetMetaData md = rs.getMetaData();

			while (rs.next()) {
				HashMap<String, Object> map = new HashMap<String, Object>();

				for (int i = 1; i <= md.getColumnCount(); i++) {
					map.put(md.getColumnLabel(i), rs.getObject(i));
				}
				list.add(map);
			}
			return list;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
	//New version
	
	public static List<Map<String,String>> getData(ResultSet rs){
		List<Map<String,String>> lsQueryData = new ArrayList<Map<String,String>>();
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				Map<String, String> rtnData = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						rtnData.put(rsMetaData.getColumnName(i), value);
					} else {
						rtnData.put(rsMetaData.getColumnName(i), value);
					}
				}
				lsQueryData.add(rtnData);
			}
			rs.close(); 
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		return lsQueryData;
    }
	
	
	
    public ArrayList<HashMap<String,String>> getData(String sql) {
		ArrayList<HashMap<String,String>> lsQueryData = new ArrayList<HashMap<String,String>>();
		ResultSet rs = null;
		try {
			this.doConnect();
			rs = this.stm.executeQuery(sql);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rtnData = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						rtnData.put(rsMetaData.getColumnName(i), value);
					} else {
						rtnData.put(rsMetaData.getColumnName(i), value);
					}
				}
				lsQueryData.add(rtnData);
			}
			rs.close(); 
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		return lsQueryData;
	}
    public Map<String, Object[]> getDataMap(String sql) {
    	Map<String, Object[]> lsQueryData = new TreeMap<String, Object[]>();
		ResultSet rs = null;
		try {
			this.doConnect();
			rs = this.stm.executeQuery(sql);
			ResultSetMetaData rsMetaData = rs.getMetaData();
			
			// column name
			int count = rsMetaData.getColumnCount();
			
			Object[] metaColumn = new Object[count];
			for (int i = 1; i <= count; i++) {
				metaColumn[i - 1] = rsMetaData.getColumnLabel(i);
			}
			lsQueryData.put("1", metaColumn);
			
			int row = 2;
			while (rs.next()) {
				Object[] metaData = new Object[count];
				for (int i = 1; i <= count; i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						metaData[i - 1] = value;
					} else {
						metaData[i - 1] = value;
					}
				}
				lsQueryData.put(String.valueOf(row), metaData);
				row++;
			}
			rs.close(); 
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error " + e.getMessage());
		}
		return lsQueryData;
	}
    public String getDropDownList(String s){
    	return "";
    }
    public static ArrayList<HashMap<String, String>> convertArrayListHashMap (ResultSet rs) throws Exception{
		ArrayList<HashMap<String, String>> lsQueryData = new ArrayList<HashMap<String, String>>();
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			while (rs.next()) {
				HashMap<String, String> rtnData = new HashMap<String, String>();
				for (int i = 1; i <= rsMetaData.getColumnCount(); i++) {
					String value = "";
					if (rs.getString(i) != null && !rs.getString(i).equals("")) {
						value = rs.getString(i);
						rtnData.put(rsMetaData.getColumnName(i), value);
					} else {
						rtnData.put(rsMetaData.getColumnName(i), value);
					}
				}
				lsQueryData.add(rtnData);
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("Error " + e.getMessage());
		}
		return lsQueryData;
	}

     

    
}