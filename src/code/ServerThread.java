package code;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread{
	
	public ServerThread(Socket socket) {
		try {
	        writer = new PrintWriter(socket.getOutputStream(), true);
	        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}
        catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
        }
	}
	
	public void run() {
		System.out.println("thread is run");
		String newLine;
		try {
			while((newLine = in.readLine()) != null) {
				// luisteren naar socket
			}
		}
		catch (IOException ex) {
			System.out.println("readLine exception: " + ex.getMessage());
		}

	}
	
	
	
	public PrintWriter writer;
	public BufferedReader in;

}
