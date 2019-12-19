package henning.leddriverj;

import java.io.IOException;
import java.util.Random;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException	{
		@SuppressWarnings("resource")
		BoardController c = new BoardController(LEDController.DEFAULT_WIDTH, LEDController.DEFAULT_HIEGHT);
		Random r = new Random();
		int l = 0;
		while (true)	{
			c.setColors(generateRandom(r));
			c.update();
			Thread.sleep(200);
			l++;
			if ((l/10) % 2 == 0)
				c.getController().setIntensity(1.5f);
			else
				c.getController().setIntensity(1.0f);
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
		for (int C = 0;C < 3;C++)	{
			int i = r.nextInt(4) * 64;
			color[C] = i;
		}
		return color;
	}
	
}
