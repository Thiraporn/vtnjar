package com.scap.sendmail.dao;

import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList; 
 







import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
 

import com.scap.sendmail.model.MstDoctor;
import com.scap.sendmail.util.DbConnector;
 

public class DoctorDao {
	
	
/* public static void main(String[] args) {
	 DoctorDao voDoctorDao = new DoctorDao();
	 try {
		 ArrayList<MstDoctor>  getDoctorPaymentDatatable = voDoctorDao.getDoctorPaymentDatatable("VTN01", "2015", "01");
	
		 
	 } catch (Exception e) {
		e.printStackTrace();
	}
	 
}*/
	
	public ArrayList<MstDoctor>  getDoctorPaymentDatatable(String hospitalCode, String yyyy, String mm) throws Exception {

		PreparedStatement ps = null; 
		ArrayList<MstDoctor> list = new ArrayList<MstDoctor>(); 

		final String SQL = "SELECT T1.DOCTOR_CODE, "
				+ "       T2.NAME_THAI, "
				+ "       T1.STATUS_MODIFY,"
				+ "       T2.EMAIL ,"
				+ "       T1.MM,"
				+ "       T1.YYYY "
				+ "FROM PAYMENT_MONTHLY T1 "
				+ "     LEFT JOIN DOCTOR T2 ON T1.HOSPITAL_CODE = T2.HOSPITAL_CODE "
				+ "                            AND T1.DOCTOR_CODE = T2.CODE "
				+ "WHERE T1.YYYY = ? " + "      AND T1.MM = ? "
				+ "      AND (T1.STATUS_MODIFY = '' "
				+ "           OR T1.STATUS_MODIFY IS NULL) "
				+ "      AND T1.HOSPITAL_CODE = ? "
				+ "      AND DR_NET_PAID_AMT > 0 "
				+ "      AND DOCTOR_CODE IN " + "( " + "    SELECT CODE "
				+ "    FROM DOCTOR " + "    WHERE EMAIL <> '' " + ");";
		MstDoctor voMstDoctor = null;
		try (Connection conn = DbConnector.getDBConnection()) {
			ps = conn.prepareStatement(SQL);
			ps.setString(1, yyyy);
			ps.setString(2, mm);
			ps.setString(3, hospitalCode);
			ResultSet rs = ps.executeQuery(); 
			ResultSetMetaData rsMetaData = rs.getMetaData();
			 while(rs.next()){
				 voMstDoctor = new MstDoctor(); 
					 voMstDoctor.setDoctorCode(rs.getString("DOCTOR_CODE"));
					 voMstDoctor.setDoctorNameTh(rs.getString("NAME_THAI"));
					 voMstDoctor.setEmail(rs.getString("EMAIL"));
					 
					 list.add(voMstDoctor);
				 
			 }
			//docorList = DbConnector.convertArrayListHashMap(rs)
			 
			 
			//System.out.println(list.get(0).getDoctorCode());
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null)
				ps.close();
		}

        return list;
	}
	
	public void SendMailPaymentSuccess(String hospitalCode, String doctorCode,String mm,String yyyy) throws SQLException{
		PreparedStatement ps = null;
		
		String SQL ="UPDATE PAYMENT_MONTHLY "
		           + "  SET "
		           + "      STATUS_MODIFY = 'T' "
		           + "WHERE DOCTOR_CODE = ? "
		           + " AND HOSPITAL_CODE = ? "
		           + " AND MM = ? "
		           + " AND YYYY = ? ";
		try (Connection conn = DbConnector.getDBConnection()) {
			
			ps = conn.prepareStatement(SQL);
			ps.setString(1, doctorCode);
			ps.setString(2, hospitalCode);
			ps.setString(3, mm);
			ps.setString(4, yyyy);
			
			ps.executeQuery();
		} catch (Exception e) {
			
		} finally {
			if (ps != null)
				ps.close();
		}
	}
	
	
	public ArrayList<HashMap<String, String>> getDoctorSendEmailPayment(String hospitalCode,String doctorCode, String yyyy, String mm) throws Exception {

		PreparedStatement ps = null;
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
		
		final String SQL ="SELECT T1.HOSPITAL_CODE, "
		           + "       T2.CODE AS DOCTOR_CODE, "
		           + "       COALESCE(NULLIF(T2.EMAIL,''),'0') EMAIL, "
		           + "       CASE "
		           + "           WHEN T2.LICENSE_ID = '' "
		           + "           THEN T2.CODE "
		           + "           ELSE T2.LICENSE_ID "
		           + "       END AS PASS_ENCRYPT, "
		           + "       T1.YYYY, "
		           + "       T1.MM, "
		           + "       T1.STATUS_MODIFY "
		           + "FROM PAYMENT_MONTHLY T1 "
		           + "     LEFT JOIN DOCTOR T2 ON T1.HOSPITAL_CODE = T2.HOSPITAL_CODE "
		           + "                            AND T1.DOCTOR_CODE = T2.CODE "
		           + "WHERE T1.HOSPITAL_CODE = ? "
		           + "		AND T2.CODE = ? "
		           + "		AND T1.YYYY = ? "
		           + "      AND T1.MM = ? "
		           + "      AND (T1.STATUS_MODIFY = '' OR T1.STATUS_MODIFY IS NULL); ";

		try (Connection conn = DbConnector.getDBConnection()) {
			ps = conn.prepareStatement(SQL);
			ps.setString(1, hospitalCode);
			ps.setString(2, doctorCode);
			ps.setString(3, yyyy);
			ps.setString(4, mm);
			data = DbConnector.convertArrayListHashMap(ps.executeQuery());
			System.out.println(data);
		} catch (Exception e) {
			throw e;
		} finally {
			if (ps != null)
				ps.close();
		}
		
		return data;
	}

}
