package henning.leddriverj.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Log {
	
	private static boolean enabled = true;
	private static PrintStream fileOutput = null;
	private static int currentLogLevel = LogLevel.INFO.ll;
	private static Scanner defaultIn = new Scanner(System.in);
	
	public static enum LogLevel {
		TRACE(-20,"TRACE"),DEBUG(-10,"DEBUG"),INFO(0,"INFO"),WARN(10,"WARN"),ERROR(20,"ERROR"),REQUEST(30,"REQUEST");
		
		public final String name;
		public final int ll;
		private LogLevel(int i,String name)	{
			this.ll = i;
			this.name = name;
		}
	}
	public static interface SimpleLog	{
		public default void log(LogLevel l,String s)	{
			Log.log(l, s, this.getClass().getSimpleName());
		}
		public default void info(String s)	{
			Log.log(LogLevel.INFO, s, this.getClass().getSimpleName());
		}
		public default void warn(String s)	{
			Log.log(LogLevel.WARN,s,this.getClass().getSimpleName());
		}
		public default void error(String s)	{
			Log.log(LogLevel.ERROR, s,this.getClass().getSimpleName());
		}
		public default void debug(String s)	{
			Log.log(LogLevel.DEBUG, s,this.getClass().getSimpleName());
		}
		public default void error(Exception e)	{
			Log.log(LogLevel.ERROR, e.getClass().getName() + ": " + e.getMessage(),this.getClass().getSimpleName());
			e.printStackTrace(System.err);
			if (fileOutput != null)
				e.printStackTrace(fileOutput);
		}
	}
	
	public static boolean isDebugEnabled()	{
		return currentLogLevel <= LogLevel.DEBUG.ll;
	}
	public static void setDebug(boolean enabled)	{
		if (enabled)
			currentLogLevel = LogLevel.DEBUG.ll;
		else
			currentLogLevel = LogLevel.INFO.ll;
	}
	public static void setLogLevel(int ll)	{
		currentLogLevel = ll;
	}
	public static void setLogLevel(LogLevel l)	{
		currentLogLevel = l.ll;
	}
	public void setEnabled(boolean enabled)	{
		Log.enabled = enabled;
	}
	public static void setFileOutput(File f) throws IOException	{
		if (fileOutput != null)	{
			fileOutput.close();
		}
		if (!f.exists())
			f.createNewFile();
		if (f.isFile())	{
			Log.fileOutput = new PrintStream(f);
		}
	}
	
	public static void println(String s)	{
		System.out.println(s);
		if (fileOutput != null)
			fileOutput.println(s);
	}
	public static void print(String s)	{
		System.out.print(s);
		if (fileOutput != null)
			fileOutput.println(s);	// Print line since user input is not shown here
	}
	public static void printErrLn(String s)	{
		System.err.println(s);
		if (fileOutput != null)
			fileOutput.println("E:" + s);
	}
	
	private static String getTimeString()	{
		Date now = Calendar.getInstance().getTime();
		return "[" + new SimpleDateFormat("HH:mm:ss:SSS").format(now) + "]";
	}
	
	private static String getPref(LogLevel l,String tag)	{
		if (tag == null)
			return getTimeString() + " [" + l.name +  "@" + Thread.currentThread().getName() + "] ";
		else
			return getTimeString() + " [" + l.name +  "@" + Thread.currentThread().getName() + "] [" + tag + "] ";
	}
	
	public static void log(LogLevel l,String msg,String tag)	{
		if (enabled && l.ll >= currentLogLevel)	{
			if (l == Log.LogLevel.REQUEST) {
				print(getPref(l, tag) + msg + ": ");
			} else if (l.ll >= LogLevel.ERROR.ll)	{
				printErrLn(getPref(l, tag) + msg);
			} else	{
				println(getPref(l, tag) + msg);
			}
		}
	}
	public static void log(LogLevel l,String msg)	{
		log(l, msg, null);
	}
	
	public static void info(String s)	{
		log(LogLevel.INFO,s);
	}
	public static void info(String s,String tag)	{
		log(LogLevel.INFO,s,tag);
	}
	public static void warn(String s)	{
		log(LogLevel.WARN,s);
	}
	public static void warn(String s,String tag)	{
		log(LogLevel.WARN,s,tag);
	}
	public static void error(String s)	{
		log(LogLevel.ERROR,s);
	}
	public static void error(String s,String tag)	{
		log(LogLevel.ERROR,s,tag);
	}
	public static void error(Exception e)	{
		log(LogLevel.ERROR,e.getClass().getName() + ": " + e.getMessage());
		e.printStackTrace(System.err);
		if (fileOutput != null)
			e.printStackTrace(fileOutput);
	}
	public static void debug(Exception e)	{
		log(LogLevel.DEBUG,e.getClass().getName() + ": " + e.getMessage());
		e.printStackTrace(System.out);
		if (fileOutput != null)
			e.printStackTrace(fileOutput);
	}
	public static void debug(String s)	{
		log(LogLevel.DEBUG,s);
	}
	public static void debug(String s,String tag)	{
		log(LogLevel.DEBUG,s,tag);
	}
	public static String request(String s,String tag)	{
		log(LogLevel.REQUEST,s,tag);
		return defaultIn.nextLine();
	}
	public static String request(String s)	{
		log(LogLevel.REQUEST,s);
		return defaultIn.nextLine();
	}
	
}
