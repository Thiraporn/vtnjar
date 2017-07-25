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
	    //File resourcesDirectory = new File("jasperReportFile/SummaryDFUnpaidSubreport.jasper");
		//ClassLoader loader = jasperReportFile.class.getClassLoader();
       // System.out.println(resourcesDirectory.getAbsolutePath());
		
		  
		
		  ScheduleRunnerSendMail test = new ScheduleRunnerSendMail();
		 /*URL location = ScheduleRunnerSendMail.class.getProtectionDomain().getCodeSource().getLocation();
		 String path  = location.getPath().substring(1, location.getPath().length()-1).replace("/", "\\");
	     System.out.println(location.getPath().substring(1, location.getPath().length()-1).replace("/", "\\"));*/
		  
		  String absoluteDiskPath = Paths.get("").toFile().getAbsolutePath()+"\\jasperReport"; 
			System.out.println("absoluteDiskPath "+absoluteDiskPath); 
			
			
			/*File file = new File(absoluteDiskPath);
			if (!file.isDirectory())
			   file = file.getParentFile();
			if (file.exists()){
				System.out.println("exist file  "+absoluteDiskPath);
			}*/
		  
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

			DoctorDao doctorDao = new DoctorDao();
			SentEmailService sentEmailService = new SentEmailService();

			// get list of Doctor
			ArrayList<MstDoctor> lstDoctor = sentEmailService.getLstDoctor(hospitalCode, "2015", "01");
			if (lstDoctor.size() != 0 && lstDoctor != null) {
				for (int i = 0; i < lstDoctor.size(); i++) {
					InputStream jasperStream1 = getClass().getClassLoader().getResourceAsStream("PaymentVoucher.jasper");
					InputStream jasperStream2 = getClass().getClassLoader().getResourceAsStream("SummaryRevenueByDetail.jasper");
					InputStream jasperStream3 = getClass().getClassLoader().getResourceAsStream("ExpenseDetail.jasper");
					InputStream jasperStream4 = getClass().getClassLoader().getResourceAsStream("SummaryDFUnpaidByDetailAsOfDate.jasper");
				 

                    ////////ok 
					//File resourcesDirectory = new File(getClass().getClassLoader()+"");
					String absoluteDiskPath = Paths.get("").toFile().getAbsolutePath()+"\\jasperReportFile"; 
					System.out.println("absoluteDiskPath "+absoluteDiskPath);
					
					
					
					/*File f = new File(absoluteDiskPath+"\\SummaryDFUnpaidSubreport.jasper");
					System.out.println(" path file "+f);
					System.out.println(" getAbsoluteFile   " + f.getAbsoluteFile());
					System.out.println(" path   " + f.getPath());*/
					
					
					//String absoluteDiskPath =   resourcesDirectory.getAbsolutePath(); 
					//System.out.println("absoluteDiskPath "+absoluteDiskPath);
					
					//URL resource = ScheduleRunnerSendMail.class.getResource("/SummaryDFUnpaidSubreport.jasper");
					 
					/*String absoluteDiskPath2 = resourcesDirectory.getAbsoluteFile().getAbsolutePath(); 
					System.out.println("absoluteDiskPath2 "+absoluteDiskPath2); */
					// File resourcesDirectory = new File(getClass().getClassLoader()+"/SummaryDFUnpaidSubreport.jasper");
					 
					 
					
					/*
					//okk
					File file = new File(Paths.get("").toFile().getAbsolutePath()+"\\jasperReportFile\\SummaryDFUnpaidSubreport.jasper");
				    String absoluteDiskPath = file.getAbsolutePath().replace("\\SummaryDFUnpaidSubreport.jasper", "");
					System.out.println(absoluteDiskPath);
		*/
					
				 
									 
					/*String absoluteDiskPath = Paths.get("").toFile().getAbsolutePath()+"\\jasperReportFile";  
					System.out.println("absoluteDiskPath "+absoluteDiskPath); */
					
					//File f = new File(absoluteDiskPath+"\\SummaryDFUnpaidSubreport.jasper");
					/*if(f.exists() && !f.isDirectory()) { 
					    System.out.println("exists \\SummaryDFUnpaidSubreport.jasper");
					}*/
					//absoluteDiskPath = f.getAbsolutePath();
				  
				
			        
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
