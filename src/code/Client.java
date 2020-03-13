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
	        
        	//URI nog parsen
        	Socket socket = new Socket(uri.getHost(), port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
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
