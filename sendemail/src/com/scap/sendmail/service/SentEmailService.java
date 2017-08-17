package com.scap.sendmail.service;

import java.io.ByteArrayOutputStream; 
import java.util.ArrayList; 
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource; 
 
 
import com.scap.sendmail.dao.DoctorDao;
import com.scap.sendmail.model.MstDoctor;
import com.scap.sendmail.util.JDate;
import com.scap.sendmail.util.ReadProperties; 
 

public class SentEmailService {

	 
	   //   Send Mail Merge Pdf 
		public String SendMailMergePdfFile(ByteArrayOutputStream pdfStream,String mail,String doctorCode) {
			ReadProperties prop = new ReadProperties();
			Map<String, String>  propData = prop.getDataReadPropertiesFile("servermail.properties");
			String auth_host = propData.get("auth_host");
			String auth_port = propData.get("auth_port");
			String auth_email = propData.get("auth_email");
			String auth_password = propData.get("auth_password");
			String mail_bcc = propData.get("mail_bcc");
			String msg = "";

			Properties props = new Properties();
			props.put("mail.smtp.host", auth_host);
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.socketFactory.port", auth_port);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", auth_port);
			
			String subject = propData.get("subject_payment");
			String body = propData.get("body_payment");

			try {

				Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(auth_email, auth_password);
					}
				});

				Message message = new MimeMessage(mailSession);

				message.setFrom(new InternetAddress(auth_email)); // From

				/*** Recipient ***/
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail)); // To
				message.setSubject(subject+" "+doctorCode);
				
				/*** Mail BCC ***/
				if(!mail_bcc.isEmpty()){
					System.out.println("BCC : "+mail_bcc);
					message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail_bcc,false));
				}
				
				System.out.println("Before Attachment ==>"+JDate.getTime());
				DataSource aAttachment = new ByteArrayDataSource(pdfStream.toByteArray(), "application/pdf");
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(body);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(new DataHandler(aAttachment));
				messageBodyPart.setFileName("DF_Payment_Report.pdf");
				multipart.addBodyPart(messageBodyPart);
				message.setContent(multipart);
				
				System.out.println("Before Attachment ==>"+JDate.getTime());
				
				Transport.send(message);
				
				msg = "PASS";
				System.out.println("Mail Send Successfully.=>"+JDate.getTime());

			} catch (MessagingException e) {
				msg = "FAIL";
			}
			return msg;
		}
	
	   // Multi  File Send
		public String SendMailMultiFile(ByteArrayOutputStream pdfStream1,ByteArrayOutputStream pdfStream2,ByteArrayOutputStream pdfStream3,ByteArrayOutputStream pdfStream4,String mail) {
			ReadProperties prop = new ReadProperties();
			Map<String, String>  propData = prop.getDataReadPropertiesFile("servermail.properties");
			String auth_host = propData.get("auth_host");
			String auth_port = propData.get("auth_port");
			String auth_email = propData.get("auth_email");
			String auth_password = propData.get("auth_password");
			String mail_bcc = propData.get("mail_bcc");
			
			String subject = propData.get("subject_payment");
			String body = propData.get("body_payment");
			
			String msg = "";

			Properties props = new Properties();
			props.put("mail.smtp.host", auth_host);
			props.put("mail.smtp.starttls.enable","true");
			props.put("mail.smtp.socketFactory.port", auth_port);
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", auth_port);

			try {

				Session mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(auth_email, auth_password);
					}
				});

				Message message = new MimeMessage(mailSession);

				message.setFrom(new InternetAddress(auth_email)); // From

				/*** Recipient ***/
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail)); // To
				message.setSubject(subject);
                //	message.setText("Hello mr.win, Please do not reply this mail");
				
				/*** Mail BCC ***/
				if(!mail_bcc.isEmpty()){
					System.out.println("BCC : "+mail_bcc);
					message.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail_bcc,false));
				}

				System.out.println("Before Attachment ==>"+JDate.getTime());
				
				// File 1 PaymentVoucher.jasper
				DataSource aAttachment = new ByteArrayDataSource(pdfStream1.toByteArray(), "application/pdf");
				BodyPart messageBodyPart = new MimeBodyPart();
				messageBodyPart.setText(body);
				Multipart multipart = new MimeMultipart();
				multipart.addBodyPart(messageBodyPart);
				messageBodyPart = new MimeBodyPart();
				messageBodyPart.setDataHandler(new DataHandler(aAttachment));
				messageBodyPart.setFileName("PaymentVoucher.pdf");
				multipart.addBodyPart(messageBodyPart);
				
				// File 2 SummaryRevenueByDetail.jasper
				DataSource aAttachment1 = new ByteArrayDataSource(pdfStream2.toByteArray(), "application/pdf");
				BodyPart messageBodyPart2 = new MimeBodyPart();
				messageBodyPart2 = new MimeBodyPart();
				messageBodyPart2.setDataHandler(new DataHandler(aAttachment1));
				messageBodyPart2.setFileName("SummaryRevenueByDetail.pdf");
				multipart.addBodyPart(messageBodyPart2);
				
				
				// File 3 ExpenseDetail.jasper
				DataSource aAttachment3 = new ByteArrayDataSource(pdfStream3.toByteArray(), "application/pdf");
				BodyPart messageBodyPart3 = new MimeBodyPart();
				messageBodyPart3 = new MimeBodyPart();
				messageBodyPart3.setDataHandler(new DataHandler(aAttachment3));
				messageBodyPart3.setFileName("ExpenseDetail.pdf");
				multipart.addBodyPart(messageBodyPart3);
				
				// File 4 SummaryDFUnpaidByDetailAsOfDate
				DataSource aAttachment4 = new ByteArrayDataSource(pdfStream4.toByteArray(), "application/pdf");
				BodyPart messageBodyPart4 = new MimeBodyPart();
				messageBodyPart4 = new MimeBodyPart();
				messageBodyPart4.setDataHandler(new DataHandler(aAttachment4));
				messageBodyPart4.setFileName("SummaryDFUnpaidByDetailAsOfDate.pdf");
				multipart.addBodyPart(messageBodyPart4);

				message.setContent(multipart);

				System.out.println("Before Attachment ==>"+JDate.getTime());
				
				Transport.send(message);
				
				msg = "PASS";
				System.out.println("Mail Send Successfully.=>"+JDate.getTime());

			} catch (MessagingException e) {
				msg = "FAIL";
			}
			return msg;
		}
		
 

   //get list of Doctor 	
	public ArrayList<MstDoctor> getLstDoctor(String hospitalCode,String yyyy,String mm){
		//get list of Doctor 
	    DoctorDao doctorDao = new DoctorDao(); 
	    ArrayList<MstDoctor>  lstDoctor  = null;
	    try {
	    	  lstDoctor = doctorDao.getDoctorPaymentDatatable(hospitalCode, yyyy, mm);
		} catch (Exception e) {
			lstDoctor = null;
		} 
	    return lstDoctor;
	}
}



