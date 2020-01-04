package henning.leddriverj;

import java.io.IOException;
import java.util.Random;

@SuppressWarnings("unused")
public class Test {

	public static void main(String[] args) throws IOException, InterruptedException	{
		@SuppressWarnings("resource")
		BoardController c = new BoardController(LEDController.DEFAULT_WIDTH, LEDController.DEFAULT_HIEGHT);
		// ** Settings
		c.getController().setAlpha(2);
		c.getController().useBrightnessMod(true);
		c.getController().setIntensity(new float[] {1.0f,1.0f,1.0f});
		// **
		Random r = new Random();
		int l = 0;
		while (true)	{
//			c.setColors(generateRandom(r));
//			c.update();
//			Thread.sleep(200);
			c.setColors(drawRing(l++, LEDController.DEFAULT_WIDTH, LEDController.DEFAULT_HIEGHT));
			c.update();
			c.sleep(200);
		}
	}
	private static int[][][] generateRandom(Random r)	{
		int[][][] buff = new int[LEDController.DEFAULT_HIEGHT][LEDController.DEFAULT_WIDTH][3];
		for (int Y = 0;Y < buff.length;Y++)	{
			for (int X = 0;X < buff[Y].length;X++)	{
				buff[Y][X] = randomColor(r);
			}
		}
		return buff;
	}
	private static int[] randomColor(Random r)	{
		int[] color = new int[3];
		int sum = 0;
		for (int C = 0;C < 3;C++)	{
			int i = r.nextInt(255);
			color[C] = i;
			sum += i;
		}
		if (sum > 200)	{
			sum = r.nextInt(4);
			if (sum != 0)	{
				color[sum - 1] = 0;
			}
		}
		return color;
	}
	
	private static final int[][] BASE_COLORS = {{255,0,0},{0,255,0},{0,0,255},{255,255,0},{0,255,255},{255,0,255}};
	
	private static int[][][] drawRing(int icolor,int w,int h)	{
		icolor = icolor%BASE_COLORS.length;
		int[][][] buff = new int[h][w][3];
		int mstage = (int) Math.ceil(((double)Math.min(w, h))/2);
		for (int C = 0;C < mstage;C++)	{
			for (int Y = C;Y < (h - C);Y++)	{
				for (int X = C;X < (w - C);X++)	{
					buff[Y][X] = BASE_COLORS[(C - icolor + BASE_COLORS.length)%BASE_COLORS.length];
				}
			}
		}
		return buff;
	}
	
}
