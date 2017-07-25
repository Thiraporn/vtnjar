package com.scap.sendmail.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

import com.scap.sendmail.dao.BatchDao;
import com.scap.sendmail.dao.DoctorDao;
import com.scap.sendmail.model.MstDoctor;
import com.scap.sendmail.util.JDate;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

 

public class ScheduleRunnerSendMail {
	
	public static void main(String[] args) { 
		  ScheduleRunnerSendMail test = new ScheduleRunnerSendMail(); 
		   
	 
		try {
			 System.out.println("=========================== Begin of sending email  process ===================================");
				
			  test.doSendMail();
			  	
		     System.out.println("=========================== End of sending email  process ===================================");
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
 
	public void doSendMail() throws IOException {
		String message = "";
		PrepareFileToSendMailService prepareFile = new PrepareFileToSendMailService();
		SentEmailService sentEmail = new SentEmailService();
		ArrayList<HashMap<String, String>> arrData = null;
		ByteArrayOutputStream bosMergePdf = null;
		try {

			 
			String email = "";
			String doctorCode = "";
			String hospitalCode = "VTN01";
			BatchDao batchDao = new BatchDao();
			String mm = batchDao.getMonth(hospitalCode);
			String yyyy = batchDao.getYear(hospitalCode);
			
			System.out.println("=========Parameter==============");
			System.out.println("mm :: "+mm);
			System.out.println("yyyy :: "+yyyy);
			System.out.println("hospitalCode :: "+hospitalCode); 
			System.out.println("==================================");

			DoctorDao doctorDao = new DoctorDao();
			SentEmailService sentEmailService = new SentEmailService();

			// get list of Doctor
			ArrayList<MstDoctor> lstDoctor = sentEmailService.getLstDoctor(hospitalCode, yyyy,mm);
			if (lstDoctor.size() != 0 && lstDoctor != null) {
				for (int i = 0; i < lstDoctor.size(); i++) {
					InputStream jasperStream1 = getClass().getClassLoader().getResourceAsStream("PaymentVoucher.jasper");
					InputStream jasperStream2 = getClass().getClassLoader().getResourceAsStream("SummaryRevenueByDetail.jasper");
					InputStream jasperStream3 = getClass().getClassLoader().getResourceAsStream("ExpenseDetail.jasper");
					InputStream jasperStream4 = getClass().getClassLoader().getResourceAsStream("SummaryDFUnpaidByDetailAsOfDate.jasper");
				  
					String absoluteDiskPath = Paths.get("").toFile().getAbsolutePath()+"\\jasperReportFile"; 
					System.out.println("absoluteDiskPath "+absoluteDiskPath);
					
					 
			        
					JasperReport jasperReport1 = (JasperReport) JRLoader.loadObject(jasperStream1);
					JasperReport jasperReport2 = (JasperReport) JRLoader.loadObject(jasperStream2);
					JasperReport jasperReport3 = (JasperReport) JRLoader.loadObject(jasperStream3);
					JasperReport jasperReport4 = (JasperReport) JRLoader.loadObject(jasperStream4);
					
					
				 
					doctorCode = lstDoctor.get(i).getDoctorCode();
					//email = lstDoctor.get(i).getEmail().trim();
					arrData = doctorDao.getDoctorSendEmailPayment(hospitalCode,doctorCode, yyyy, mm);
					email = arrData.get(0).get("EMAIL").trim();

					System.out.println("Timing Get Doctor Sent Mail ==> " + JDate.getTime());

					if (!email.equals("0")) {

						int month = Integer.parseInt(mm);
						int year = Integer.parseInt(yyyy);
					 
                       bosMergePdf = prepareFile.PrepareMergePayment(arrData,
     								jasperReport1, jasperReport2, jasperReport3,
     								jasperReport4, jasperStream1, jasperStream2,
     								jasperStream3, jasperStream4, month, year,
     								absoluteDiskPath); 
						 
						System.out.println("Timing Create Report ==> "  + JDate.getTime());

						message = sentEmail.SendMailMergePdfFile(bosMergePdf, email);

						System.out.println("Timing Create Report ==> " + JDate.getTime());

						doctorDao.SendMailPaymentSuccess(hospitalCode, doctorCode, mm, yyyy);
					} else {
						message = "NO_DATA";
					}
					System.out.println("  ========================================================================================    ");
				}
				
			}

		} catch (Exception e) {
			message = "FAIL";
			e.printStackTrace();
		} finally {
			if (bosMergePdf != null)
				 
				bosMergePdf.close();
		     	 

		}

	}	
}
