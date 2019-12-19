package henning.leddriverj;

import java.io.Closeable;
import java.io.IOException;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialFactory;

import henning.leddriverj.util.Log;

public class Transmitter implements Closeable {
	
	private Serial serial;
	private ReturnListener returnListener;
	private Serial serialDebug;
	private boolean use_return;
	
	public Transmitter() throws IOException	{
		Config c = Config.getInstance();
		this.use_return = c.useCommandReturnByte();
		serial = SerialFactory.createInstance();
		serial.open(c.getSerialDevice(), 115200);
		serial.setBufferingDataReceived(false);
		serial.addListener(returnListener);
		String dbd = c.getSerialDebug();
		if (dbd != null)	{
			serialDebug = SerialFactory.createInstance();
			serialDebug.open(dbd, 115200);
			serialDebug.setBufferingDataReceived(false);
			serialDebug.addListener(this::debugListener);
		}
	}
	private void debugListener(SerialDataEvent ev)	{
		try {
			Log.debug(ev.getAsciiString(), "TR-DevLog");
		} catch (IOException e) {
			Log.warn("Event failure", "TR-DevLog");
		}
	}
	
	private byte[] convert(int[] i)	{
		byte[] re = new byte[i.length];
		for (int C = 0;C < i.length;C++)	{
			re[C] = (byte) i[C];
		}
		return re;
	}
	private void sendCommand(int id,int[] args) throws IOException	{
		if (args == null)	{
			args = new int[0];
		}
		try	{
			byte[] cmd = new byte[args.length + 3];
			cmd[0] = (byte) 0xff;
			cmd[1] = (byte) 0xff;
			cmd[2] = (byte) id;
			System.arraycopy(convert(args), 0, cmd, 3, args.length);
			serial.write(cmd, 0, cmd.length);
			if (use_return)	{
				this.returnListener.waitForReturn();	// Not really neccessary because of input fifo@uartC of LEDDriver
			}
		}	catch (IllegalStateException ex)	{
			Log.error("Illegal serial state sending command", "TR");
			Log.error(ex);
		}
	}
	
	public void clear()	{
		try {
			this.sendCommand(0x01, null);
		} catch (IOException e) {
			Log.error("Error sending clear", "TR");
			Log.error(e);
		}
	}
	public void setAlpha(int alphaDiv)	{
		try {
			this.sendCommand(0x04, new int[] {alphaDiv});
		} catch (IOException e) {
			Log.error("Error sending clear", "TR");
			Log.error(e);
		}
	}
	public static final int[] COLOR_MAP = new int[] {1,0,2};
	public static final int[] INVERSE_COLOR_MAP = new int[] {1,0,2};
	public void setRGB(int[][][] rgb)	{		// Format rgb[y][x][c]: c: 0:r,1:g,2:b
		if (rgb == null || rgb.length == 0 || rgb[0].length == 0)
			throw new IllegalArgumentException("Invalid/Empty rgb array");
		int[] grball = new int[rgb.length*rgb[0].length*3];
		int C = 0;
		for (int Y = 0;Y < rgb.length;Y++)	{
			for (int X = 0;X < rgb[0].length;X++)	{
				for (int CL = 0;CL < 3;CL++)	{
					grball[C++] = rgb[Y][X][COLOR_MAP[CL]];
				}
			}
		}
		try {
			sendCommand(0x03, grball);
		} catch (IOException e) {
			Log.error("Error sending rgb", "TR");
			Log.error(e);
		}
	}
	
	@Override
	public void close()	{
		if (serial != null)	{
			try {
				serial.close();
			} catch (IllegalStateException | IOException e) {
				Log.error("Failed to close serial", "TR");
			}
		}
		if (serialDebug != null)	{
			try {
				serialDebug.close();
			} catch (IllegalStateException | IOException e) {
				Log.error("Failed to close debug serial", "TR");
			}
		}
	}
	
}
