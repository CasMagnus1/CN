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
			//zien naar content length (kijken naar bytestream ipv character stream)
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
	
	//translate the textToTranslate from a given a language to a given language using
	//yandex API
	public static String translate(String textToTranslate, String from, String to) {
		String result = "translation failed";
		try {
			//SETUP CONNECTION
			String key = "trnsl.1.1.20200316T110857Z.ab574be75c148af7.5a82ce1ebf9c4336334482932ca9f230146a5a29";
	        URL url = new URL("https://translate.yandex.net/api/v1.5/tr/translate?lang=" + from + "-" + to + 
	        		"&key=" + key); 
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); 

	        //REQUEST
	        connection.setRequestMethod("POST"); 
	        connection.setRequestProperty("Host", "translate.yandex.net"); 
	        connection.setRequestProperty("Accept", "*/*"); 
	        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");   
	        //connection.setFixedLengthStreamingMode(17); 

	        String body = "text=" + textToTranslate;
	        
	        connection.setDoOutput(true);
	        OutputStream out = connection.getOutputStream(); 
            out.write(body.getBytes()); 
            
            //RESPONSE
            //only reads body
            InputStream in = connection.getInputStream(); 
            BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(in));
            
            StringBuilder intermediateResponse = new StringBuilder(); 
            String newLine = null; 
            while ((newLine = bufferedIn.readLine()) != null)  
            { 
                intermediateResponse.append(newLine); 
            } 
            //response in xml: <text>translated_text</text>
            String response = intermediateResponse.toString();
            int indexBegin = response.indexOf("<text>") + 6;
            int indexEnd = response.indexOf("</text");
            result = response.substring(indexBegin, indexEnd);
            
		}
		catch (MalformedURLException ex) {
			System.out.println(ex.getMessage());
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		return result;
	}
}
