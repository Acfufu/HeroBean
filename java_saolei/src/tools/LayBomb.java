package tools;

import java.util.Random;

import bean.MineLable;

public class LayBomb {//布雷
	public static void lay(MineLable[][] lable, int row, int col) {
		int count = 0;
		Random random = new Random();
		while (count < StaticTool.allcount) {
			int x = random.nextInt(StaticTool.allrow);//用随机数生成
			int y = random.nextInt(StaticTool.allcol);
			if (!lable[x][y].isMineTag() && !(x == row && y == col)) {
				lable[x][y].setMineTag(true);
				lable[x][y].setCounAround(9);
				if (StaticTool.isHole) {//后门的原理：将雷标记出来
					lable[x][y].setIcon(StaticTool.holeIcon);
				}
				count++;
			}
		}
		computeBomb(lable);
	}

	public static void computeBomb(MineLable[][] lable) {
		for (int i = 0; i < lable.length; i++) {
			for (int j = 0; j < lable[i].length; j++) {
				if (!lable[i][j].isMineTag()) {
					int count = 0;
					for (int x = Math.max(0, i - 1); x <= Math.min(//雷布局
							StaticTool.allrow - 1, i + 1); x++) {
						for (int y = Math.max(0, j - 1); y <= Math.min(
								StaticTool.allcol - 1, j + 1); y++) {
							if (lable[x][y].isMineTag()) {
								count++;

							}
						}
					}
					lable[i][j].setCounAround(count);//雷周围的数字统计
				}
			}
		}
	}
}
