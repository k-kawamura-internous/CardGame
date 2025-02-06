package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import common.CardConst;
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
		// アプリケーションの起動メッセージの表示
		String startMsg = createStartMsg();
		System.out.println(startMsg);

		// ユーザーのメニュー選択を処理
		String line = handleMenuSelection();
		// 入力された選択に応じて処理を分岐
		if ("1".equals(line)) {
			// ゲームを開始
			handleGameStart();
		} else if ("2".equals(line)) {
			// 結果を表示
			handleResults();
		} else {
			// それ以外（3を選択された場合）
			System.out.println(CardConst.MSG_EXIT_AP);
		}
	}

	@Override
	public void game() {
		System.out.println(CardConst.MSG_GAME_START);
		System.out.println(CardConst.MSG_BABANUKI_START);

		// カードを配る
		dealCards();

		Result r = new Result();

		// ゲームが終わるまでターンを繰り返す
		while (true) {
			// プレイヤーとコンピュータのターンを処理
			playTurn(r);

			// 勝敗判定
			if (checkGameOver(r)) {
				break;
			}
		}

		// 結果をファイルに書き出す
		r.printResult(r.checkResult(userCards, comCards));
	}

	/**
	 * 勝敗判定
	 * @param r 結果クラス
	 * @return 勝敗
	 */
	private boolean checkGameOver(Result r) {
		int flag = r.checkResult(userCards, comCards);
	    return flag == 0 || flag == 1;
	}

	/**
	 * // プレイヤーとコンピュータのターンを処理
	 * @param r 結果クラス
	 */
	private void playTurn(Result r) {
		// ユーザーのターン
		userTurn(r);
		// 手札を表示
		printCards(userCards);

		// COMのターン
		comTurn(r);
		// 手札を表示
		printCards(userCards);
	}

	/**
	 * カードを配る。
	 */
	private void dealCards() {
		// トランプを配る
		handOutTrump();
		// 手札を表示する
		printCards(userCards);

		System.out.print(CardConst.MSG_CLEAR_CARD);
		userCards = clearCards(userCards);
		// 1秒間処理を止める
		waitProcess();
		System.out.println(CardConst.MSG_FINISHED);
		// 手札を表示する
		printCards(userCards);

		// COMのカードを精査する
		comCards = clearCards(comCards);
	}


	/**
	 * ユーザーのメニュー選択を処理する。
	 * @return 選択されたメニュー番号
	 */
	private String handleMenuSelection() {
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				// キーボードから文字が入力された場合、その文字列をlineに格納する
				line = reader.readLine();
				// 入力文字が正しい場合、このループを抜ける処理をこの下に記述する
				if("1".equals(line) || "2".equals(line)  || "3".equals(line) ) {
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

	/** 
	 * ゲームを開始する。
	 */
	private void handleGameStart() {
		game();
	}

	/**
	 * 結果を表示する。
	 */
	private void handleResults() {
		System.out.println(CardConst.MSG_SEE_RESULTS);
		Result r = new Result();
		String message = r.getGameResults(CardConst.GAME_RESULT_FILE);
		System.out.println(message);

	}


	/**
	 * １秒間処理をストップ。
	 */
	private void waitProcess() {
		try {
			// 1秒間、実行を停止する
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// 本課題では例外時の処理を考慮しない
			e.printStackTrace();
		}
	}
	/**
	 * アプリケーションの起動メッセージを表す文字列を返す。
	 * @param version バージョン
	 * @return 起動メッセージ
	 */
	private String createStartMsg() {
		StringBuilder startMsg = new StringBuilder();
		startMsg.append(CardConst.MSG_APP_START).append("\n");;
		startMsg.append(CardConst.MSG_APP_START_1).append("\n");
		startMsg.append(CardConst.MSG_APP_START_2).append("\n");
		startMsg.append(CardConst.MSG_APP_START_3).append("\n");
		return startMsg.toString();
	}

	/**
	 * トランプをユーザーとCOMに配布する。
	 */
	private void handOutTrump() {
		System.out.print(CardConst.MSG_CARD_HAND_OUT);
		// 配列を1次元にフラット化
		List<String> cards = new ArrayList<String>();
		for (String[] suit : CardConst.CARD_TRUMP) {
			cards.addAll(Arrays.asList(suit));
		}
		// シャッフルする
		Collections.shuffle(cards);
		// ユーザーとCOMにカードを配る
		userCards = new ArrayList<String>(cards.subList(0, CardConst.CARD_NUM_HALF));  // subListから新しいリストを作成
		comCards = new ArrayList<String>(cards.subList(CardConst.CARD_NUM_HALF, CardConst.CARD_NUM_MAX));  // 同様に
		// ジョーカーを配る
		int randomInt = (int) (Math.random() * 2);

		if(randomInt == 0) {
			// 0だったらユーザーにジョーカーを追加
			userCards.add(CardConst.TRUMP_CALL_JOKER);
			// ジョーカーの位置を変更するためにシャッフルする
			Collections.shuffle(comCards);
		} else {
			// 1だったらコンピューターにジョーカーを追加
			comCards.add(CardConst.TRUMP_CALL_JOKER);
			// ジョーカーの位置を変更するためにシャッフルする
			Collections.shuffle(comCards);
		}

		// 1秒間処理を止める
		waitProcess();
		System.out.println(CardConst.MSG_FINISHED);
	}
	/**
	 * カードを精査する（重複を削除する）。
	 * @param cards 手札
	 * @return 重複を削除した手札
	 */
	private List<String> clearCards(List<String> cards) {
		List<String> uniqueCards = new ArrayList<String>();
		for (String card : cards) {
			// トランプの記号を除いたカード名を作成
			String filteredCard = card.replace(CardConst.TRUMP_CALL_HEART, "")
					.replace(CardConst.TRUMP_CALL_DIAGRAM, "")
					.replace(CardConst.TRUMP_CALL_SPADE, "")
					.replace(CardConst.TRUMP_CALL_CLOVER, "")
					.replace(CardConst.TRUMP_CALL_JOKER, "");

			// ペアのカードを処理
			boolean isFound = false;
			for (int i = 0; i < uniqueCards.size(); i++) {
				String c = uniqueCards.get(i);
				String filtered = c.replace(CardConst.TRUMP_CALL_HEART, "")
						.replace(CardConst.TRUMP_CALL_DIAGRAM, "")
						.replace(CardConst.TRUMP_CALL_SPADE, "")
						.replace(CardConst.TRUMP_CALL_CLOVER, "")
						.replace(CardConst.TRUMP_CALL_JOKER, "");

				// 同じカードがあった場合、ペアを削除
				if (filtered.equals(filteredCard)) {
					uniqueCards.remove(i); // 要素削除
					isFound = true;
					break;
				}
			}
			// ペアが見つからなかった場合は追加
			if (!isFound) {
				uniqueCards.add(card);
			}
		}
		return uniqueCards;
	}

	/**
	 * 手札を表示する。
	 * @param cards　手札
	 */
	private void printCards(List<String> cards) {
		System.out.println(CardConst.MSG_USER_CARD);
		for(String card : cards) {
			System.out.print(card);
		}
		System.out.println();
	}

	/**
	 * ユーザーのターン
	 * @param r 結果クラス
	 */
	private void userTurn(Result r) {
		// どちらかのカードがなければ、処理終了
		if(checkGameOver(r)) { 
			return ;
		}
		
		System.out.println(CardConst.MSG_TURN_USER);
		// COMの手札の一覧を生成し、表示する
		printCOMCardsList();


		String line = null;
		int selected = 0;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				// キーボードから文字が入力された場合、その文字列をlineに格納する
				line = reader.readLine();
				// 入力文字が正しい場合、このループを抜ける処理をこの下に記述する
				List<Integer> list = new ArrayList<Integer>();
				for(int i=0; i<comCards.size(); i++) {
					list.add(i+1);
				}
				selected = Integer.parseInt(line);
				if(list.contains(selected)) {
					break;
				}
				String msg = CardConst.MSG_SELECT_CARD_NO.replaceAll("X", String.valueOf(comCards.size()));
				System.out.println(msg);

			}
		} catch (IOException e) {
			// 本課題では例外時の処理を考慮しない
		}

		String selectedCard = comCards.get(selected -1);
		comCards.remove(selected -1);
		userCards.add(selectedCard);
		// 手札を精査する
		userCards = clearCards(userCards);
		Collections.shuffle(userCards);
	}

	/**
	 * COMのターン。
	 * @param r 結果クラス
	 */
	private void comTurn(Result r) {
		// どちらかのカードがなければ、処理終了
		if(checkGameOver(r)) { 
			return ;
		}
		
		System.out.println(CardConst.MSG_TURN_COM);
		System.out.println(CardConst.MSG_SELECT_CARD);
		int selected =  (int) (Math.random() * userCards.size());
		String selectedCard = userCards.get(selected);

		// 1秒間処理を止める
		waitProcess();
		System.out.println(CardConst.MSG_FINISHED);
		userCards.remove(selected);
		comCards.add(selectedCard);

		// 手札を精査する
		comCards = clearCards(comCards);
		Collections.shuffle(comCards);
	}

	/**
	 * COMの手札の一覧を作成する。
	 * @return メッセージ
	 */
	private void printCOMCardsList() {
		System.out.println(CardConst.MSG_SLECT_COM_CARD);

		// 手札のサイズを取得
		int cardCount = comCards.size();

		// 手札の一覧を表示するためのメッセージを構築
		StringBuilder comCardsList = new StringBuilder();

		// 上部の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "+----+ ");
		appendNewLine(comCardsList);

		// 空白の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "|    | ");
		appendNewLine(comCardsList);

		// 番号を中央に配置
		for (int i = 0; i < cardCount; i++) {
			comCardsList.append(String.format("| %2d | ", i + 1)); // 番号を右揃えで表示
		}
		appendNewLine(comCardsList);

		// 空白の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "|    | ");
		appendNewLine(comCardsList);

		// 下部の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "+----+ ");
		appendNewLine(comCardsList);

		// 完成した一覧を表示
		System.out.println(comCardsList.toString());
	}

	/**
	 * StringBuilder に繰り返しメッセージを追加
	 * @param sb StringBuilder
	 * @param count 繰り返し回数
	 * @param message 追加するメッセージ
	 */
	private void appendRepeatedMessage(StringBuilder sb, int count, String message) {
		for (int i = 0; i < count; i++) {
			sb.append(message);
		}
	}

	/**
	 * 新しい行を追加
	 * @param sb StringBuilder
	 */
	private void appendNewLine(StringBuilder sb) {
		sb.append("\n");
	}
}
