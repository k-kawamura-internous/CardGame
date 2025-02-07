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
		
		
		Result r = new Result();
		// ゲームが終わるまでターンを繰り返す
		while (true) {
			playTurn(r);
			if (checkGameOver(r)) {
				break;
			}
		}

		// 結果をファイルに書き出す
		String message = r.getResult(r.checkResult(userCards, comCards));
		FileUtils.setGameResults(CardConst.GAME_RESULT_FILE, message);
	}

	/**
	 * ゲームが終了しているか確認する
	 * @param r 結果クラス
	 * @return true:ゲーム終了,false:ゲーム続行
	 */
	private boolean checkGameOver(Result r) {
		int finishedFlag = r.checkResult(userCards, comCards);
		return finishedFlag == 0 || finishedFlag == 1;
	}

	private void playTurn(Result r) {
		userTurn(r);
		CardsUtils.printCards(userCards);
		comTurn(r);
		CardsUtils.printCards(userCards);
	}

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
			System.out.println(CardConst.MSG_SEQ_NUM);
		}
		return line;
	}

	private void handleGameStart() {
		game();
	}

	private void handleResults() {
		System.out.println(CardConst.MSG_SEE_RESULTS);
		String message = FileUtils.getGameResults(CardConst.GAME_RESULT_FILE);
		System.out.println(message);
	}

	private void userTurn(Result r) {
		if (checkGameOver(r)) return;
		System.out.println(CardConst.MSG_TURN_USER);
		CardsUtils.printCOMCardsList(comCards);

		String line = null;
		int selected = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				line = reader.readLine();
				List<Integer> list = new ArrayList<>();
				for (int i = 0; i < comCards.size(); i++) {
					list.add(i + 1);
				}
				selected = Integer.parseInt(line);
				if (list.contains(selected)) {
					break;
				}
				String msg = CardConst.MSG_SELECT_CARD_NO.replaceAll("X", String.valueOf(comCards.size()));
				System.out.print(msg);
			}
		} catch (IOException e) {
			// 例外処理
		}

		String selectedCard = comCards.get(selected - 1);
		comCards.remove(selected - 1);
		userCards.add(selectedCard);
		userCards = CardsUtils.clearCards(userCards);
		Collections.shuffle(userCards);
	}

	private void comTurn(Result r) {
		if (checkGameOver(r)) return;
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

	private String createStartMsg() {
		StringBuilder startMsg = new StringBuilder();
		startMsg.append(CardConst.MSG_APP_START).append("\n");
		startMsg.append(CardConst.MSG_APP_START_1).append("\n");
		startMsg.append(CardConst.MSG_APP_START_2).append("\n");
		startMsg.append(CardConst.MSG_APP_START_3).append("\n");
		return startMsg.toString();
	}
}
