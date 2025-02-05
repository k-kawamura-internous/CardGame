package util;


/**
 * ゲームアプリケーション用インターフェース。
 * @author K Kawamura
 */
public interface GameInterface {

	/**
	 * カードゲームのメイン処理です。
	 */
	abstract void execute();

	/**
	 * ババ抜きゲーム処理を行います。
	 */
	abstract void game();
}
