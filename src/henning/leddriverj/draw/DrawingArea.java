package henning.leddriverj.draw;

public class DrawingArea {
	
	private int[][][] area;
	private int width;
	private int[] currentcolor;
	
	public DrawingArea(int width,int height)	{
		this.area = new int[height][width][3];
		this.width = width;
	}
	
	public void setColor(int[] color)	{
		this.currentcolor = color.clone();
	}
	
	public int[][][] getArea()	{
		return this.area;
	}
	
	private int round(double d)	{
		int i = (int) d;
		if ((d - i) > 0.5)	{
			return i + 1;
		}	else	{
			return i;
		}
	}
	public void set(int x,int y)	{
		if (x >= 0 && y >= 0 && x < width && y < this.area.length)	{
			this.area[y][x] = currentcolor;
		}
	}
	public void drawLine(int x1,int y1,int x2,int y2)	{
		int steps = Math.max(x2 - x1, y2 - y1);
		this.set(x1, y1);
		if (steps == 0)
			return;
		double stepY = (y2-y1)/steps;
		double stepX = (x2-x1)/steps;
		double cx = x1 + stepX;
		double cy = y1 + stepY;
		for (int C = 0;C < steps;C++)	{
			this.set(round(cx),round(cy));
			cx += stepX;
			cy += stepY;
		}
	}
	public void drawRect(int x,int y,int w,int h)	{
		int xe = x + w - 1;
		int ye = y + h - 1;
		for (int CY = y;CY <= ye;CY++)	{
			this.set(x, CY);
			this.set(xe, CY);
		}
		for (int CX = x + 1;CX < xe;CX++)	{
			this.set(CX, y);
			this.set(CX, ye);
		}
	}
	public void fillRect(int x,int y,int w,int h)	{
		int xe = x + w - 1;
		int ye = y + h - 1;
		for (int CY = y;CY <= ye;CY++)	{
			for (int CX = x;CX <= xe;CX++)	{
				this.set(CX,CY);
			}
		}
	}
	public void draw(int[][][] a2,int x,int y)	{
		for (int CY = 0;CY < a2.length;CY++)	{
			for (int CX = 0;CX < a2[CY].length;CX++)	{
				int XX = CX + x;
				int YY = CY + y;
				if (XX >= 0 && YY >= 0 && XX < this.width && YY < this.area.length)	{
					this.area[YY][XX] = a2[CY][CX];
				}
			}
		}
	}
	public void fill(int[] color)	{
		int[] cclone = color.clone();
		for (int Y = 0;Y < this.area.length;Y++)	{
			for (int X = 0;X < this.area[Y].length;X++)	{
				this.area[Y][X] = cclone;
			}
		}
	}
	public void drawInverse(int[][][] toInverse) {
		if (this.area.length == toInverse.length) {
			if(this.area[0].length == toInverse[0].length) {
				for(int i = 0; i < toInverse.length; i++) {
					for(int j = 0; j < toInverse[i].length; j++) {
						area[toInverse.length-i-1][j]=toInverse[i][j];
					}
				}
			}
		}
	}
	public void draw(Drawable d)	{
		DrawingArea da = new DrawingArea(d.getWidth(), d.getHeight());
		boolean vis = d.draw(da);
		if (vis)
			this.draw(da.area, d.getX(), d.getY());
	}
	/**
	 * <b>!!Draws only full sized drawables!!</b><br>
	 * x=0,y=0
	 * @param d
	 */
	public void drawInverse(Drawable d)	{
		DrawingArea da = new DrawingArea(d.getWidth(), d.getHeight());
		boolean vis = d.draw(da);
		if (vis)
			this.drawInverse(da.area);
	}
	// Draw text
}
