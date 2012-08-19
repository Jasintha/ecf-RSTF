package org.eclipse.ecf.remoteservice.testframework.handler;

import org.eclipse.ui.console.MessageConsoleStream;

public class RemoteConsole {
	
	
	 static MessageConsoleStream  console;

	 public static void println(String msg) {
		 console.println(msg);
	}

	public static void setConsole(MessageConsoleStream console) {
		 RemoteConsole.console = console;
	 }
	 
	 

}
