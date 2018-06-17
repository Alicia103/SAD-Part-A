package com.socket;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class EchoServer {
	public final static int PORT = 6; //wird auf Port 6 geöffnet

	public static void main(String[] args) throws IOException {

		ExecutorService pool = Executors.newFixedThreadPool(500);
		try (ServerSocket server = new ServerSocket(PORT)) {
			while (true) {
				try {
					Socket connection = server.accept();
					Callable<Void> task = new EchoTask(connection);
					pool.submit(task);
				} catch (IOException ex) {
				}
			}
		} catch (IOException ex) {
			System.err.println("Couldn't start server");
		}
	}

}

class EchoTask implements Callable<Void> {
	private Socket connection;

	EchoTask(Socket connection) {
		this.connection = connection;
	}

	@Override
	public Void call() throws IOException {
		try {
			InputStream in = new BufferedInputStream(connection.getInputStream());//liest Byte-weise
			OutputStream out = connection.getOutputStream();
			int c;
			while ((c = in.read()) != -1) {//input(Buchstabe,Zahl etc) wird gelesen und wieder ausgegeben
				out.write(c);
				out.flush();
			}
		} catch (IOException ex) {
			System.err.println(ex);
		} finally {
			connection.close();
		}
		return null;
	}
} //Testbar indem man die Kommando-Zeile aufruft und "telnet localhost 6" eingibt, dadurch wird der port geöffnet und man kann duch Eingaben den Echo Server testen
