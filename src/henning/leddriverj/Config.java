package henning.leddriverj;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import henning.leddriverj.util.Log;

public class Config {
	
	public static final String DEFAULT_CONFIG = "./led.properties";
	private static Config instance = null;
	public static Config getInstance()	{
		if (instance == null)	{
			try {
				instance = new Config();
			} catch (IOException e) {
				Log.error("Failed to load config", "Config");
				Log.error(e);
				System.exit(1);
			}
		}
		return instance;
	}
	
	private Properties prop;
	
	public Config() throws FileNotFoundException, IOException	{
		prop = this.load();
		Log.setDebug(getDebugEnabled());
	}
	
	private Properties load() throws FileNotFoundException, IOException	{
		Properties p = new Properties();
		p.setProperty("data_serial", "/dev/ttyUSB0");
		p.setProperty("debug_serial", "null");
		p.setProperty("debug", "true");
		p.setProperty("command_return_byte", "false");
		p.setProperty("py_port", "COM4");
		File f = new File(DEFAULT_CONFIG);
		if (!f.exists())	{
			p.load(new FileReader(f));
			Log.info("Properties loaded", "Config");
		}	else	{
			f.createNewFile();
			p.store(new FileOutputStream(f), "--- LED Driver Configuration ---");
			Log.info("Created new Properties file at " + DEFAULT_CONFIG, "Config");
		}
		return p;
	}
	
	public String getSerialDevice()	{
		return this.prop.getProperty("data_serial");
	}
	public String getSerialDebug()	{
		String r = this.prop.getProperty("debug_serial");
		return r == null || r.equalsIgnoreCase("null") ? null : r;
	}
	public boolean getDebugEnabled()	{
		String r = this.prop.getProperty("debug");
		return r != null && r.equalsIgnoreCase("true");
	}
	public boolean useCommandReturnByte()	{
		String r = this.prop.getProperty("command_return_byte");
		return r != null && r.equalsIgnoreCase("true");
	}
	public String getPyPort()	{
		return this.prop.getProperty("py_port");
	}
	
}
