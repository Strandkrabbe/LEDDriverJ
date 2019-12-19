package henning.leddriverj;

import java.io.IOException;

public class SyncLEDController extends LEDController {

	protected Transmitter tr;
	
	public SyncLEDController(int width, int height) throws IOException {
		super(width, height);
		this.tr = new Transmitter();
	}

	@Override
	public void clear() {
		tr.clear();
	}

	@Override
	public void setAlpha(int alphaDiv) {
		tr.setAlpha(alphaDiv);
	}

	@Override
	protected void writeRGB(int[][][] rgb) {
		tr.setRGB(this.rgb);
	}
	
	@Override
	public void close() throws IOException {
		this.tr.close();
	}
	
}
