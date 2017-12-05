package client;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerRunner
{
	static Socket clientSock;
	static Scanner getHIDInput;
	static remoteTTY tty;
	static double startTime;
	static double mLoadTime;
	// PrintWriter pw;
	static int ActivationCount = 0;
	BufferedWriter bw;
	DataOutputStream out;
	Stopwatch clock;
	public ServerRunner(Stopwatch clock)
	{
		// Auto-generated class constructor
		this.clock = clock;
		startTime = clock.elapsedTime();
		getHIDInput = new Scanner(System.in);
	}
	public boolean connect(String ip, int port)
	{
		try 
		{
			clientSock = new Socket(ip, port);
			System.out.println("Connected to " + ip + " in " + (clock.elapsedTime() - startTime) + "s");
			tty = new remoteTTY(clientSock);
		    Thread getinputstm = new Thread(new Runnable() {
		         public void run() {
		              try {
						tty.listen();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		         }
		    });  
		    getinputstm.start();
		    System.out.println("Loaded modules in " + (clock.elapsedTime() - startTime) + "s");
			return true;
		}
		catch (Exception e)
		{
			System.err.println("Failed to connect to server " + ip + " on port " + port);
			e.printStackTrace();
			return false;
		}
	}
	public void beginComms() throws IOException
	{
		System.out.println("Beginning communications with server...");
		bw = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
		for(;;)
		{
			try
			{
				System.out.print("\nSERVER>");
				String s = getHIDInput.nextLine();
				if (s.matches("$ragequit")) { break; }
				if (s.matches("$time"))
				{
					double seconds = clock.getTime();
					int getDays = (int) (seconds / 60 / 60 / 24);
					int getHours = (int) ((seconds/60/60)%24);
					int getMins = (int) ((seconds / 60)%60);
					int getSecs = (int) (seconds % 60);
					//double getSeconds = seconds / 60 / 60 / 24
					System.out.println("UTC: " + getDays + " d " + getHours + " hrs " + getMins + " mins " + getSecs + " sec");
					double uptime = clock.elapsedTime();
					int getDaysU = (int) (uptime / 60 / 60 / 24);
					int getHoursU = (int) ((uptime/60/60)%24);
					int getMinsU = (int) ((uptime / 60)%60);
					int getSecsU = (int) (uptime % 60);
					System.out.println("Uptime: " + getDaysU + " d " + getHoursU + " hrs " + getMinsU + " mins " + getSecsU + " sec");
				}
				if (ActivationCount >= 2)
				{
					bw.write(s);
		            bw.newLine();
		            bw.flush();
		            ActivationCount++;
				}
				else
				{
					for(ActivationCount = 0; ActivationCount < 3; ActivationCount++)
					{
						bw.write(s);
			            bw.newLine();
			            bw.flush();
					}
				}
			}
			catch (Exception e)
			{
				System.err.println("Connection was interrupted: " + e);
				e.printStackTrace();
				break;
			}
		}
	}
}
