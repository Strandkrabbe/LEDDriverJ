package henning.leddriverj.draw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import henning.leddriverj.util.Log;

public class Image extends BasicDrawable {
	
	private int[][][] rgb = null;
	
	public Image()	{
		this.setBackgrounColor(null);
	}
	
	public void load(InputStream i) throws IOException	{
		BufferedImage img = ImageIO.read(i);
		int height = img.getHeight();
		int width = img.getWidth();
		this.rgb = new int[height][width][3];
		for (int Y = 0;Y < height;Y++)	{
			for (int X = 0;X < width;X++)	{
				int rgbv = img.getRGB(X, Y);
				int[] rgb = new int[3];
				rgb[2] = rgbv & (0xFF);
				rgb[1] = (rgbv >> 8) & (0xFF);
				rgb[0] = (rgbv >> 16) & (0xFF);
				this.rgb[Y][X] = rgb;
			}
		}
	}
	public void load(File f)	{
		try {
			this.load(new FileInputStream(f));
		} catch (IOException e) {
			Log.error(e);
		}
	}
	public void load(String resource)	{
		InputStream ios = this.getClass().getResourceAsStream("/lx/ledgeo/res/" + resource);
		if (ios != null)
			try {
				this.load(ios);
			} catch (IOException e) {
				this.rgb = null;
				Log.error(e);
			}
		else	{
			this.rgb = null;
			Log.error("Unable to locate resource " + resource, "Image");
		}
	}
	public void loadRaw(String resource)	{
		InputStream ios = this.getClass().getResourceAsStream(resource);
		if (ios != null)
			try {
				this.load(ios);
			} catch (IOException e) {
				this.rgb = null;
				Log.error(e);
			}
		else	{
			this.rgb = null;
			Log.error("Unable to locate resource " + resource, "Image");
		}
	}
	
	@Override
	public boolean draw(DrawingArea a) {
		if (!super.draw(a))	{
			return false;
		}
		if (rgb == null)	{
			a.setColor(new int[] {255,0,255});
			for (int Y = 0;Y < this.getHeight();Y++)	{
				for (int X = 0;X < this.getWidth();X++)	{
					if (Y%2 == X%2)	{
						a.set(X, Y);
					}
				}
			}
		}	else	{
			a.draw(rgb, 0, 0);
		}
		return true;
	}
	@Override
	public ColorMode getColorMode() {
		return ColorMode.REPLACE;
	}
	
}
