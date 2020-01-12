package henning.leddriverj.input;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

public class KeyInputFrame extends JFrame implements InputProvider {

	private static final long serialVersionUID = -8258684516373070844L;
	public static int MAX_BUFFER_SIZE = 64;
	
	private ConcurrentLinkedQueue<Integer> keybuffer;
	private Set<Integer> activeKeys = Collections.synchronizedSet(new HashSet<Integer>());
	
	public KeyInputFrame(MouseListener ml,KeyListener kl)	{
		Container content = this.getContentPane();
		JLabel msg = new JLabel("This window has to be focused!");
		msg.setForeground(new Color(255,0,0));
		msg.setFont(new Font(Font.MONOSPACED, Font.BOLD, 14));
		msg.setPreferredSize(new Dimension(1000,1000));
		content.setLayout(new GridLayout(1, 1));
		content.add(msg);
		this.setSize(1000, 600);
		if (ml != null)
			this.addMouseListener(ml);
		if (kl != null)
			this.addKeyListener(kl);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//		this.addWindowListener(new WindowAdapter() {
//			
//		});
		this.setVisible(true);
	}
	
	public KeyInputFrame() {
		this(null,null);
		this.keybuffer = new ConcurrentLinkedQueue<>();
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (keybuffer.size() < MAX_BUFFER_SIZE)
					keybuffer.add(e.getKeyCode());
				activeKeys.add(e.getKeyCode());
			}
			@Override
			public void keyReleased(KeyEvent e) {
				activeKeys.remove(e.getKeyCode());
			}
		});
	}
	
	@Override
	public int getLastKey()	{
		if (!keybuffer.isEmpty())
			return this.keybuffer.remove();
		else
			return -1;
	}
	@Override
	public boolean hasKey()	{
		return !this.keybuffer.isEmpty();
	}
	@Override
	public boolean getOptionalKeyPressed(int keycode) {
		return this.activeKeys.contains(keycode);
	}
	
	@Override
	public void close() throws IOException {
		this.setVisible(false);
		this.dispose();
	}
	
}
