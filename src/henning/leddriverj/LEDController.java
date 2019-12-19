package henning.leddriverj;

import java.io.Closeable;
import java.io.IOException;

public abstract class LEDController implements Closeable {
	// Implement sync and async controller
	
	public static final int DEFAULT_WIDTH = 17;
	public static final int DEFAULT_HIEGHT = 10;
	
	protected int[][][] rgb;
	private final int width;
	private final int height;
	
	public LEDController(int width,int height) throws IOException	{
		this.width = width;
		this.height = height;
		this.rgb = new int[height][width][3];
	}
	public LEDController() throws IOException	{
		this(DEFAULT_WIDTH,DEFAULT_HIEGHT);
	}
	
	/**
	 * Clear will not clear the current buffer. After an update the previvious picture is restored.<br>
	 * CMD-Typ: 0x01
	 */
	public abstract void clear();
	
	/**
	 * Will divide all colors by 2^n (shift right by n)<br>
	 * CMD-Type: 0x04
	 * @param alphaDiv
	 */
	public abstract void setAlpha(int alphaDiv);
	protected abstract void writeRGB(int[][][] rgb);
	
	/**
	 * Will set all bytes in the buffer to the given ones. Format: [y][x][c(0-2)]
	 * @param rgb
	 */
	public void setRGB(int[][][] rgb)	{	// Format rgb[Y][X] = c 
		for (int Y = 0;Y < this.height;Y++)	{
			for (int X = 0;X < this.width;X++)	{
				this.rgb[Y][X] = rgb[Y][X];	// TODO adjust my use x first for this input
			}
		}
	}
	// Draw area
	// Draw line
	// Draw/Fill square
	public void setColor(int x,int y,int[] color)	{
		if (color.length != 3)
			throw new IllegalArgumentException("Invalid color");
		this.rgb[y][x] = color.clone();
	}
	public void setColor(int x,int y,int r,int g,int b)		{
		this.rgb[y][x][0] = r;
		this.rgb[y][x][1] = g;
		this.rgb[y][x][2] = b;
	}
	/**
	 * Prints the buffer to the board<br>
	 * CMD-Type: 0x03
	 */
	public void update()	{
		writeRGB(rgb);
	}
	
	public int[][][] getBuffer()	{
		return this.rgb;
	}
	public int getWidth()	{
		return this.width;
	}
	public int getHeight()	{
		return this.height;
	}
	public int getTotalSize()	{
		return this.width * this.height;
	}
	
}
