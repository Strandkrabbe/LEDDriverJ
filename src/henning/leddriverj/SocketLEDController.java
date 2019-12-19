package henning.leddriverj;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import henning.leddriverj.util.Log;

public class SocketLEDController extends LEDController {
	
	private static final String ADDR = "::1";
	private static final int PORT = 22444;
	
	private Socket soc;
	private OutputStream os;
	private Process py;
	
	SocketLEDController(int width,int height) throws IOException	{
		super(width,height);
		start();
	}
	private void start() throws IOException	{
		InputStream i = this.getClass().getResourceAsStream("/henning/leddriverj/py/serialsoc.py");
		Files.copy(i, new File("./serialsoc.py").toPath(), StandardCopyOption.REPLACE_EXISTING);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		this.py = Runtime.getRuntime().exec("python ./serialsoc.py " + Config.getInstance().getPyPort());
		this.soc = new Socket(ADDR, PORT);
		this.os = soc.getOutputStream();
	}
//	private byte[] toByteArray(int[] arr)	{
//		byte[] b = new byte[arr.length];
//		for (int C = 0;C < arr.length;C++)	{
//			b[C] = (byte) arr[C];
//		}
//		return b;
//	}
	private void sendCommand(int cmd,int[] a) throws IOException	{
		if (a == null)
			a = new int[0];
		byte[] b = new byte[a.length + 3];
		b[0] = (byte) 0xff;
		b[1] = (byte) 0xff;
		b[2] = (byte) cmd;
		for (int C = 0;C < a.length;C++)	{
			b[3 + C] = (byte) a[C];
		}
		this.os.write(b);
		this.os.flush();
	}
	
	public static final int[] COLOR_MAP = new int[] {1,0,2};
	public static final int[] INVERSE_COLOR_MAP = new int[] {1,0,2};
	@Override
	protected void writeRGB(int[][][] rgb) {
		if (rgb == null || rgb.length == 0 || rgb[0].length == 0)
			throw new IllegalArgumentException("Invalid/Empty rgb array");
		int[] grball = new int[rgb.length*rgb[0].length*3];
		int C = 0;
		for (int Y = 0;Y < rgb.length;Y++)	{
			for (int X = 0;X < rgb[0].length;X++)	{
				int[] col = rgb[Y][X];
				col = this.colorMod(col);
				for (int CL = 0;CL < 3;CL++)	{
					grball[C++] = col[COLOR_MAP[CL]] == 0xff ? 0xfe : col[COLOR_MAP[CL]];
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
		try {
			soc.close();
		} catch (IOException e) {
			Log.error(e);
		}
		Log.debug("Awaiting py termination by socket close...", "SLED");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {}
		if (py.isAlive())	{
			py.destroy();
			Log.debug("Py not terminated. Forced", "SLED");
		}
	}
	@Override
	public void clear() {
		try	{
			sendCommand(0x01, null);
		}	catch (IOException e)	{
			Log.error("Error sending clear","TR");
			Log.error(e);
		}
	}
	@Override
	public void setAlpha(int alphaDiv) {
		try	{
			sendCommand(0x04, new int[] {alphaDiv});
		}	catch (IOException e)	{
			Log.error("Error sending alpha","TR");
			Log.error(e);
		}
	}
	
}
