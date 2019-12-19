package henning.leddriverj;

import java.io.Closeable;
import java.io.IOException;

public class BoardController implements Closeable {
	
	private LEDController backend;
	private int[] back = new int[] {0,0,0};
	
	public BoardController(Mode m,int width,int height) throws IOException	{
		if (m == Mode.MODE_PY)	{
			backend = new SocketLEDController(width, height);
		} else if (m == Mode.MODE_RPI)	{
			backend = new SyncLEDController(width, height);
		} else {
			throw new IllegalArgumentException("Invalid Mode for LEDController");
		}
	}
	public BoardController(int width,int heigth) throws IOException	{
		this(detect(),width,heigth);
	}
	private static Mode detect()	{
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win"))	{
			return Mode.MODE_PY;
		}	else	{
			return Mode.MODE_RPI;
		}
	}
	
	public void updateBoard()	{
		this.backend.update();
	}
	public void update()	{
		this.backend.update();
	}
	public void setColors(int[][][] rgb)	{
		this.backend.setRGB(rgb);
	}
	public int[][][] getColors()	{
		return this.backend.getBuffer();
	}
	public void setColor(int x,int y,int[] color)	{
		this.backend.setColor(x, y, color);
	}
	public void setColor(int x,int y,int r,int g,int b)	{
		this.backend.setColor(x, y, r, g, b);
	}
	public int[] getColorAt(int x,int y)	{
		return backend.getBuffer()[y][x];
	}
	public void sleep(long ms)	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public void setBoardColor(int r,int g,int b)	{
		this.setBoardColor(new int[] {r,g,b});
	}
	public void setBoardColor(int[] color)	{
		int[][][] buffer = this.backend.getBuffer();
		for (int Y = 0;Y < buffer.length;Y++)	{
			for (int X = 0;X < buffer[Y].length;X++)	{
				buffer[Y][X] = color;
			}
		}
	}
	public void setBackgroundColor(int[] color)	{
		this.back = color.clone();
	}
	public int[] getBackgroundColor()	{
		return this.back.clone();
	}
	public void reset()	{
		int[][][] buffer = this.backend.getBuffer();
		int[] backclone = this.back.clone();
		for (int Y = 0;Y < buffer.length;Y++)	{
			for (int X = 0;X < buffer[Y].length;X++)	{
				buffer[Y][X] = backclone;
			}
		}
	}
	public void resetBoard()	{
		this.reset();
	}
	public void addColor(int x,int y,int[] color)	{
		int[][][] buffer = this.backend.getBuffer();
		buffer[y][x][0] += color[0];
		buffer[y][x][1] += color[1];
		buffer[y][x][2] += color[2];
		if (buffer[y][x][0] > 255)
			buffer[y][x][0] = 255;
		if (buffer[y][x][1] > 255)
			buffer[y][x][1] = 255;
		if (buffer[y][x][2] > 255)
			buffer[y][x][2] = 255;
	}
	public void addColor(int x,int y,int r,int g,int b)	{
		this.addColor(x, y, new int[] {r,g,b});
	}
	
	@Override
	public void close() throws IOException {
		if (this.backend != null)
			this.backend.close();
	}
	
}
