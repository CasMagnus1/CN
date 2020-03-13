package code;

import java.io.*;
import java.net.*;

public class Server {

	public static void main(String[] args) {
        int port = 3000;
        
        try {
        	ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
 
                System.out.println("New client connected");
 
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            }
 
        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
	}

}
