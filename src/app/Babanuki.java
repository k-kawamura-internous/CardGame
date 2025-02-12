package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import common.CardConst;
import util.CardsUtils;
import util.FileUtils;
import util.GameInterface; //GameInterfaceのメソッド
import util.Result;

/**
 * ババ抜きゲーム。
 * @author K Kawamura
 */
public class Babanuki implements GameInterface  {// GameInterfaceをオーバーライド
	private List<String> userCards;
	private List<String> comCards;
	
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	@Override
	public void execute() {
		// メニューを表示する
		String startMsg = createStartMsg();
		System.out.println(startMsg);
		// ユーザーにメニューを選択させる
		String line = handleMenuSelection();

		if ("1".equals(line)) {
			//ババ抜きを開始する
			handleGameStart();
		} else if ("2".equals(line)) {
			// ゲームの結果を表示する
			handleResults();
		} else {
			// ゲームを終了する
			System.out.println(CardConst.MSG_EXIT_AP);
		}
	}

	@Override
	public void game() {
		System.out.println(CardConst.MSG_GAME_START);
		System.out.println(CardConst.MSG_BABANUKI_START);

		// カードを配る
		userCards = new ArrayList<>();
		comCards = new ArrayList<>();
		Map<String, List<String>> cardsMap = CardsUtils.handOutTrump(userCards, comCards);
		userCards = cardsMap.get("userCards");
		comCards = cardsMap.get("comCards");
		
		// カード情報を表示する
		CardsUtils.printCards(userCards);
		CardsUtils.waitProcess(1500);
		
		// ユーザーのカードを精査する
		System.out.print(CardConst.MSG_CLEAR_CARD);
		userCards = CardsUtils.clearCards(userCards);

		// COMのカードを精査する
		comCards = CardsUtils.clearCards(comCards);

		CardsUtils.waitProcess(1000);
		System.out.println(CardConst.MSG_FINISHED);


		// カード情報を表示する
		CardsUtils.printCards(userCards);
		CardsUtils.waitProcess(1500);
		
		
		// ゲームが終わるまでターンを繰り返す
		boolean isGameOver = false;
		while (!isGameOver) {
			isGameOver = playTurn();
		}
		
		// 結果を取得する
		int result = Result.checkResult(userCards, comCards);
		boolean isWin = Result.checkWin(result);
		
		// 今回の結果を今までの記録に加算し、取得する
		String resultsMsg = Result.getResult(isWin);
		// 結果をファイルに書き出す
		FileUtils.setGameResults(CardConst.GAME_RESULT_FILE, resultsMsg);
		
		System.out.println(CardConst.MSG_GAME_END);
	}

	/**
	 * ゲームが終了しているか確認する。
	 * @return true:ゲーム終了,false:ゲーム続行
	 */
	private boolean checkGameOver() {
		int result = Result.checkResult(userCards, comCards);
		return result == 0 || result == 1;
	}

	/**
	 * ゲームのターンを管理する。
	 * @return true:ゲーム終了,false:ゲーム続行
	 */
	private boolean playTurn() {

		boolean isGameOver = checkGameOver();
		// ユーザー、COMどちらかの手札がなければ終了
		if (isGameOver) return true;
		
		// ユーザーのターン
		isGameOver = playUserTurn();
		//ユーザー、COMどちらかの手札がなければ終了
		if(isGameOver) return true;
		
		// COMのターン
		isGameOver = playComTurn();
		//ユーザー、COMどちらかの手札がなければ終了
		if(isGameOver) return true;
		
		return false;
	}

	
	/**
	 * ゲームメニューをユーザーに選択させる。
	 * @return ユーザーが選択したメニュー
	 */
	private String handleMenuSelection() {
		String line = null;
		try {
			
			// 1～3が入力されるまで繰り返す
			while (true) {
				line = reader.readLine();
				if ("1".equals(line) || "2".equals(line) || "3".equals(line)) {
					break;
				} else {
					// 1～3以外の数字が入力された場合
					System.out.println(CardConst.MSG_SEQ_NUM);
				}
			}
		} catch (IOException e) {
			System.out.println(CardConst.ERR_MSG_INPUT);
		}
		return line;
	}

