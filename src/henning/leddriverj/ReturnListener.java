package henning.leddriverj;

import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;

import henning.leddriverj.util.Log;

public class ReturnListener implements SerialDataEventListener {
	
	@SuppressWarnings("exports")
	@Override
	public void dataReceived(SerialDataEvent event) {
		synchronized (this)	{
			this.notifyAll();
		}
	}
	
	public void waitForReturn()	{
		synchronized (this)	{
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				Log.error(e);
			}
		}
	}
	
}
