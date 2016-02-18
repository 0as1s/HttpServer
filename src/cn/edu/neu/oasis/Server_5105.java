
package cn.edu.neu.oasis;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

class Server {
	
	public static final int	TCP_PORT			= 8000;
	
	private ServerSocket	serverSocket_5105	= null;
	
	private Socket			socket_5105			= null;
	
	public Server(String[] args) {
	
		this.serverSocket_5105 = null;
		this.socket_5105 = null;
		
		try {
			serverSocket_5105 = new ServerSocket(TCP_PORT);
			while (true) {
				socket_5105 = null;
				
				BufferedReader bufferedReader_5105 = null;

				
				ArrayList<String> arrayStrings_5105 = new ArrayList<String>();

				
				DataOutputStream dos_5105 = null;
				socket_5105 = serverSocket_5105.accept();
				
				bufferedReader_5105 =
						new BufferedReader(new InputStreamReader(
								socket_5105.getInputStream()));
				String str = null;
				
				while ((str = bufferedReader_5105.readLine()) != null) {
					arrayStrings_5105.add(str);
					System.out.println(str);
					if (str.equals(""))
						break;
				}

				
				dos_5105 = new DataOutputStream(socket_5105.getOutputStream());
				if (arrayStrings_5105.size() == 0
						|| !arrayStrings_5105.get(0).matches(
								"[A-Z]+\\s.+\\..+\\s.+")) {
					dos_5105.writeUTF("HTTP/1.0 400 Bad Request\r\n"
							+ "Server: "
							+ InetAddress.getLocalHost().getHostName() + "\r\n"
							+ new Date() + "\r\n");
					dos_5105.close();
					bufferedReader_5105.close();
					socket_5105.close();
					continue;
				}

				
				String s[] = arrayStrings_5105.get(0).split(" ");
				
				File f = new File(args[0] + s[1]);
				if (arrayStrings_5105.get(2).endsWith("keep-alive")) {

					if (!f.exists())
						continue;
					else {
						
						DataInputStream dis =
								new DataInputStream(new FileInputStream(f));
						byte data[] = new byte[dis.available()];
						dis.read(data);
						dos_5105.write(data);
						dos_5105.close();
						dis.close();
						continue;
					}
				}
				String tmp = null;
				switch (s[1].split("\\.")[1]) {

					case ("html"):
						tmp = "text/html";
						respond(dos_5105, s, f, tmp);
						break;
					case ("htm"):
						tmp = "text/html";
						respond(dos_5105, s, f, tmp);
						break;
					case ("jpeg"):
						tmp = "image/jpeg";
						respond(dos_5105, s, f, tmp);
						break;
					case ("jpg"):
						tmp = "image/jpg";
						respond(dos_5105, s, f, tmp);
						break;
					default:
						tmp = "*/*";
						respond(dos_5105, s, f, tmp);
				}
				
				dos_5105.close();
				bufferedReader_5105.close();
				socket_5105.close();
			}
		} catch (UnknownHostException e) {

			System.out.println("Cannot Get The Host Name");
			e.printStackTrace();
		} catch (IOException e) {

			System.out.println("An IOException Occurred");
			e.printStackTrace();
		} finally {
			try {
				serverSocket_5105.close();
			} catch (IOException e) {

				System.out.println("Cannot Shut Down The Server");
				e.printStackTrace();
			}
		}
	}
	
	private void respond(DataOutputStream dos, String[] s, File f, String tmp)
			throws UnknownHostException, IOException {
	

		
		if (s[2].equals("HTTP/1.0") || (s[2].equals("HTTP/1.1"))) {
			if (s[0].equals("GET")) {
				if (!f.exists()) {
					
					dos.writeUTF("HTTP/1.0 404 Not Found\r\n" + "Server: "
							+ InetAddress.getLocalHost().getHostName() + "\r\n"
							+ "Content-Type: " + tmp + "\r\n" + new Date()
							+ "\r\n");
					
				} else {
					
					DataInputStream dis =
							new DataInputStream(new FileInputStream(f));
					
					dos.writeUTF("HTTP/1.0 200 OK\r\n" + "Server: "
							+ InetAddress.getLocalHost().getHostName() + "\r\n"
							+ "Content-Type: " + tmp + "\r\n"
							+ "Content-Length: " + dis.available() + "\r\n"
							+ new Date() + "\r\n");
					byte data[] = new byte[dis.available()];
					dis.read(data);
					dos.write(data);
					dos.close();
					dis.close();
				}
			} else if (s[0].equals("HEAD")) {
				if (!f.exists()) {
					dos.writeUTF("HTTP/1.0 404 Not Found\r\n" + "Server: "
							+ InetAddress.getLocalHost().getHostName() + "\r\n"
							+ "Content-Type: " + tmp + "\r\n" + new Date()
							+ "\r\n");
					
				} else {
					dos.writeUTF("HTTP/1.0 200 OK\r\n" + "Server: "
							+ InetAddress.getLocalHost().getHostName() + "\r\n"
							+ "Content-Type: " + tmp + "\r\n" + new Date()
							+ "\r\n");
				}
			} else if (s[0].equals("PUT") || s[0].equals("DELETE")) {
				dos.writeUTF("HTTP/1.0 405 Method Not Allowed\r\n" + "Server: "
						+ InetAddress.getLocalHost().getHostName() + "\r\n"
						+ "Content-Type: " + tmp + "\r\n" + new Date() + "\r\n");
			} else if (s[0].equals("POST")) {
				dos.writeUTF("HTTP/1.0 501 Not Implemented\r\n" + "Server: "
						+ InetAddress.getLocalHost().getHostName() + "\r\n"
						+ new Date() + "\r\n");
			} else {
				dos.writeUTF("HTTP/1.0 500 Bad Request\r\n" + "Server: "
						+ InetAddress.getLocalHost().getHostName() + "\r\n"
						+ new Date() + "\r\n");
			}
		} else {
			dos.writeUTF("HTTP/1.0 400 Bad Request\r\n" + "Server: "
					+ InetAddress.getLocalHost().getHostName() + "\r\n"
					+ new Date() + "\r\n");
		}
	}
}

public class Server_5105 {
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	

		if (args.length != 1) {
			System.out
					.println("Please Post One Argument To Server To Point The Directory");
			System.exit(-1);
		}
		File f = new File(args[0]);
		if (!f.exists() || !f.isDirectory()) {
			System.out
					.println("The Argument Post to Server Should Be A Directory");
			System.exit(-1);
		}
		

		Server server_5105 = new Server(args);
	}
}
