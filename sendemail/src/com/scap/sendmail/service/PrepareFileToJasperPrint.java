package com.scap.sendmail.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map; 

import com.scap.sendmail.util.DbConnector;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

public class PrepareFileToJasperPrint {
	public JasperPrint PaymentVoucherReport(JasperReport jasperReport, String from_doctor, String to_doctor, String mm,
			String yyyy, String to_date, String absoluteDiskPath) throws JRException, SQLException {

		// Get Connection
		Connection conn = null;
		DbConnector con = new DbConnector();
		conn = con.getConnection();

		// Parameter Of Report
		Map<String, Object> params = new HashMap<>();
		params.put("from_doctor", from_doctor);
		params.put("to_doctor", to_doctor);
		params.put("month", mm);
		params.put("year", yyyy);
		params.put("from_date", "00000000");
		params.put("to_date", to_date);
		params.put("SUBREPORT_DIR", absoluteDiskPath); 
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		 
		conn.close();

		return jasperPrint;

	}

	public JasperPrint SummaryRevenueByDetail(JasperReport jasperReport, String hospitalCode, String from_doctor,
			String to_doctor, String mm, String yyyy) throws JRException, SQLException {

		// Get Connection
		Connection conn = null;
		DbConnector con = new DbConnector();
		conn = con.getConnection();

		// Parameter Of Report
		Map<String, Object> params = new HashMap<>();
		params.put("hospital_code", hospitalCode);
		params.put("from_doctor", from_doctor);
		params.put("to_doctor", to_doctor);
		params.put("month", mm);
		params.put("year", yyyy);
		params.put("doctor_category", "%%");
		params.put("doctor_department", "%%");
		params.put("order_item", "%%");
		params.put("order_item_category", "%%");

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		conn.close();
		return jasperPrint;

	}

	public JasperPrint SummaryDFUnpaidByDetailAsOfDate(JasperReport jasperReport, String to_date, String to_doctor,
			String hospitalCode) throws JRException, SQLException {
		// Get Connection
		Connection conn = null;
		DbConnector con = new DbConnector();
		conn = con.getConnection();

		// Parameter Of Report
		Map<String, Object> params = new HashMap<>();
		params.put("from_date", "00000000");
		params.put("to_date", to_date);
		params.put("doctor", to_doctor);
		params.put("hospital_code", hospitalCode);
		params.put("as_of_date", "%%");
		params.put("department_code", "%%");
		params.put("payor_code", "%%");

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		conn.close();
		return jasperPrint;

	}

	public JasperPrint ExpenseDetail(JasperReport jasperReport, String hospitalCode, String from_doctor,
			String to_doctor, String mm, String yyyy) throws JRException, SQLException {
		// Get Connection
		Connection conn = null;
		DbConnector con = new DbConnector();
		conn = con.getConnection();

		// Parameter Of Report
		Map<String, Object> params = new HashMap<>();
		params.put("hospital_code", hospitalCode);
		params.put("from_doctor", from_doctor);
		params.put("to_doctor", to_doctor);
		params.put("month", mm);
		params.put("year", yyyy);
		params.put("doctor_category", "%%");
		params.put("doctor_department", "%%");
		params.put("order_item", "%%");
		params.put("order_item_category", "%%");

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
		conn.close();
		return jasperPrint;
	}
}
