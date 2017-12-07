package client;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;

public class Defibrillator
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
	
	BufferedWriter bw;
	boolean isRun = false;
	
	public Defibrillator(BufferedWriter bw)
	{
		this.bw = bw;
	}
	
	public void run() throws InterruptedException, IOException
	{
		for(;;)
		{
			if (isRun)
			{
				// send keep-alive message to server every 5 minutes
				Thread.sleep(300000);
				bw.write("zap");
				bw.newLine();
				bw.flush();
			}
		}
	}
	public void toggle()
	{
		if (isRun)
		{
			System.out.println("Turning off anti-AFK...");
		}
		else
		{
			System.out.println("Turning on anti-AFK...");
		}
		isRun = !isRun;
	}
}
