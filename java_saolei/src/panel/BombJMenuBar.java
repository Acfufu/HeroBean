package panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serial;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import dialog.AboutSweeping;
import dialog.HeroDialog;
import dialog.UserDefinedJDialog;

import tools.StaticTool;

import main.MainFrame;

public class BombJMenuBar extends JMenuBar {//游戏界面的菜单导航条
	@Serial
	private static final long serialVersionUID = 1L;
	JMenu menuGame = new JMenu("游戏(G)");
	JMenu menuHelp = new JMenu("帮助(H)");
	JMenuItem menuItemStart = new JMenuItem("开局");
	JMenuItem menuItemC = new JMenuItem("初级");
	JMenuItem menuItemZ = new JMenuItem("中级");
	JMenuItem menuItemG = new JMenuItem("高级");

	JMenu menuHero = new JMenu("英雄榜");
	JMenuItem menuHeroC = new JMenuItem("初级英雄榜");
	JMenuItem menuHeroZ = new JMenuItem("中级英雄榜");
	JMenuItem menuHeroG = new JMenuItem("高级英雄榜");
	JMenuItem menuItemCustom = new JMenuItem("自定义");
	JMenuItem menuItemExit = new JMenuItem("退出");

	JMenuItem menuItemAbout = new JMenuItem("关于扫雷");
	JMenuItem menuItemHole = new JMenuItem("后门进入");

	MainFrame mainFrame;

	public BombJMenuBar(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		init();
	}

	private void init() {
		menuGame.setMnemonic('G');// 设置快捷键 alt+ G 才会有效果
		menuHelp.setMnemonic('H');
		// 菜单项加入到菜单
		menuGame.add(menuItemStart);

		menuItemStart.addActionListener(e -> mainFrame.reStartGame());

		menuGame.addSeparator();// 菜单加入分割线

		menuGame.add(menuItemC);
		menuItemC.addActionListener(e -> {
			StaticTool.allrow = 9;//初级界面的宽
			StaticTool.allcol = 9;//初级界面的长
			StaticTool.allcount = 10;//初级界面的雷数量
			mainFrame.reStartGame();
		});

		menuGame.add(menuItemZ);
		menuItemZ.addActionListener(e -> {
			StaticTool.allrow = 16;//中级界面的宽
			StaticTool.allcol = 16;//中级界面的长
			StaticTool.allcount = 40;//中级界面的雷数量
			mainFrame.reStartGame();
		});

		menuGame.add(menuItemG);
		menuItemG.addActionListener(e -> {
			StaticTool.allrow = 16;//高级界面的宽
			StaticTool.allcol = 30;//高级界面的长
			StaticTool.allcount = 99;//高级界面的雷数量
			mainFrame.reStartGame();
		});
		menuGame.addSeparator();// 菜单加入分割线
		menuGame.add(menuItemCustom);
		menuItemCustom.addActionListener(e -> {
			new UserDefinedJDialog(mainFrame);//自定义雷
		});

		menuGame.addSeparator();// 菜单加入分割线
		menuGame.add(menuHero);
		menuHero.add(menuHeroC);
		menuHeroC.addActionListener(e -> new HeroDialog(1, mainFrame));
		menuHero.add(menuHeroZ);
		menuHeroZ.addActionListener(e -> new HeroDialog(2, mainFrame));
		menuHero.add(menuHeroG);
		menuHeroG.addActionListener(e -> new HeroDialog(3, mainFrame));

		menuGame.addSeparator();// 菜单加入分割线
		menuGame.add(menuItemExit);//退出按钮
		menuItemExit.addActionListener(e -> System.exit(JFrame.EXIT_ON_CLOSE));

		menuHelp.add(menuItemAbout);//关于扫雷
		menuItemAbout.addActionListener(e -> new AboutSweeping(mainFrame));
		menuHelp.add(menuItemHole);
		//后门模式（实际上就是把雷标记出来）
		menuItemHole.addActionListener(e -> {//更新点：在不重启游戏的情况下自由使用和关闭后门
			if (StaticTool.isHole = false){
				StaticTool.isHole = true;
				mainFrame.reStartGame();
				menuItemHole.setText("取消后门");
			}else{
				mainFrame.reStartGame();
				menuItemHole.setText("后门进入");
				StaticTool.isHole = false;
			}
		});
		// 菜单加入到菜单条
		this.add(menuGame);
		this.add(menuHelp);
	}

}
