import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import main.java.com.absyz.core.DbConnection;
public class UploadServlet1 extends HttpServlet {
   public void doPost(HttpServletRequest request,  HttpServletResponse response)throws IOException, ServletException{
  Connection conn =null;
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
