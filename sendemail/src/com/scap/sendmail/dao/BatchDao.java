package com.scap.sendmail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet; 

import com.scap.sendmail.util.DbConnector;
 

  
public class BatchDao {
	
	public String getYear(String hospitalCode) throws Exception {
		String value = "";
		PreparedStatement ps = null;
		
		final String SQL = "SELECT YYYY FROM BATCH WHERE HOSPITAL_CODE = ? AND (CLOSE_DATE = '' OR CLOSE_DATE IS NULL)";
		try (Connection conn = DbConnector.getDBConnection()) {
			ps = conn.prepareStatement(SQL);
			ps.setString(1, hospitalCode);
			ResultSet rs = ps.executeQuery();
			rs.next();
			value = rs.getString("YYYY");
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null) ps.close();
		}
		return value;
	}
	
	public String getMonth(String hospitalCode) throws Exception {
		String value = "";
		PreparedStatement ps = null;
		
		final String SQL = "SELECT MM FROM BATCH WHERE HOSPITAL_CODE = ? AND CLOSE_DATE = ''";
		try (Connection conn =  DbConnector.getDBConnection()) {
			ps = conn.prepareStatement(SQL);
			ps.setString(1, hospitalCode);
			ResultSet rs = ps.executeQuery();
			rs.next();
			value = rs.getString("MM");
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null) ps.close();
		}
		return value;
	}
	
	public Boolean isBatchClose(String hospitalCode, String year, String mount) throws Exception {
		int value = 0;
		PreparedStatement ps = null;
		
		final String SQL = "SELECT COUNT(*) COUNTDATA FROM BATCH WHERE HOSPITAL_CODE = ? AND YYYY = ? AND MM = ? AND CLOSE_DATE != ''";
		
		try (Connection conn = DbConnector.getDBConnection()) {
			ps = conn.prepareStatement(SQL);
			ps.setString(1, hospitalCode);
			ps.setString(2, year);
			ps.setString(3, mount);
			ResultSet rs = ps.executeQuery();
			rs.next();
			value = rs.getInt("COUNTDATA");
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null) ps.close();
		}
		return value == 0 ? true : false;
	}
	 
	
}