	/**
	 * ババ抜きゲームを開始する。
	 */
	private void handleGameStart() {
		game();
	}

	/**
	 * ゲームの結果を参照する。
	 */
	private void handleResults() {
		System.out.println(CardConst.MSG_SEE_RESULTS);
		// ファイルからゲームの結果を読み込む
		String gameResultsMsg = FileUtils.getGameResults(CardConst.GAME_RESULT_FILE);
		System.out.println(gameResultsMsg);
	}

	/**
	 * ユーザーのターン。
	 * @return true:ゲーム終了,false:ゲーム続行
	 */
	private boolean playUserTurn() {
		System.out.println(CardConst.MSG_TURN_USER);
		//COMの手札を出力
		CardsUtils.printCOMCardsList(comCards);

		String line = null;
		int selected = 0;
		
		// とるカードを選択
		try {
			while (true) {
				line = reader.readLine();
				
				try {
					selected = Integer.parseInt(line);
					// 選択された数字が手札の番号のリストに含まれていたら、処理終了
					if (selected >= 1 && selected <= comCards.size()) {
						break;
					}
					// 手札の番号以外を入力された場合、メッセージを出力
					String selectMsg = CardConst.MSG_SELECT_CARD_NO.replaceAll("X", String.valueOf(comCards.size()));
					System.out.println(selectMsg);
					
				} catch(NumberFormatException e) {
					// 手札の番号以外を入力された場合、メッセージを出力
					String selectMsg = CardConst.MSG_SELECT_CARD_NO.replaceAll("X", String.valueOf(comCards.size()));
					System.out.println(selectMsg);
				}
			}
		} catch (IOException e) {
			System.out.println(CardConst.ERR_MSG_INPUT);
		}

		// 選択したカードをCOMの手札から移動させる
		String selectedCard = comCards.get(selected - 1);
		comCards.remove(selected - 1);
		userCards.add(selectedCard);
		
		// カードを精査する
		userCards = CardsUtils.clearCards(userCards);
		Collections.shuffle(userCards);
		
		// ゲーム終了か判定
		if(checkGameOver()) {
			//BufferedReaderをクローズする
			try {
				if(reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				System.out.println(CardConst.ERR_MSG_CLOSE);
			}
			
			return true;
		}
		
		// ユーザーの手札を表示
		CardsUtils.printCards(userCards);
		return false;
	}

	/**
	 * COMのターン。
	 * @return true:ゲーム終了,false:ゲーム続行
	 */
	private boolean playComTurn() {
		System.out.println(CardConst.MSG_TURN_COM);
		System.out.print(CardConst.MSG_SELECT_CARD);
		
		// とるカードを選択
		int selected = (int) (Math.random() * userCards.size());
		String selectedCard = userCards.get(selected);
		CardsUtils.waitProcess(1000);
		System.out.println(CardConst.MSG_FINISHED);
		
		// 選択したカードをユーザーの手札から移動させる
		userCards.remove(selected);
		comCards.add(selectedCard);

		// カードを精査する
		comCards = CardsUtils.clearCards(comCards);
		Collections.shuffle(comCards);

		// ゲーム終了か判定
		if(checkGameOver()) {
			return true;
		}
		
		// ユーザーの手札を表示
		CardsUtils.printCards(userCards);
		return false;
	}

	/**
	 * 実行時に出力するメッセージを取得する。
	 * @return 実行時に出力するメッセージ
	 */
	private String createStartMsg() {
		StringBuilder startMsg = new StringBuilder();
		startMsg.append(CardConst.MSG_APP_START).append("\n");
		startMsg.append(CardConst.MSG_APP_START_1).append("\n");
		startMsg.append(CardConst.MSG_APP_START_2).append("\n");
		startMsg.append(CardConst.MSG_APP_START_3).append("\n");
		return startMsg.toString();
	}
}
