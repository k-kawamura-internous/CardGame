package util;


/**
 * ゲームアプリケーション用インターフェース。
 * @author K Kawamura
 */
public interface GameInterface {

	/**
	 * カードゲームのメイン処理。
	 */
	abstract void execute();

	/**
	 * ババ抜きゲーム処理を行う。
	 */
	abstract void game();
}
