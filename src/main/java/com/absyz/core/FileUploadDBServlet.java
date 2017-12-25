package main.java.com.absyz.core;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
 
@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class FileUploadDBServlet extends HttpServlet {
      protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
    response.setContentType("text/html");
   PrintWriter out = response.getWriter();
String saveFile="";
String contentType = request.getContentType();
if ((contentType != null) && (contentType.indexOf("multipart/form-data") >= 0)) {
DataInputStream in = new DataInputStream(request.getInputStream());
int formDataLength = request.getContentLength();
byte dataBytes[] = new byte[formDataLength];
int byteRead = 0;
int totalBytesRead = 0;
while (totalBytesRead < formDataLength) {
byteRead = in.read(dataBytes, totalBytesRead,formDataLength);
totalBytesRead += byteRead;
}
String file = new String(dataBytes);
saveFile = file.substring(file.indexOf("filename=\"") + 10);
saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,saveFile.indexOf("\""));
int lastIndex = contentType.lastIndexOf("=");
String boundary = contentType.substring(lastIndex + 1,contentType.length());
int pos;
pos = file.indexOf("filename=\"");
pos = file.indexOf("\n", pos) + 1;
pos = file.indexOf("\n", pos) + 1;
pos = file.indexOf("\n", pos) + 1;
int boundaryLocation = file.indexOf(boundary, pos) - 4;
int startPos = ((file.substring(0, pos)).getBytes()).length;
int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
File ff = new File(saveFile);
FileOutputStream fileOut = new FileOutputStream(ff);
fileOut.write(dataBytes, startPos, (endPos - startPos));
fileOut.flush();
fileOut.close();
out.println("You have successfully upload the file by the name of: "+saveFile);
try {
Class.forName("com.mysql.jdbc.Driver").newInstance();
conn = DbConnection.getConnection();
File f = new File(saveFile);
PreparedStatement psmnt = conn.prepareStatement("insert into image(image) values(?)");
FileInputStream fis = new FileInputStream(f);
psmnt.setBinaryStream(1, (InputStream)fis, (int)(f.length()));
int s = psmnt.executeUpdate();
if(s>0) {
System.out.println("Uploaded successfully !");
}
else{
System.out.println("unsucessfull to upload file.");
}
}
catch(Exception e){e.printStackTrace();
}
   out.println("<table align='center' border=1 width='200px'>");
 out.println("<tr><td colspan=2 align='center'><b>Download Images</b></td></tr><tr><td colspan=2> </td></tr>");
   String connectionURL = "jdbc:mysql://localhost:3306/test";
  Connection con=null;
  try{      
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    con=DbConnection.getConnection();
    Statement stmt=con.createStatement();
    String strQuery = "select image_id from image";
    ResultSet rs = stmt.executeQuery(strQuery);
    int sno=0;
    while(rs.next())
    {
      sno++;
      out.println("<tr style=background-color:#efefef><td><b>"+sno+"</b></td><td align='center'><a href=DownloadFileServlet?id="+rs.getInt(1)+"><img src=DownloadFileServlet?id="+rs.getInt(1)+" width=50 height=50></a></td></tr>");
    }
      out.println("</table>");
    rs.close();
    con.close();
    stmt.close();
  }
  catch(Exception e)
  {
    e.getMessage();
  }
}
  }
}
