package util;

import app.Babanuki;

/**
 * トランプゲーム用の起動クラス。
 * @author  K Kawamura
 */
public class TrumpApplication {
	/**
	 * 起動用メイン
	 *
	 * @param String[] args
	 * @return なし
	 */
	public static void main(String[] args) {
		// 1. Babanukiクラスをインスタンス化する。
		Babanuki b = new Babanuki();
		// 2. Babanukiクラスのexecuteメソッドを呼び出す。
		b.execute();

	}
}