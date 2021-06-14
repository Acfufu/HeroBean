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

public class BombJMenuBar extends JMenuBar {//��Ϸ����Ĳ˵�������
	@Serial
	private static final long serialVersionUID = 1L;
	JMenu menuGame = new JMenu("��Ϸ(G)");
	JMenu menuHelp = new JMenu("����(H)");
	JMenuItem menuItemStart = new JMenuItem("����");
	JMenuItem menuItemC = new JMenuItem("����");
	JMenuItem menuItemZ = new JMenuItem("�м�");
	JMenuItem menuItemG = new JMenuItem("�߼�");

	JMenu menuHero = new JMenu("Ӣ�۰�");
	JMenuItem menuHeroC = new JMenuItem("����Ӣ�۰�");
	JMenuItem menuHeroZ = new JMenuItem("�м�Ӣ�۰�");
	JMenuItem menuHeroG = new JMenuItem("�߼�Ӣ�۰�");
	JMenuItem menuItemCustom = new JMenuItem("�Զ���");
	JMenuItem menuItemExit = new JMenuItem("�˳�");

	JMenuItem menuItemAbout = new JMenuItem("����ɨ��");
	JMenuItem menuItemHole = new JMenuItem("���Ž���");

	MainFrame mainFrame;

	public BombJMenuBar(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		init();
	}

	private void init() {
		menuGame.setMnemonic('G');// ���ÿ�ݼ� alt+ G �Ż���Ч��
		menuHelp.setMnemonic('H');
		// �˵�����뵽�˵�
		menuGame.add(menuItemStart);

		menuItemStart.addActionListener(e -> mainFrame.reStartGame());

		menuGame.addSeparator();// �˵�����ָ���

		menuGame.add(menuItemC);
		menuItemC.addActionListener(e -> {
			StaticTool.allrow = 9;//��������Ŀ�
			StaticTool.allcol = 9;//��������ĳ�
			StaticTool.allcount = 10;//���������������
			mainFrame.reStartGame();
		});

		menuGame.add(menuItemZ);
		menuItemZ.addActionListener(e -> {
			StaticTool.allrow = 16;//�м�����Ŀ�
			StaticTool.allcol = 16;//�м�����ĳ�
			StaticTool.allcount = 40;//�м������������
			mainFrame.reStartGame();
		});

		menuGame.add(menuItemG);
		menuItemG.addActionListener(e -> {
			StaticTool.allrow = 16;//�߼�����Ŀ�
			StaticTool.allcol = 30;//�߼�����ĳ�
			StaticTool.allcount = 99;//�߼������������
			mainFrame.reStartGame();
		});
		menuGame.addSeparator();// �˵�����ָ���
		menuGame.add(menuItemCustom);
		menuItemCustom.addActionListener(e -> {
			new UserDefinedJDialog(mainFrame);//�Զ�����
		});

		menuGame.addSeparator();// �˵�����ָ���
		menuGame.add(menuHero);
		menuHero.add(menuHeroC);
		menuHeroC.addActionListener(e -> new HeroDialog(1, mainFrame));
		menuHero.add(menuHeroZ);
		menuHeroZ.addActionListener(e -> new HeroDialog(2, mainFrame));
		menuHero.add(menuHeroG);
		menuHeroG.addActionListener(e -> new HeroDialog(3, mainFrame));

		menuGame.addSeparator();// �˵�����ָ���
		menuGame.add(menuItemExit);//�˳���ť
		menuItemExit.addActionListener(e -> System.exit(JFrame.EXIT_ON_CLOSE));

		menuHelp.add(menuItemAbout);//����ɨ��
		menuItemAbout.addActionListener(e -> new AboutSweeping(mainFrame));
		menuHelp.add(menuItemHole);
		//����ģʽ��ʵ���Ͼ��ǰ��ױ�ǳ�����
		menuItemHole.addActionListener(e -> {//���µ㣺�ڲ�������Ϸ�����������ʹ�ú͹رպ���
			if (StaticTool.isHole = false){
				StaticTool.isHole = true;
				mainFrame.reStartGame();
				menuItemHole.setText("ȡ������");
			}else{
				mainFrame.reStartGame();
				menuItemHole.setText("���Ž���");
				StaticTool.isHole = false;
			}
		});
		// �˵����뵽�˵���
		this.add(menuGame);
		this.add(menuHelp);
	}

}
