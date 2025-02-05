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
import util.TrumpApplication; //.TrumpApplicationのメソッド

/**
 * ババ抜きゲーム
 * @author K Kawamura
 */
public class Babanuki extends TrumpApplication implements GameInterface  {//TrumpApplicationを継承　GameInterfaceをオーバーライド
	private List<String> userCards;
	private List<String> comCards;


	@Override
	public void execute() {
		// アプリケーションの起動メッセージの表示
		String startMsg = createStartMsg("1.0");
		System.out.println(startMsg);

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

			if("1".equals(line)) {
				// １を選択された場合、ゲーム
				game();
			} else if("2".equals(line)) {
				// 2を選択された場合
				System.out.println(CardConst.MSG_SEE_RESULTS);
				Result r = new Result();
				String message = r.getGameResults(CardConst.GAME_RESULT_FILE);
				System.out.println(message);

			} else {
				// それ以外(3を選択された場合）
				System.out.println(CardConst.MSG_EXIT_AP);
			}
		} catch (IOException e) {
			// 本課題では例外時の処理を考慮しない
		}
	}

	@Override
	public void game() {
		System.out.println(CardConst.MSG_GAME_START);
		System.out.println(CardConst.MSG_BABANUKI_START);
		
		// トランプを配る
		handOutTrump();
		// 手札を表示する
		printCards(userCards);

		System.out.print("カード精査中...");
		userCards = clearCards(userCards);
		try {
			// 1秒間、実行を停止する
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// 本課題では例外時の処理を考慮しない
			e.printStackTrace();
		}
		System.out.println(CardConst.MSG_FINISHED);
		// 手札を表示する
		printCards(userCards);

		// COMのカードを精査する
		comCards = clearCards(comCards);
		Result r = new Result();
		int flag = -1;
		while(true) {
			// ユーザーのターン
			userTurn();
			// 勝敗をチェック
			flag = r.checkResult(userCards, comCards);
			if(flag == 0 || flag == 1) {
				// 勝敗が決定したのでループから抜ける
				break;
			}
			// 手札を表示
			printCards(userCards);

			// COMのターン
			comTurn();
			// 勝敗をチェック
			flag = r.checkResult(userCards, comCards);
			if(flag == 0 || flag == 1) {
				// 勝敗が決定したのでループから抜ける
				break;
			}
			// 手札を表示
			printCards(userCards);
		}
		if(flag == 0) {
			System.out.println(CardConst.MSG_WIN);
		} else {
			System.out.println(CardConst.MSG_LOSE);
		}
		System.out.println(CardConst.MSG_GAME_END);

		String message = r.getGameResults(CardConst.GAME_RESULT_FILE);
		if(message == null) {
			if(flag == 0) {
				message = "1勝0敗";
			} else {
				message = "0勝1敗";
			}
		} else {
			// X勝Y敗 を X,Yに分断する
            String[] results = message.split("勝|敗");
            int winCount = Integer.parseInt(results[0]);
            int looseCount = Integer.parseInt(results[1]);

			if(flag == 0) {
				winCount++; // 勝った場合
			} else {
				looseCount++; // 負けた場合
			}

			StringBuilder resultMsg = new StringBuilder();
			resultMsg.append(winCount);
			resultMsg.append("勝");
			resultMsg.append(looseCount);
			resultMsg.append("敗");
			message = resultMsg.toString();

		}
		//ファイルに書き込む
		r.setGameResults(CardConst.GAME_RESULT_FILE, message);
	}
	
	/**
	 * アプリケーションの起動メッセージを表す文字列を返します。
	 * @param version バージョン
	 * @return 起動メッセージ
	 */
	private String createStartMsg(String version) {
		StringBuilder startMsg = new StringBuilder();
		startMsg.append(CardConst.MSG_APP_START);
		startMsg.append("\n");
		startMsg.append(CardConst.MSG_APP_START_1);
		startMsg.append("\n");
		startMsg.append(CardConst.MSG_APP_START_2);
		startMsg.append("\n");
		startMsg.append(CardConst.MSG_APP_START_3);
		startMsg.append("\n");
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
		try {
			// 1秒間、実行を停止する
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// 本課題では例外時の処理を考慮しない
			e.printStackTrace();
		}
		System.out.println(CardConst.MSG_FINISHED);
	}
	/**
	 * カードを精査する（重複を削除する）
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
	 * 手札を表示する
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
	 */
	private void userTurn() {
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
	 * COMのターン
	 */
	private void comTurn() {
		System.out.println(CardConst.MSG_TURN_COM);
		System.out.println(CardConst.MSG_SELECT_CARD);
		int selected =  (int) (Math.random() * userCards.size());
		String selectedCard = userCards.get(selected);
		try {
			// 1秒間、実行を停止する
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// 本課題では例外時の処理を考慮しない
			e.printStackTrace();
		}
		System.out.println(CardConst.MSG_FINISHED);
		userCards.remove(selected);
		comCards.add(selectedCard);
		
		// 手札を精査する
		comCards = clearCards(comCards);
		Collections.shuffle(comCards);
	}
	
	/**
	 * COMの手札の一覧を作成
	 * @return メッセージ
	 */
	private void printCOMCardsList() {

		System.out.println(CardConst.MSG_SLECT_COM_CARD);
		// 手札の一覧を生成
		StringBuilder comCardsList = new StringBuilder();
		for (int i = 0; i < comCards.size(); i++) {
			comCardsList.append("+----+ ");
		}
		comCardsList.append("\n");

		for (int i = 0; i < comCards.size(); i++) {
			comCardsList.append("|    | ");
		}
		comCardsList.append("\n");

		for (int i = 0; i < comCards.size(); i++) {
			// 数字を中央揃えにするために3桁分のスペースを確保してフォーマット
		    comCardsList.append(String.format("| %2d | ", i + 1));  // 右揃えを使用して中央に表示される
		}
		comCardsList.append("\n");

		for (int i = 0; i < comCards.size(); i++) {
			comCardsList.append("|    | ");
		}
		comCardsList.append("\n");

		for (int i = 0; i < comCards.size(); i++) {
			comCardsList.append("+----+ ");
		}
		comCardsList.append("\n");
		
		// 手札の一覧を表示する
		System.out.println(comCardsList.toString());
	}
}
