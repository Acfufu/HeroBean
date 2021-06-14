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

		int row = mineLable.getRowx();//���λ�ü���
		int col = mineLable.getColy();

		if (e.getModifiersEx() == InputEvent.BUTTON1_DOWN_MASK//�������¼�
				+ InputEvent.BUTTON3_DOWN_MASK) {
			isDoublePress = true;
			doublePress(row, col);

		} else if (e.getModifiers() == InputEvent.BUTTON1_MASK
				&& !mineLable.isFlagTag()) {
			if (!mineLable.isExpendTag()) {
				mineLable.setIcon(StaticTool.icon0);

			}
			mainFrame.getFaceJPanel().getLabelFace()
					.setIcon(StaticTool.clickIcon);//��������������
		} else if (e.getModifiers() == InputEvent.BUTTON3_MASK//�Ҽ�����¼�
				&& !mineLable.isExpendTag()) {
			if (mineLable.getRightClickCount() == 0) {
				mineLable.setIcon(StaticTool.flagIcon);//��������
				mineLable.setRightClickCount(1);
				mineLable.setFlagTag(true);
				StaticTool.bombCount--;//��������һ
				mainFrame.getFaceJPanel().setNumber(StaticTool.bombCount);

			} else if (mineLable.getRightClickCount() == 1) {//�Ҽ����α�ǣ����ӱ���ʺ�
				mineLable.setIcon(StaticTool.askIcon);
				mineLable.setRightClickCount(2);
				mineLable.setFlagTag(false);
				StaticTool.bombCount++;
				mainFrame.getFaceJPanel().setNumber(StaticTool.bombCount);

			} else {//��α������ԭ������������ռ���
				mineLable.setIcon(StaticTool.iconBlank);
				mineLable.setRightClickCount(0);
			}

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {

		MineLable mineLable = (MineLable) e.getSource();
		int row = mineLable.getRowx();//����������ڵ��������
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
					.setIcon(StaticTool.smileIcon);//����Ц��

		} else if (e.getModifiers() == InputEvent.BUTTON1_MASK//�������
				&& !mineLable.isFlagTag()) {
			if (!StaticTool.isStart) {//�ж���Ϸ��ʼ
				LayBomb.lay(this.mineLable, row, col);//���õ���
				StaticTool.isStart = true;//��ʾ���ǵ�һ�ε��

			}
			mainFrame.getTimer().start();//��ʱ����ʼ��ʱ��

			if (mineLable.isMineTag()) {//�������ж��Ƿ����ף�������������Ϸ
				bombAction(row, col);
				mineLable.setIcon(StaticTool.bloodIcon);//��ʾ������
				mainFrame.getFaceJPanel().getLabelFace()
						.setIcon(StaticTool.faultFaceIcon);
			} else {
				mainFrame.getFaceJPanel().getLabelFace()
						.setIcon(StaticTool.smileIcon);
				expand(row, col);
			}
		}
		isWin();//�ж����Ƿ�����
	}

	private void bombAction(int row, int col) {//���׺���ʾ���ף���ʾ����������
		for (MineLable[] mineLables : mineLable) {
			for (MineLable lable : mineLables) {
				if (lable.isMineTag()) {
					if (!lable.isFlagTag()) {//��ʾ��
						lable.setIcon(StaticTool.blackBombIcon);
					}
				} else {
					if (lable.isFlagTag()) {//����
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

	private void expand(int x, int y) {//���հ׻�������
		int count = mineLable[x][y].getCounAround();//��ȡ���λ����Χ�����
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

	private void backIcon(int i, int j) {//Խ�������ֱ��
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

	private void isWin() {//���Ÿɾ��������û���Ϣ
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
							"������������ɨ����ɣ������´�����");
					if (name != null) {
						StaticTool.treeSetC.add(new HeroBean(
								StaticTool.timecount, name));
					}
				} else if (level == 2) {
					String name = JOptionPane.showInputDialog(mainFrame,
							"���������м�ɨ����ɣ������´�����");
					if (name != null) {
						StaticTool.treeSetZ.add(new HeroBean(
								StaticTool.timecount, name));
					}
				} else if (level == 3) {
					String name = JOptionPane.showInputDialog(mainFrame,
							"���������߼�ɨ����ɣ������´�����");
					if (name != null) {
						StaticTool.treeSetG.add(new HeroBean(
								StaticTool.timecount, name));
					}
				}

			}

		}

	}

}
