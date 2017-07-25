package com.scap.sendmail.service;

import java.io.ByteArrayOutputStream; 
import java.io.InputStream; 
import java.util.ArrayList;
import java.util.HashMap; 
 



import com.scap.sendmail.util.JDate; 

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;


public class PrepareFileToSendMailService {

	 
	// Merge PDF 4 file For Send Mail
	public ByteArrayOutputStream PrepareMergePayment(ArrayList<HashMap<String, String>> arrData, JasperReport jasperReport1,JasperReport jasperReport2,JasperReport jasperReport3,JasperReport jasperReport4,
			InputStream jasperStream1,InputStream jasperStream2,InputStream jasperStream3,InputStream jasperStream4 ,int month,int year,String absoluteDiskPath) throws Exception{

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		JasperBuilderService voJasperBuilder = new JasperBuilderService();
		String password = arrData.get(0).get("PASS_ENCRYPT");
		String to_date = JDate.getLastDayOfMonth(year, month);
		
		String from_doctor = arrData.get(0).get("DOCTOR_CODE");
		String to_doctor = arrData.get(0).get("DOCTOR_CODE");
		String mm  = arrData.get(0).get("MM");
		String yyyy = arrData.get(0).get("YYYY");
		String hospitalCode = arrData.get(0).get("HOSPITAL_CODE");
		 
		PrepareFileToJasperPrint jasperPrint = new PrepareFileToJasperPrint(); 
		JasperPrint jpPaymentVoucherReport = jasperPrint.PaymentVoucherReport(jasperReport1, from_doctor, to_doctor, mm, yyyy, to_date, absoluteDiskPath); 
		JasperPrint jpSummaryRevenueByDetail = jasperPrint.SummaryRevenueByDetail(jasperReport2, hospitalCode, from_doctor, to_doctor, mm, yyyy);	 
		JasperPrint jpExpenseDetail = jasperPrint.ExpenseDetail(jasperReport3, hospitalCode, from_doctor, to_doctor, mm, yyyy);
		JasperPrint SummaryDFUnpaidByDetailAsOfDate = jasperPrint.SummaryDFUnpaidByDetailAsOfDate(jasperReport4, to_date, to_doctor, hospitalCode);

		 
		 bos = voJasperBuilder.jasperBuilderPdfEncryptMergePdf(jpPaymentVoucherReport,jpSummaryRevenueByDetail,jpExpenseDetail,SummaryDFUnpaidByDetailAsOfDate, "application/pdf","",password);
		 return bos;
	}

}
