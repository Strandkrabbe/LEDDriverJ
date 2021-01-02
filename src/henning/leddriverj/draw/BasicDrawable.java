package henning.leddriverj.draw;

public abstract class BasicDrawable implements Drawable {
	
	private int X;
	private int Y;
	private int width;
	private int height;
	private int[] backgroundColor = null;
	private boolean visible;
	
	public BasicDrawable()	{
		this.visible = true;
	}
	
	@Override
	public boolean draw(DrawingArea a) {
		if (!this.visible)
			return false;
		if (backgroundColor != null)	{
			a.fill(backgroundColor);
		}
		return true;
	}
	
	public void setPosition(int x,int y)	{
		this.X = x;
		this.Y = y;
	}
	public void setSize(int width,int height)	{
		this.width = width;
		this.height = height;
	}
	@Override
	public int getX() {
		return X;
	}
	@Override
	public int getY() {
		return Y;
	}
	@Override
	public int getWidth() {
		return width;
	}
	@Override
	public int getHeight() {
		return height;
	}
	/**
	 * If color is null background will be transparent
	 */
	public void setBackgroundColor(int[] color)	{
		this.backgroundColor = color;
	}
	
	public void setVisible(boolean v)	{
		this.visible = v;
	}
	
	public boolean isVisible()	{
		return this.visible;
	}
}
