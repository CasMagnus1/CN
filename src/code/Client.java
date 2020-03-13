package code;

import java.io.*;
import java.net.*;

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
            
            if (httpCommand.equals("GET")) {
            	//send get request
            }
            if (httpCommand.equals("HEAD")) {
            	//send head request
            }
            if (httpCommand.equals("PUT")) {
            	//send put request
            }
            if (httpCommand.equals("POST")) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("type what you want to send: ");
                String stringToSend = in.readLine();
                //send post request met stringToSend als body
            }
            
            
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
}
