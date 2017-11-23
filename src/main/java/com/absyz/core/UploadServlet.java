package main.java.com.absyz.core;

import java.io.*;
import java.util.*;
 
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
   }

