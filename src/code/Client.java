package code;

import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Client {

	public static void main(String[] args) {
		try {
			String httpCommand = args[0];
			URI uri = new URI(args[1]);
	        int port = Integer.parseInt(args[2]);
	        String language = args[3];
	        
        	Socket socket = new Socket(uri.getHost(), port);
            BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOut = new PrintWriter(socket.getOutputStream(), true);
            
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            
            
        	//send request
        	socketOut.println(httpCommand + " " + uri.getPath() + " HTTP/1.1");
        	socketOut.println("Host: " + uri.getHost());
        	socketOut.println("Accept-Language: " + language);
        	socketOut.println("");
        	
            if (httpCommand.equals("POST") || httpCommand.equals("PUT")) {
                System.out.println("type what you want to send: ");
                String stringToSend = stdIn.readLine();
                socketOut.println(stringToSend);
            }
        	
        	//receive response
  	        PrintWriter writer = new PrintWriter("body.html");
  	        boolean pastHeaders = false;
  	        boolean chunked = false;
  	        boolean endOfChunks = false;
    		String newLine;
			//zien naar content length of 0 (bij chuncked) om te stoppen met luisteren
			while((newLine = socketIn.readLine()) != null) {
				System.out.println(newLine);
				
				if (pastHeaders) {
					if (chunked) {
						if (newLine.equals("0")) {
							endOfChunks = true;
						}
						if (!endOfChunks && !isChunkSize(newLine)) {
							writer.println(newLine);
							writer.flush();
						}
					}
					else {
						writer.println(newLine);
						writer.flush();
					}
				}
				else {
					if (newLine.equals("Transfer-Encoding: chunked")) {
						chunked = true;
					}
					if (newLine.equals("")) {
						pastHeaders = true;
					}
				}
			}
			
//			File file = new File("C:\\Users\\casma\\git\\CN\\src\\code\\fileToServe.html");
//			Document doc = Jsoup.parse(file, "UTF-8");
//			Elements imgs = doc.getElementsByTag("img");
//			for (Element img : imgs) {
//			  String imgLocation = img.attr("src");
//			  System.out.println(imgLocation);
//			}	 
			

			
           
		}
		catch(URISyntaxException ex) {
			System.out.println("URI expection: " + ex.getMessage());
		}        
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        }
		catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        }
    }
	
	
	public static boolean isHexadecimal(String nb) {
		try {
			Long.parseLong(nb, 16);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
	
	public static boolean isChunkSize(String line) {
		int indexOfFirstSemiColon = line.indexOf(";");
		if (indexOfFirstSemiColon == -1) {
			//no semicolon in line
			return isHexadecimal(line);
		}
		else {
			return isHexadecimal(line.substring(0, indexOfFirstSemiColon));
		}
	}
}
