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
			//setup connection
			String httpCommand = args[0];
			URI uri = new URI(args[1]);
	        int port = Integer.parseInt(args[2]);
	        String language = args[3];
	        
        	Socket socket = new Socket(uri.getHost(), port);
            BufferedReader socketInChar = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter socketOutChar = new PrintWriter(socket.getOutputStream(), true);            
            
        	//send request
        	socketOutChar.println(httpCommand + " " + uri.getPath() + " HTTP/1.1");
        	socketOutChar.println("Host: " + uri.getHost());
        	socketOutChar.println("Accept-Language: " + language);
        	socketOutChar.println("");
        	
            if (httpCommand.equals("POST") || httpCommand.equals("PUT")) {
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("type what you want to send: ");
                String stringToSend = stdIn.readLine();
                socketOutChar.println(stringToSend);
            }
        	
        	//receive response
  	        PrintWriter writer = new PrintWriter("body.html");
  	        boolean pastHeaders = false;
  	        boolean chunked = false;
  	        boolean endOfChunks = false;
    		String newLine;
			//zien naar content length (kijken naar bytestream ipv character stream, hoezo character
    		//stream automatisch juiste encodering?)
			while((newLine = socketInChar.readLine()) != null && !endOfChunks) {
				System.out.println(newLine);
				
				if (pastHeaders) {
					if (chunked) {
						if (newLine.equals("0")) {
							endOfChunks = true;
						}
						if (!endOfChunks && !containsChunkSize(newLine)) {
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
			
			//GET embedded imgs
			File file = new File("C:\\Users\\casma\\git\\CN\\body.html");
			Document doc = Jsoup.parse(file, "UTF-8");
			Elements imgs = doc.getElementsByTag("img");
//			for (Element img : imgs) {
//			  String imgLocation = img.attr("src");
//			  System.out.println(imgLocation);
//			}	 
			
			System.out.println(imgs.first().attr("src"));
        	socketOutChar.println("GET " + imgs.first().attr("src") + " HTTP/1.1");
        	socketOutChar.println("Host: " + uri.getHost());
        	socketOutChar.println("Accept-Language: " + language);
        	socketOutChar.println("");
        	
        	//vermoedelijk metadata in body, ook chunked ondersteunen (bytes naar char omzetten?)
	        InputStream socketInBytes = socket.getInputStream();
	        FileOutputStream writerBytes = new FileOutputStream("TestImage.png");
    		String newLine2;

			
           
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
	
	//check if the given string is a representation of a hexadecimal number
	public static boolean isHexadecimal(String nb) {
		try {
			Long.parseLong(nb, 16);
			return true;
		}
		catch (NumberFormatException ex) {
			return false;
		}
	}
	
	//check if the given string contains the current chunk size
	public static boolean containsChunkSize(String line) {
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
