package com.scap.sendmail.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.scap.sendmail.dao.DoctorDao;
import com.scap.sendmail.util.JDate;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

public class SendEmailMultiThread implements Runnable{

	String hospitalCode ;
	String doctorCode;
	String yyyy;
	String mm ;
	//int doctorNo;
	//String dateStamp;

	public SendEmailMultiThread(String hospitalCode,String doctorCode,String yyyy,String mm) throws Exception{
		
		this.hospitalCode  = hospitalCode;
		this.doctorCode = doctorCode;
		this.yyyy = yyyy; 
		this.mm = mm;
		
	} 
	
	//public SendEmailMultiThread(int doctorNo, String dateStamp) throws Exception{
		
		//this.doctorNo = doctorNo;
		//this.dateStamp = dateStamp;
	//}

	@Override
	public void run() {
		
		//System.out.println("Date time :" + dateStamp + " >>> Thread is running..." + doctorNo);
		ArrayList<HashMap<String, String>> arrData = null;
		String email = "";
		String message = "";
		PrepareFileToSendMailService prepareFile = new PrepareFileToSendMailService();
		SentEmailService sentEmail = new SentEmailService();
		ByteArrayOutputStream bosMergePdf = null;
		DoctorDao doctorDao = new DoctorDao(); 
		
		
		InputStream jasperStream1 = getClass().getClassLoader().getResourceAsStream("PaymentVoucher.jasper");
		InputStream jasperStream2 = getClass().getClassLoader().getResourceAsStream("SummaryRevenueByDetail.jasper");
		InputStream jasperStream3 = getClass().getClassLoader().getResourceAsStream("ExpenseDetail.jasper");
		InputStream jasperStream4 = getClass().getClassLoader().getResourceAsStream("SummaryDFUnpaidByDetailAsOfDate.jasper");
	  
		String absoluteDiskPath = Paths.get("").toFile().getAbsolutePath()+"\\jasperReportFile"; 
		System.out.println("absoluteDiskPath "+absoluteDiskPath);
		
		 
        
		JasperReport jasperReport1 = null;
		JasperReport jasperReport2= null;
		JasperReport jasperReport3= null;
		JasperReport jasperReport4= null;
		try {
			
			
			jasperReport4 = (JasperReport) JRLoader.loadObject(jasperStream4);
			jasperReport3 = (JasperReport) JRLoader.loadObject(jasperStream3);
			jasperReport2 = (JasperReport) JRLoader.loadObject(jasperStream2);
			jasperReport1 = (JasperReport) JRLoader.loadObject(jasperStream1);
		} catch (JRException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
		
		
	 
		//doctorCode = lstDoctor.get(i).getDoctorCode();
		//email = lstDoctor.get(i).getEmail().trim();
		try {
			arrData = doctorDao.getDoctorSendEmailPayment(hospitalCode,doctorCode, yyyy, mm);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		email = arrData.get(0).get("EMAIL").trim();

		System.out.println("Timing Get Doctor Sent Mail ==> " + JDate.getTime());

		if (!email.equals("0")) {

			int month = Integer.parseInt(mm);
			int year = Integer.parseInt(yyyy);
		 
           try {
			bosMergePdf = prepareFile.PrepareMergePayment(arrData,
								jasperReport1, jasperReport2, jasperReport3,
								jasperReport4, jasperStream1, jasperStream2,
								jasperStream3, jasperStream4, month, year,
								absoluteDiskPath);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
			 
			System.out.println("Timing Create Report ==> "  + JDate.getTime());

			message = sentEmail.SendMailMergePdfFile(bosMergePdf, email,doctorCode);

			System.out.println("Timing Create Report ==> " + JDate.getTime());

			try {
				if(message.equals("PASS")){
					doctorDao.SendMailPaymentSuccess(hospitalCode, doctorCode, mm, yyyy);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			message = "NO_DATA";
		}
		System.out.println("  ========================================================================================    ");
	
		
	}

}
