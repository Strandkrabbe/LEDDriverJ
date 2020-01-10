package henning.leddriverj;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import henning.leddriverj.util.Log;

public class FrameLEDController extends LEDController {

	private static final int BOX_SIZE = 25;
	private static final int BOX_SPACE = 2;
	
	private DrawingPanel dp;
	private JFrame frame;
	private boolean alphaMsg = false;
	
	public FrameLEDController(int width,int height) throws IOException {
		super(width, height);
		init();
	}
	public FrameLEDController() throws IOException {
		super();
		init();
	}
	private void init()	{
		Log.debug("Starting virtual controller", "FLC");
		this.dp = new DrawingPanel();
		this.dp.clear();
		this.frame = new JFrame();
		this.frame.setLayout(new GridLayout(1, 1));
		this.frame.add(this.dp);
		this.frame.pack();
		this.frame.setVisible(true);
		this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
	
	private class DrawingPanel extends JPanel {
		
		private static final long serialVersionUID = 326944192779154921L;
		private int[][][] state;
		
		public DrawingPanel()	{
			int dpw = FrameLEDController.this.getWidth();
			int dph = FrameLEDController.this.getHeight();
			this.state = new int[dph][dpw][3];
			int panel_w = BOX_SIZE*dpw + BOX_SPACE*(dpw - 1);
			int panel_h = BOX_SIZE*dph + BOX_SPACE*(dph - 1);
			Dimension d = new Dimension(panel_w, panel_h);
			this.setMinimumSize(d);
			this.setPreferredSize(d);
			this.setMaximumSize(d);
			this.setForeground(new Color(255,255,255));
			this.setBackground(new Color(0,0,0));
		}
		
		@Override
		public void paint(Graphics g) {
			synchronized (this) {
				super.paint(g);
				for (int Y = 0;Y < this.state.length;Y++)	{
					for (int X = 0;X < this.state[Y].length;X++)	{
						int cx = (BOX_SIZE+BOX_SPACE)*X;
						int cy = (BOX_SIZE+BOX_SPACE)*Y;
						Color cc = new Color(this.state[Y][X][0], this.state[Y][X][1], this.state[Y][X][2]);
						g.setColor(cc);
						g.fillRect(cx, cy, BOX_SIZE, BOX_SIZE);
					}
				}
			}
		}
		
		public void setState(int[][][] s)	{
			synchronized (this)	{
				for (int Y = 0;Y < s.length;Y++)	{
					for (int X = 0;X < s[Y].length;X++)	{
						for (int C = 0;C < 3;C++)	{
							this.state[Y][X][C] = s[Y][X][C];
							float mult = ((float)(255 - this.state[Y][X][C]))/255 + 1;
							this.state[Y][X][C] *= mult;
						}
					}
				}
			}
			this.repaint();
		}
		public void clear()	{
			synchronized (this) {
				for (int Y = 0;Y < state.length;Y++)	{
					for (int X = 0;X < state[Y].length;X++)	{
						for (int C = 0;C < 3;C++)	{
							this.state[Y][X][C] = 0; 
						}
					}
				}
			}
		}
		
	}

	@Override
	public void close() throws IOException {
		this.frame.setVisible(false);
		this.frame.dispose();
	}
	@Override
	public void clear() {
		this.dp.clear();
	}
	@Override
	public void setAlpha(int alphaDiv) {
		if (!this.alphaMsg)	{
			this.alphaMsg = true;
			Log.warn("Using alpha in virtual board is not allowed!!", "FLC");
		}
	}
	@Override
	protected void writeRGB(int[][][] rgb) {
		this.dp.setState(rgb);
	}
	
}
