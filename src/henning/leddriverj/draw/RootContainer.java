package henning.leddriverj.draw;

import java.io.Closeable;
import java.io.IOException;

import henning.leddriverj.BoardController;
import henning.leddriverj.Mode;
import henning.leddriverj.util.Log;

public class RootContainer extends Container implements Closeable {

	private BoardController board;
	
	public RootContainer(int width,int height) throws IOException	{
		this.board = new BoardController(width, height);
	}
	public RootContainer(Mode m,int width,int height) throws IOException	{
		this.board = new BoardController(m,width, height);
	}
	
	@Override
	public int getX() {
		return 0;
	}
	@Override
	public int getY() {
		return 0;
	}
	@Override
	public int getWidth() {
		return this.board.getController().getWidth();
	}
	public int getHeight()	{
		return this.board.getController().getHeight();
	}
	
	public void draw()	{
		DrawingArea da = new DrawingArea(getWidth(), getHeight());
		boolean b = this.draw(da);
		if (b)	{
			this.board.setColors(da.getArea());
			this.board.update();
		}	else	{
			this.board.getController().clear();
		}
	}
	
	public BoardController getBoard()	{
		return this.board;
	}
	
	@Override
	public void close()	{
		try {
			this.board.close();
		} catch (IOException e) {
			Log.error(e);
			Log.error("Failed to close board", "RootContainer");
		}
	}
	
}
