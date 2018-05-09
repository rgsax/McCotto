import javax.swing.JFrame;

import graphics.Window.Window;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	public static void main(String[] args) {
		MainFrame f = new MainFrame(800, 800);
		f.setVisible(true);
	}
	
	public MainFrame(int w, int h) {
		super();
		setSize(w, h);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setContentPane(new Window());
		setTitle("Gioco");
	}
}
