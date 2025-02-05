package common;

/**
 * カードゲームの共通定数。
 * @author K Kawamura
 */
public final class CardConst {

	/** ゲーム結果保存ファイル。 */
	public static final String GAME_RESULT_FILE = "./file/result.txt";

	/** トランプ記号：ハート。 */
	public static final String TRUMP_CALL_HEART = "♥";
	/** トランプ記号：ダイヤ。 */
	public static final String TRUMP_CALL_DIAGRAM = "♦";
	/** トランプ記号：スペード。 */
	public static final String TRUMP_CALL_SPADE = "♠";
	/** トランプ記号：クローバー。 */
	public static final String TRUMP_CALL_CLOVER = "♣";
	/** トランプ記号：ジョーカー。 */
	public static final String TRUMP_CALL_JOKER = "☠";

	/** カード：トランプ。 */
	public static final String CARD_TRUMP[][] = {
		{ "♥A", "♥2", "♥3", "♥4", "♥5", "♥6", "♥7", "♥8", "♥9", "♥10", "♥J", "♥Q", "♥K" },
		{ "♦A", "♦2", "♦3", "♦4", "♦5", "♦6", "♦7", "♦8", "♦9", "♦10", "♦J", "♦Q", "♦K" },
		{ "♠A", "♠2", "♠3", "♠4", "♠5", "♠6", "♠7", "♠8", "♠9", "♠10", "♠J", "♠Q", "♠K" },
		{ "♣A", "♣2", "♣3", "♣4", "♣5", "♣6", "♣7", "♣8", "♣9", "♣10", "♣J", "♣Q", "♣K" }
	};

	/** カード：トランプ(Joker)。 */
	public static final String CARD_TRUMP_JOKER = "☠Jorker";

	/** カード総枚数。 */
	public static final int CARD_NUM_MAX = 52;
	/** カード枚数。 */
	public static final int CARD_NUM_HALF = 26;
	/** アプリスタートメッセージ*/
	public static final String APP_START_MSG = "実行した処理の番号を選んでください";
	/** アプリスタートメッセージ*/
	public static final String APP_START_MSG_1 = "1:ババ抜きを始める";
	/** アプリスタートメッセージ*/
	public static final String APP_START_MSG_2 = "2:成績を参照する";
	/** アプリスタートメッセージ*/
	public static final String APP_START_MSG_3 = "3:処理を終了する";
	/** 表示メッセージ：ゲーム開始時メッセージ。 */
	public static final String GAME_START_MSG = "Starting Game Application.";
	/** 表示メッセージ：ゲーム終了時メッセージ。 */
	public static final String GAME_END_MSG = "Endinßg Game Application.";

	/** 表示メッセージ：手札メッセージ(USER)。 */
	public static final String MSG_USER_CARD = "USER'S CARD.";
	/** 表示メッセージ：手札メッセージ(COM)。 */
	public static final String MSG_COM_CARD = "COM'S CARD.";

	/** 表示メッセージ：ターンメッセージ(USER)。 */
	public static final String TURN_MSG_USER = "USER'S TURN.";
	/** 表示メッセージ：ターンメッセージ(COM)。 */
	public static final String TURN_MSG_COM = "COM'S TURN.";

	/** 表示メッセージ：勝利時メッセージ。 */
	public static final String WIN_MSG = "USERの勝利です";
	/** 表示メッセージ：敗北時メッセージ。 */
	public static final String LOSE_MSG = "COMの勝利です";

	/** 表示メッセージ：成績参照時メッセージ。 */
	public static final String MSG_SEE_RESULTS = "過去の成績を参照します";
	/** 表示メッセージ：アプリケーション終了時メッセージ。 */
	public static final String MSG_EXIT_AP = "アプリケーションを終了します";
	/** 表示メッセージ：処理番号氏提示メッセージ。 */
	public static final String MSG_SEQ_NUM = "処理番号は1～3の数字で指定してください";

	/** ファイル入力不正時メッセージ。 */
	public static final String ERR_MSG_FILE_IN = "ファイル読み込み時に不正な処理が行われました";
	/** ファイル出力不正時メッセージ。 */
	public static final String ERR_MSG_FILE_OUT = "ファイル書き込み時に不正な処理が行われました";
	

}
