package listenner;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import main.MainFrame;

import tools.LayBomb;
import tools.StaticTool;

import bean.HeroBean;
import bean.MineLable;

public class Listener implements MouseListener {
	MineLable[][] mineLable;
	MainFrame mainFrame;
	private boolean isDoublePress = false;

	public Listener(MineLable[][] mineLable, MainFrame mainFrame) {
		this.mineLable = mineLable;
		this.mainFrame = mainFrame;

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		MineLable mineLable = (MineLable) e.getSource();

		int row = mineLable.getRowx();//鼠标位置监听
		int col = mineLable.getColy();

		if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK//左键点击事件
				+ InputEvent.BUTTON3_DOWN_MASK) {
			isDoublePress = true;
			doublePress(row, col);

		} else if (e.getModifiers() == InputEvent.BUTTON1_MASK
				&& !mineLable.isFlagTag()) {
			if (!mineLable.isExpendTag()) {
				mineLable.setIcon(StaticTool.icon0);

			}
			mainFrame.getFaceJPanel().getLabelFace()
					.setIcon(StaticTool.clickIcon);//左键点击后表情更换
		} else if (e.getModifiers() == InputEvent.BUTTON3_MASK//右键点击事件
				&& !mineLable.isExpendTag()) {
			if (mineLable.getRightClickCount() == 0) {
				mineLable.setIcon(StaticTool.flagIcon);//棋子设置
				mineLable.setRightClickCount(1);
				mineLable.setFlagTag(true);
				StaticTool.bombCount--;//雷数量减一
				mainFrame.getFaceJPanel().setNumber(StaticTool.bombCount);

			} else if (mineLable.getRightClickCount() == 1) {//右键两次标记，旗子变成问号
				mineLable.setIcon(StaticTool.askIcon);
				mineLable.setRightClickCount(2);
				mineLable.setFlagTag(false);
				StaticTool.bombCount++;
				mainFrame.getFaceJPanel().setNumber(StaticTool.bombCount);

			} else {//多次标记则变回原来背景，并清空计数
				mineLable.setIcon(StaticTool.iconBlank);
				mineLable.setRightClickCount(0);
			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		MineLable mineLable = (MineLable) e.getSource();
		int row = mineLable.getRowx();//鼠标在雷区内点击的坐标
		int col = mineLable.getColy();
		if (isDoublePress) {
			isDoublePress = false;
			if (!mineLable.isExpendTag()
					&& !mineLable.isFlagTag()) {
				backIcon(row, col);
			} else {

				boolean isEquals = isEquals(row, col);
				if (isEquals) {
					doubleExpend(row, col);
				} else {
					backIcon(row, col);

				}

			}
			mainFrame.getFaceJPanel().getLabelFace()
					.setIcon(StaticTool.smileIcon);//设置笑脸

		} else if (e.getModifiers() == InputEvent.BUTTON1_MASK//左键弹起
				&& !mineLable.isFlagTag()) {
			if (!StaticTool.isStart) {//判断游戏开始
				LayBomb.lay(this.mineLable, row, col);//布置地雷
				StaticTool.isStart = true;//表示不是第一次点击

			}
			mainFrame.getTimer().start();//计时器开始计时间

			if (mineLable.isMineTag()) {//弹起再判断是否是雷，如果是则结束游戏
				bombAction(row, col);
				mineLable.setIcon(StaticTool.bloodIcon);//显示所有雷
				mainFrame.getFaceJPanel().getLabelFace()
						.setIcon(StaticTool.faultFaceIcon);
			} else {
				mainFrame.getFaceJPanel().getLabelFace()
						.setIcon(StaticTool.smileIcon);
				expand(row, col);
			}
		}
		isWin();//判断雷是否清完
	}

	private void bombAction(int row, int col) {//踩雷后，显示红雷，显示错误标记旗帜
		for (MineLable[] mineLables : mineLable) {
			for (MineLable lable : mineLables) {
				if (lable.isMineTag()) {
					if (!lable.isFlagTag()) {//显示雷
						lable.setIcon(StaticTool.blackBombIcon);
					}
				} else {
					if (lable.isFlagTag()) {//红雷
						lable.setIcon(StaticTool.errorBombIcon);
					}
				}
			}

		}

		mainFrame.getTimer().stop();

		for (MineLable[] mineLables : mineLable) {
			for (MineLable lable : mineLables) {
				lable.removeMouseListener(this);
			}
		}

	}

	private void expand(int x, int y) {//填充空白或者数字
		int count = mineLable[x][y].getCounAround();//获取点击位置周围的情况
		if (!mineLable[x][y].isExpendTag()
				&& !mineLable[x][y].isFlagTag()) {
			if (count == 0) {
				mineLable[x][y].setIcon(StaticTool.num[count]);
				mineLable[x][y].setExpendTag(true);
				for (int i = Math.max(0, x - 1); i <= Math.min(
						mineLable.length - 1, x + 1); i++) {
					for (int j = Math.max(0, y - 1); j <= Math.min(
							mineLable[x].length - 1, y + 1); j++) {
						expand(i, j);
					}
				}

			} else {
				mineLable[x][y].setIcon(StaticTool.num[count]);
				mineLable[x][y].setExpendTag(true);
			}

		}

	}

	private void backIcon(int i, int j) {//越界后的数字标记
		for (int x = Math.max(0, i - 1); x <= Math.min(StaticTool.allrow - 1,
				i + 1); x++) {
			for (int y = Math.max(0, j - 1); y <= Math.min(
					StaticTool.allcol - 1, j + 1); y++) {
				if (!mineLable[x][y].isFlagTag()
						&& !mineLable[x][y].isExpendTag()) {
					int rightClickCount = mineLable[x][y].getRightClickCount();
					if (rightClickCount == 2) {
						mineLable[x][y].setIcon(StaticTool.askIcon);
					} else {
						mineLable[x][y].setIcon(StaticTool.iconBlank);

					}
				}
			}
		}

	}

	private boolean isEquals(int i, int j) {
		int count = mineLable[i][j].getCounAround();
		int flagCount = 0;
		for (int x = Math.max(0, i - 1); x <= Math.min(StaticTool.allrow - 1,
				i + 1); x++) {
			for (int y = Math.max(0, j - 1); y <= Math.min(
					StaticTool.allcol - 1, j + 1); y++) {
				if (mineLable[x][y].isFlagTag()) {
					flagCount++;
				}
			}
		}
		return count == flagCount;
	}

	private void doublePress(int i, int j) {
		for (int x = Math.max(0, i - 1); x <= Math.min(StaticTool.allrow - 1,
				i + 1); x++) {
			for (int y = Math.max(0, j - 1); y <= Math.min(
					StaticTool.allcol - 1, j + 1); y++) {
				if (!mineLable[x][y].isExpendTag()
						&& !mineLable[x][y].isFlagTag()) {
					int rightClickCount = mineLable[x][y].getRightClickCount();
					if (rightClickCount == 1) {
						mineLable[x][y].setIcon(StaticTool.askPressIcon);

					} else {
						mineLable[x][y].setIcon(StaticTool.icon0);

					}
				}
			}
		}
	}

	private void doubleExpend(int i, int j) {
		for (int x = Math.max(0, i - 1); x <= Math.min(StaticTool.allrow - 1,
				i + 1); x++) {
			for (int y = Math.max(0, j - 1); y <= Math.min(
					StaticTool.allcol - 1, j + 1); y++) {
				if (mineLable[x][y].isMineTag()) {
					if (!mineLable[x][y].isFlagTag()) {
						bombAction(x, y);

					}
				} else {

					if (!mineLable[x][y].isFlagTag()) {
						expand(x, y);
					}

				}

			}
		}

	}

	private void isWin() {//雷排干净后，输入用户信息
		int needCount = StaticTool.allrow * StaticTool.allcol
				- StaticTool.allcount;
		int expendCount = 0;
		for (MineLable[] mineLables : mineLable) {
			for (MineLable lable : mineLables) {
				if (lable.isExpendTag()) {
					expendCount++;
				}
			}

		}
		if (needCount == expendCount) {
			for (MineLable[] lables : mineLable) {
				for (MineLable lable : lables) {
					if (lable.isMineTag()
							&& !lable.isFlagTag()) {
						lable.setIcon(StaticTool.flagIcon);
						lable.setFlagTag(true);
					}

				}

			}

			mainFrame.getFaceJPanel().setNumber(0);
			mainFrame.getTimer().stop();
			for (MineLable[] mineLables : mineLable) {
				for (MineLable lable : mineLables) {
					lable.removeMouseListener(this);

				}
			}

			mainFrame.getFaceJPanel().getLabelFace()
					.setIcon(StaticTool.winFaceIcon);
			int level = StaticTool.getLevel();
			if (level != 0) {
				if (level == 1) {
					String name = JOptionPane.showInputDialog(mainFrame,
							"好厉害！初级扫雷完成，请留下大名！");
					if (name != null) {
						StaticTool.treeSetC.add(new HeroBean(
								StaticTool.timecount, name));
					}
				} else if (level == 2) {
					String name = JOptionPane.showInputDialog(mainFrame,
							"好厉害！中级扫雷完成，请留下大名！");
					if (name != null) {
						StaticTool.treeSetZ.add(new HeroBean(
								StaticTool.timecount, name));
					}
				} else if (level == 3) {
					String name = JOptionPane.showInputDialog(mainFrame,
							"好厉害！高级扫雷完成，请留下大名！");
					if (name != null) {
						StaticTool.treeSetG.add(new HeroBean(
								StaticTool.timecount, name));
					}
				}

			}

		}

	}

}
