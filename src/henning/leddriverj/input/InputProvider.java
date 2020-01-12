package henning.leddriverj.input;

import java.io.Closeable;

public interface InputProvider extends Closeable {
	
	public boolean hasKey();
	public int getLastKey();
	public default boolean getOptionalKeyPressed(int keycode)	{
		return false;
	}
	
}
