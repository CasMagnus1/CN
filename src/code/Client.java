package code;

import java.io.*;
import java.net.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Client {

	public static void main(String[] args) {
		try {
			String httpCommand = args[0];
			URI uri = new URI(args[1]);
	        int port = Integer.parseInt(args[2]);
	        String language = args[3];
	        
        	Socket socket = new Socket(uri.getHost(), port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            
        	//send request
        	out.println(httpCommand + " " + uri.getPath() + " HTTP/1.1");
        	out.println("Host: " + uri.getHost());
        	out.println("Accept-Language: " + language);
        	out.println("");
        	
            if (httpCommand.equals("POST") || httpCommand.equals("PUT")) {
            	//include body
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("type what you want to send: ");
                String stringToSend = in.readLine();
            }
        	
        	//receive response
  	        PrintWriter writer = new PrintWriter("response.txt");
  	        
			String html = "<p>An <a href='http://example.com/'><b>example</b></a> link.</p>";
			Document doc = Jsoup.parse(html);
			Element link = doc.select("a").first();
  	        
			//mss aparte thread?
    		String newLine;
			while((newLine = reader.readLine()) != null) {
				System.out.println(newLine);
				writer.println(newLine);
				//without flush only writes half response
				writer.flush();
			}
			


			

			
           
		}
		catch(URISyntaxException ex) {
			System.out.println("URI expection: " + ex.getMessage());
		}        
        catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        }
		catch (IOException ex) {
            System.out.println("I/O or readLine error: " + ex.getMessage());
        }
    }
}
