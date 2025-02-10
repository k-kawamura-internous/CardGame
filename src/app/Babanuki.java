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

	@Override
	public void execute() {
		String startMsg = createStartMsg();
		System.out.println(startMsg);
		String line = handleMenuSelection();

		if ("1".equals(line)) {
			handleGameStart();
		} else if ("2".equals(line)) {
			handleResults();
		} else {
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
		
		// カードを精査する
		System.out.print(CardConst.MSG_CLEAR_CARD);
		userCards = CardsUtils.clearCards(userCards);
		CardsUtils.waitProcess(1000);
		System.out.println(CardConst.MSG_FINISHED);
		
		comCards = CardsUtils.clearCards(comCards);

		// カード情報を表示する
		CardsUtils.printCards(userCards);
		CardsUtils.waitProcess(1500);
		
		
		// ゲームが終わるまでターンを繰り返す
		while (true) {
			playTurn();
			if (checkGameOver()) {
				break;
			}
		}
		
		// 結果を取得する
		int result = Result.checkResult(userCards, comCards);
		boolean isWin = Result.checkWin(result);
		
		// 結果をファイルに書き出す
		String resultsMsg = Result.getResult(isWin);
		FileUtils.setGameResults(CardConst.GAME_RESULT_FILE, resultsMsg);
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
	 */
	private void playTurn() {
		// ユーザーのターン
		playUserTurn();
		CardsUtils.printCards(userCards);
		// COMのターン
		playComTurn();
		CardsUtils.printCards(userCards);
	}

	
	/**
	 * 	ゲームメニューを選択する。
	 */
	private String handleMenuSelection() {
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				line = reader.readLine();
				if ("1".equals(line) || "2".equals(line) || "3".equals(line)) {
					break;
				} else {
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
		String gameResultsMsg = FileUtils.getGameResults(CardConst.GAME_RESULT_FILE);
		System.out.println(gameResultsMsg);
	}

	/**
	 * ユーザーのターン。
	 */
	private void playUserTurn() {
		if (checkGameOver()) return;
		System.out.println(CardConst.MSG_TURN_USER);
		CardsUtils.printCOMCardsList(comCards);

		String line = null;
		int selected = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				line = reader.readLine();
				List<Integer> cardNoList = new ArrayList<>();
				for (int i = 0; i < comCards.size(); i++) {
					cardNoList.add(i + 1);
				}
				
				try {
					selected = Integer.parseInt(line);
					if (cardNoList.contains(selected)) {
						break;
					}
				} catch(NumberFormatException e) {
					//この後の処理でメッセージを出すので、ここでは出さない
					
				}
			
				String msg = CardConst.MSG_SELECT_CARD_NO.replaceAll("X", String.valueOf(comCards.size()));
				System.out.println(msg);
			}
		} catch (IOException e) {
			System.out.println(CardConst.ERR_MSG_INPUT);
		}

		String selectedCard = comCards.get(selected - 1);
		comCards.remove(selected - 1);
		userCards.add(selectedCard);
		userCards = CardsUtils.clearCards(userCards);
		Collections.shuffle(userCards);
	}

	/**
	 * COMのターン。
	 */
	private void playComTurn() {
		if (checkGameOver()) return;
		System.out.println(CardConst.MSG_TURN_COM);
		System.out.println(CardConst.MSG_SELECT_CARD);
		int selected = (int) (Math.random() * userCards.size());
		String selectedCard = userCards.get(selected);

		CardsUtils.waitProcess(1000);
		System.out.println(CardConst.MSG_FINISHED);
		userCards.remove(selected);
		comCards.add(selectedCard);

		comCards = CardsUtils.clearCards(comCards);
		Collections.shuffle(comCards);
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
