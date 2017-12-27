//package codeTpoint;
package main.java.com.absyz.core;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import javax.servlet.http.Part;
/**
 * Servlet implementation class UploadServlet
 */
public class uploadservlet1 extends HttpServlet {
    private static final long serialVersionUID = 1L;
       
	private boolean isMultipart;
	   private String filePath;
	   private int maxFileSize = 50 * 1024;
	   private int maxMemSize = 4 * 1024;
	   private File file ;

	   public void init( ){
	      // Get the file location where it would be stored.
	       //filePath = getServletContext().getInitParameter("file-path"); 
		
		   //uploadservlet1 u=new uploadservlet1();
		   filePath ="EcommerceMaven\\WebContent\\images\\"; 
		
		  
		   
	   }
	   public void doPost(HttpServletRequest request,    HttpServletResponse response)   throws ServletException, java.io.IOException {
		   
		   // Check that we have a file upload request
	      isMultipart = ServletFileUpload.isMultipartContent(request);
	      response.setContentType("text/html");
	      java.io.PrintWriter out = response.getWriter( );

	      
	      
	      if( !isMultipart )
	      {
	         out.println("No Upload This Time");
	         return;
	      }
	      DiskFileItemFactory factory = new DiskFileItemFactory();
	      
	      
	      // maximum size that will be stored in memory
	      factory.setSizeThreshold(maxMemSize);
     
	      // Location to save data that is larger than maxMemSize.
	      factory.setRepository(new File("WebContent"));

	      // Create a new file upload handler
	      ServletFileUpload up = new ServletFileUpload(factory);
	      
	      

	      up.setSizeMax( maxFileSize );

	      try{ 

	      List fileItems = up.parseRequest(request);
		

	      Iterator element = fileItems.iterator();
 
	      out.println("<html>");
	      out.println("<head>");
	      out.println("<title>Servlet upload</title>");  
	      out.println("</head>");
	      out.println("<body>");
	      while ( element.hasNext () ) 
	      {
	        FileItem fi = (FileItem)element.next();
	         if ( !fi.isFormField () )	
	         {
	            
	            String fieldName = fi.getFieldName();
	            String fileName = fi.getName();
	            String contentType = fi.getContentType();
	            boolean isInMemory = fi.isInMemory();
	            long sizeInBytes = fi.getSize();
	            				 
	            
	            if( fileName.lastIndexOf("\\") >= 0 ){
	               file = new File( filePath +"\\"+ fileName.substring( fileName.lastIndexOf("\\"))) ;
	            }else{
	               file = new File( filePath +"\\"+fileName.substring(fileName.lastIndexOf("\\")+1)) ;
	            }
	            
	          
	            fi.write( file ) ;
	            out.println("Uploaded Filename: " + fileName + "<br>"+ filePath);
	         }
	      }
	      out.println("</body>");
	      out.println("</html>");
	   }catch(Exception ex) {
	       System.out.println(ex);
	   } 
	   }

	   public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException { doPost(request, response); }
	   	    
	
public String getPath() throws UnsupportedEncodingException {
	try {
String path = this.getClass().getClassLoader().getResource("").getPath();
String fullPath = URLDecoder.decode(path, "UTF-8");
String pathArr[] = fullPath.split("/WEB-INF/classes/");
System.out.println(fullPath);
System.out.println(pathArr[0]);
fullPath = pathArr[0];
String reponsePath = "";
// to read a file from webcontent
reponsePath = new File(fullPath).getPath() + File.separatorChar;
return reponsePath;
}

	catch (UnsupportedEncodingException e) {
	e.printStackTrace();
		return null;
	}
}
}
