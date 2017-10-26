import java.util.*;
import javax.activation.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.SimpleDateFormat;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;


public class Send_Email{
    private String from;
    private String to;
    
    private String subject;
    private String content;

    private String location;
    private String name;

    private String host;
    private String password;

    public void setAddress(String tfrom, String tto) {
        this.from = tfrom;
        this.to = tto;
    }

    public void setEmail(String tsubject, String tcontent){
	this.subject = tsubject;
	this.content = tcontent;
    }
    
    public void setAccess(String tlocation, String tname){
	this.location = tlocation;
	this.name = tname;
    }

    public void setHostPwd(String thost, String tpassword){
	this.host = thost;
	this.password = tpassword;
    }

    public void send(){
	Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);
	
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);

            Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(content);
            multipart.addBodyPart(contentPart);

            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(location);
            messageBodyPart.setDataHandler(new DataHandler(source));
            

            messageBodyPart.setFileName(name);
            
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
            message.saveChanges();

            Transport transport = session.getTransport("smtp");

            transport.connect(host, from, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static int file_type(String temp)
    {
    		if(temp.indexOf("jpeg")!=-1)
    			return 1;
    		if(temp.indexOf("m4v")!=-1)
    			return 2;
    		return 0;
    }
    
    public static void main(String[] args)  throws IOException,
	InterruptedException
    {
//		Send_Email cn = new Send_Email();
  
//		cn.setAddress("ronian_zhang@163.com", "zhangziyu900526@gmail.com");
//		cn.setEmail("A test email", "A test Content");
//		cn.setAccess("/Users/ronian/Documents/cs2610/1/fire2.m4v", "fire2.m4v");
//		cn.setHostPwd("smtp.163.com", "q1w2e3AA");
//		cn.send();
//    	
    		String director = "/Users/ronian/Documents/cs2610/1/";
    		Path faxFolder = Paths.get(director);
		WatchService watchService = FileSystems.getDefault().newWatchService();
		faxFolder.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

		boolean valid = true;
		do {
			WatchKey watchKey = watchService.take();

			for (WatchEvent event : watchKey.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				if (StandardWatchEventKinds.ENTRY_CREATE.equals(event.kind())) {
					String fileName = event.context().toString();
					
					System.out.println("File Created:" + fileName);
					
					Send_Email cn = new Send_Email();
				  
					cn.setAddress("ronian_zhang@163.com", "zhangziyu900526@gmail.com");
					//cn.setAccess(director+fileName, fileName);
//					cn.setEmail("A test email", "A test Conten\n");
					if( file_type(fileName) ==1)
					{
						cn.setEmail("Smoke Alert", "Smoke alert triggered\n");
						//cn.setEmail("A test email-S", "A test Content-S");
						System.out.println("111111\n" );
					}
					else if( file_type(fileName) ==2)
					{
						cn.setEmail("Fire Alert", "Fire alert triggered\n");
						//cn.setEmail("A test email-F", "A test Content-F");
						System.out.println("2222222\n" );
					}
					else if( file_type(fileName) ==0)
					{
						System.out.println("333333\n" );
						//cn.setEmail("No type", "asd\n");
						//cn.setEmail("No type", "The email have no type\n");
						cn.setEmail("A test email", "A test Content");
					}
					
					//cn.setAccess("/Users/ronian/Documents/cs2610/1/asdasd.m4v", "asdasd.m4v");
					cn.setAccess(director+fileName, fileName);
					cn.setHostPwd("smtp.163.com", "q1w2e3AA");
					
					
					//System.out.println("File Created:" + director+fileName);
					
					//SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");    
					//String datetime = tempDate.format(new java.util.Date());
					
					
					cn.send();
					
					  

				}
			}
			valid = watchKey.reset();

		} while (valid);

    }
}
