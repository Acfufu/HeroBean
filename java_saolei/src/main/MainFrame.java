package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Serial;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import panel.BombJMenuBar;
import panel.BombJPanel;
import panel.FaceJPanel;
import timer.TimerListener;
import tools.StaticTool;

public class MainFrame extends JFrame {
	@Serial
	private static final long serialVersionUID = 1L;
	private BombJMenuBar menuBar = new BombJMenuBar(this);
	private FaceJPanel faceJPanel = new FaceJPanel(this);
	private BombJPanel bombJPanel = new BombJPanel(this);
	private TimerListener timerListener = new TimerListener(this);
	private Timer timer = new Timer(1000, timerListener);

	public MainFrame() {
		init();
		this.setIconImage(StaticTool.imageIcon.getImage());
		this.setTitle("扫雷");
		this.setSize(new Dimension(220, 300));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.pack();
		this.setVisible(true);
	}

	private void init() {
		this.setJMenuBar(menuBar);
		this.add(faceJPanel, BorderLayout.NORTH);
		this.add(bombJPanel);

	}

	public void reStartGame() {//重启游戏
		this.remove(faceJPanel);
		this.remove(bombJPanel);//更新信息
		StaticTool.bombCount = StaticTool.allcount;
		StaticTool.timecount = 0;
		StaticTool.isStart = false;

		faceJPanel = new FaceJPanel(this);
		bombJPanel = new BombJPanel(this);
		this.add(faceJPanel, BorderLayout.NORTH);
		this.add(bombJPanel);
		this.pack();
		this.validate();

		getTimer().stop();

	}

	public FaceJPanel getFaceJPanel() {
		return faceJPanel;
	}

	public BombJPanel getBombJPanel() {
		return bombJPanel;
	}

	public Timer getTimer() {
		return timer;
	}


	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		new MainFrame();

	}

}
