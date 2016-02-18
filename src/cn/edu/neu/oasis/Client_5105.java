
package cn.edu.neu.oasis;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client_5105 {
	
	private static final int	TCP_PORT	= 8000;
	
	public static void main(String[] args) {
	
		Socket s = null;
		PrintWriter pw_5105 = null;
		DataOutputStream dos_5105 = null;
		DataInputStream dis_5105 = null;
		// TODO 自动生成的方法存根
		try {
			/* 对用户传递给Client的参数进行判断，如果不符合要求，则返回错误信息并退出 */
			if (args.length != 2) {
				System.out.println("Wrong Input, Please Read The Manual");
				System.exit(-1);
			}
			s = new Socket("127.0.0.1", TCP_PORT);
			pw_5105 =
					new PrintWriter(new OutputStreamWriter(s.getOutputStream()));
			
			/* 利用用户传递给Client的参数构造请求 */
			String tmp = null;
			try {
				switch (args[1].split("\\.")[1]) {
				/* 检测请求的文件名的后缀以决定MIME类型 */
					case ("html"):
						tmp = "text/html";
						break;
					case ("htm"):
						tmp = "text/html";
						break;
					case ("jpeg"):
						tmp = "image/jpeg";
						break;
					case ("jpg"):
						tmp = "image/jpg";
						break;
					default:
						tmp = "*/*";
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			
			pw_5105.println("GET " + args[1] + " HTTP/1.0");
			pw_5105.println("Host: " + args[0]);
			pw_5105.println("Content-Type: " + tmp);
			pw_5105.println();
			pw_5105.flush();
			/* 使用DataInputStream来读取来自Server的报头和文件 */
			dis_5105 = new DataInputStream(s.getInputStream());
			String str = dis_5105.readUTF();
			System.out.print(str);
			/* 如果返回的状态码在200到300之间，则接受文件 */
			if (200 <= Integer.parseInt(str.split(" ")[1])
					&& Integer.parseInt(str.split(" ")[1]) < 300) {
				dos_5105 =
						new DataOutputStream(new FileOutputStream(new File(
								args[1])));
				byte data[] = new byte[dis_5105.available()];
				dis_5105.read(data);
				dos_5105.write(data);
			}
		} catch (UnknownHostException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} finally {
			try {
				dis_5105.close();
				pw_5105.close();
				s.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
}
