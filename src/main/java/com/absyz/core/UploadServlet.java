package main.java.com.absyz.core;

import java.io.*;
import java.util.*;
 import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;
import java.util.List;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import main.java.com.absyz.service.Products;

public class UploadServlet extends HttpServlet {
   
   private boolean isMultipart;
   private String filePath;
   private int maxFileSize = 500 * 1024;
   private int maxMemSize = 4 * 1024;
   private File file ;

   public void init( ){
      // Get the file location where it would be stored.
      //filePath = getServletContext().getInitParameter("file-upload"); 
	  // filePath = "C:\\Files\\";
	  filePath = "WebContent/images/";
	   AWSCredentials credentials = new BasicAWSCredentials(
				"AKIAIQT7Y5EDYZQFWTBA", 
				"kd7Qb/ML/NVubxjVd+EGZJlHincFI5AGnLTjVW+P");
		
		// create a client connection based on credentials
		AmazonS3 s3client = new AmazonS3Client(credentials);
		
		// create bucket - name must be unique for all S3 users
		String bucketName = "javatutorial-net-example-bucket";
		s3client.createBucket(bucketName);
		
		// list buckets
		for (Bucket bucket : s3client.listBuckets()) {
			System.out.println(" - " + bucket.getName());
		}
		
		// create folder into bucket
		String folderName = "testfolder";
		createFolder(bucketName, folderName, s3client);
		
		// upload file to folder and set it to public
		String fileName = folderName + SUFFIX + "testvideo.mp4";
		s3client.putObject(new PutObjectRequest(bucketName, fileName, 
				new File("C:\\Users\\vishwasamudralogo.PNG"))
				.withCannedAcl(CannedAccessControlList.PublicRead));
		
		
   }
   
   public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {
	   System.out.println("inside file upload");
	   System.out.println(request.getParameter("txtPname"));
      // Check that we have a file upload request
      isMultipart = ServletFileUpload.isMultipartContent(request);
      response.setContentType("text/html");
      java.io.PrintWriter out = response.getWriter( );
      String strPname = "";
      String strBname ="";
      String strFilename = "";
      int intPrice = 0;
      int intCount = 0;
      String relativeWebPath = "/images";
      String absoluteDiskPath = getServletContext().getRealPath(relativeWebPath);
      
      if( !isMultipart ) {
         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
         out.println("<p>No file uploaded</p>"); 
         out.println("</body>");
         out.println("</html>");
         return;
      }
  
      DiskFileItemFactory factory = new DiskFileItemFactory();
   
      // maximum size that will be stored in memory
      factory.setSizeThreshold(maxMemSize);
   
      // Location to save data that is larger than maxMemSize.
      factory.setRepository(new File("WebContent/images"));

      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
   
      // maximum file size to be uploaded.
      upload.setSizeMax( maxFileSize );

      try { 
         // Parse the request to get file items.
         List fileItems = upload.parseRequest(request);
	
         // Process the uploaded file items
         Iterator i = fileItems.iterator();

         out.println("<html>");
         out.println("<head>");
         out.println("<title>Servlet upload</title>");  
         out.println("</head>");
         out.println("<body>");
   
         while ( i.hasNext () ) 
         {
            FileItem fi = (FileItem)i.next();
            if ( !fi.isFormField () ) {
               // Get the uploaded file parameters
               String fieldName = fi.getFieldName();
               String fileName = fi.getName();
               String contentType = fi.getContentType();
               boolean isInMemory = fi.isInMemory();
               long sizeInBytes = fi.getSize();
               
            System.out.println(fileName);
               // Write the file
//               if( fileName.lastIndexOf("\\") >= 0 ) {
//                  file = new File( filePath + fileName.substring( fileName.lastIndexOf("\\"))) ;
//               } else {
//                  file = new File( filePath + fileName.substring(fileName.lastIndexOf("\\")+1)) ;
//               }
            System.out.println(absoluteDiskPath);
            System.out.println(filePath);
               File file = new File(filePath, fileName);
               strFilename = fileName;
               fi.write( file ) ;
               out.println("Uploaded Filename: " + fileName + "<br>");
            }
            if (fi.isFormField()) { 
          		System.out.println("I am inside fields"); 
          	 
          	if (fi.getFieldName().equals("txtPname")) {
        		strPname = fi.getString();
          	}
          	else if (fi.getFieldName().equals("txtBname")) {
          	  	strBname = fi.getString();
          	}
          	else if (fi.getFieldName().equals("txtPcount")) {
          	  	intCount = Integer.parseInt(fi.getString());
          	}
          	else if (fi.getFieldName().equals("txtPrice")) {
          	  	intPrice = Integer.parseInt(fi.getString());
          	}
          	}
         }
         System.out.println(strPname+","+strBname+","+intCount+","+intPrice+","+strFilename);
         String strOutput = Products.add_products_withimage(strPname, strBname, strFilename, intCount, intPrice);
         response.sendRedirect("addproduct.html");
         out.println("</body>");
         out.println("</html>");
         } catch(Exception ex) {
            System.out.println(ex);
         }
      }
      
      public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, java.io.IOException {

         throw new ServletException("GET method used with " +
            getClass( ).getName( )+": POST method required.");
      }
	public static void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX, emptyContent, metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}
   }

