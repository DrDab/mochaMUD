package client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Runner 
{
	/**
	 * 
	 *  Copyright (c) 2017 _c0da_ (Victor Du)
	 *
	 *	Permission is hereby granted, free of charge, to any person obtaining a copy
	 *	of this software and associated documentation files (the "Software"), to deal
	 *	in the Software without restriction, including without limitation the rights
	 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	 *	copies of the Software, and to permit persons to whom the Software is
	 *	furnished to do so, subject to the following conditions:
	 *  
	 *	The above copyright notice and this permission notice shall be included in all
	 *	copies or substantial portions of the Software.
	 *
	 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	 *	SOFTWARE.
	 */
	static ServerRunner sr;
	static Stopwatch clock;
	static Scanner lc;
	@SuppressWarnings("static-access")
	public static void main (String[] args)
	{
		clock = new Stopwatch();
		sr = new ServerRunner(clock);
		System.out.println("Welcome to coffee{MUD} v0.1 by Victor Du");
		System.out.println("This software is open source and freely redistributable.");
		System.out.println("\n");
		System.out.println("              _____  _____               .____     _____   ____ ___________     ____. ");
		System.out.println("  ____  _____/ ____\\/ ____\\____   ____   |   _|   /     \\ |    |   \\______ \\   |_   | ");
		System.out.println("_/ ___\\/  _ \\   __\\\\   __\\/ __ \\_/ __ \\  |  |    /  \\ /  \\|    |   /|    |  \\    |  | ");
		System.out.println("\\  \\__(  <_> )  |   |  | \\  ___/\\  ___/  |  |   /    Y    \\    |  / |    `   \\   |  | ");
		System.out.println(" \\___  >____/|__|   |__|  \\___  >\\___  > |  |_  \\____|__  /______/ /_______  /  _|  | ");
		System.out.println("     \\/                       \\/     \\/  |____|         \\/                 \\/  |____| ");
		System.out.println("\n");
		System.out.println("Try connecting to a server with the command: connect <serverIP> <serverport>");
		System.out.println("Example command: \" connect furrymuck.com 8888 \" or \" connect batmud.bat.org 23 \"");
		System.out.println("\n\n Please enter a command. \n\n");
		lc = new Scanner(System.in);
		for(;;)
		{
			if (shouldOrderPizza())
			{
				System.out.println("\n\n Whew, you've been playing for a while. Maybe take a break and order some pizza?");
				System.out.println("To order pizza, run \" pizza \".\n\n");
			}
			System.out.print("\nLOCAL>");
			String input = lc.nextLine();
			if (input.contains("connect") && checkValidInput(input))
			{
				StringTokenizer st = new StringTokenizer(input);
				st.nextToken();
				String ip = st.nextToken();
				int port = Integer.parseInt(st.nextToken());
				if (sr.connect(ip, port))
				{
					try {
						sr.beginComms();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (input.matches("time"))
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
			if (input.matches("stats"))
			{
				if (sr.isRun)
				{
					System.out.println("========[ tiny{MUD} stats page ]========");
					double uptime = clock.elapsedTime();
					int getDaysU = (int) (uptime / 60 / 60 / 24);
					int getHoursU = (int) ((uptime/60/60)%24);
					int getMinsU = (int) ((uptime / 60)%60);
					int getSecsU = (int) (uptime % 60);
					System.out.println("IP     : " + sr.clientSock.getRemoteSocketAddress());
					System.out.println("Port   : " + sr.clientSock.getPort());
					System.out.println("Uptime : " + getDaysU + " d " + getHoursU + " hrs " + getMinsU + " mins " + getSecsU + " sec");
					System.out.println("Moves  : " + sr.commands);
					System.out.println("========[ End of stats page ]========");
				}
			}
			if (input.matches("pizza"))
			{
				// https://www.dominos.com/en/pages/order/
				System.out.println("Ordering Domino's pizza for you...");
				try {
					java.awt.Desktop.getDesktop().browse(new URI("https://www.dominos.com/en/pages/order/"));
				} catch (IOException | URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (input.matches("resume"))
			{
				if (sr.isRun)
				{
					try {
						sr.beginComms();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if (input.matches("exit"))
			{
				System.out.println("Exiting coffee{MUD}...");
				System.exit(0);
			}
		}
	}
	public static boolean checkValidInput(String str)
	{
		StringTokenizer st = new StringTokenizer(str);
		if (str.contains("connect"))
		{
			try
			{
				st.nextToken();
				st.nextToken();
				if (!isInteger(st.nextToken()))
				{
					System.out.println("You didn't enter a port number!");
					System.out.println("connect usage : connect <server IP> <server port>");
					return false;
				}
			} 
			catch (Exception e)
			{
				System.out.println("Input for 'connect' is not correct");
				System.out.println("connect usage : connect <server IP> <server port>");
				return false;
			}
		}
		return true;
	}
	public static boolean isInteger(String s)
	{
	    try 
	    { 
	        Integer.parseInt(s); 
	    } 
	    catch(NumberFormatException e)
	    { 
	        return false; 
	    } 
	    catch(NullPointerException e) 
	    {
	        return false;
	    }
	    return true;
	}
	public static boolean shouldOrderPizza()
	{
		double uptime = clock.elapsedTime();
		int getHoursU = (int) ((uptime/60/60)%24);
		if (getHoursU >= 1)
		{
			return true;
		}
		return false;
	}
}
