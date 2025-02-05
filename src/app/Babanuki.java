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
		String startMsg = startMsg("1.0");
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
				String message = getGameResults(CardConst.GAME_RESULT_FILE);
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
		System.out.println(CardConst.GAME_START_MSG);
		System.out.println("これよりトランプゲーム【ババ抜き】を開始します");
		System.out.print("カード配布中...");
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
		System.out.println("完了");
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
		System.out.println("完了");
		// 手札を表示する
		printCards(userCards);

		// COMのカードを精査する
		comCards = clearCards(comCards);

		int flag = -1;
		while(true) {
			// ユーザーのターン
			userTurn();
			// 勝敗をチェック
			flag = checkResult();
			if(flag == 0 || flag == 1) {
				// 勝敗が決定したのでループから抜ける
				break;
			}
			// 手札を表示
			printCards(userCards);

			// COMのターン
			comTurn();
			// 勝敗をチェック
			flag = checkResult();
			if(flag == 0 || flag == 1) {
				// 勝敗が決定したのでループから抜ける
				break;
			}
			// 手札を表示
			printCards(userCards);
		}
		if(flag == 0) {
			System.out.println(CardConst.WIN_MSG);
		} else {
			System.out.println(CardConst.LOSE_MSG);
		}
		System.out.println(CardConst.GAME_END_MSG);

		String message = getGameResults(CardConst.GAME_RESULT_FILE);
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
		setGameResults(CardConst.GAME_RESULT_FILE, message);
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
		System.out.println(CardConst.TURN_MSG_USER);
		System.out.println("COM側のカード番号を選択してください");
		StringBuilder selectCardMsg = new StringBuilder();
		for(int i=0; i<comCards.size(); i++) {
			if(i < 9) {
				selectCardMsg.append("+-----+ ");
			} else {
				selectCardMsg.append("+------+ ");	
			}
		}
		selectCardMsg.append("\n");
		for(int i=0; i<comCards.size(); i++) {
			if(i < 9) {
				selectCardMsg.append("|     | ");
				
			} else {
				selectCardMsg.append("|      | ");
			}
		}
		selectCardMsg.append("\n");
		for(int i=0; i<comCards.size(); i++) {
			if(i < 9) {
				selectCardMsg.append("|  " + (i+1) + "  | "); 
			} else {
				selectCardMsg.append("|  " + (i+1) + "  | "); 
			}
		}
		selectCardMsg.append("\n");
		for(int i=0; i<comCards.size(); i++) {
			if(i < 9) {
				selectCardMsg.append("|     | ");
				
			} else {
				selectCardMsg.append("|      | ");
			}
		}
		selectCardMsg.append("\n");
		for(int i=0; i<comCards.size(); i++) {
			if(i < 9) {
				selectCardMsg.append("+-----+ ");
			} else {
				selectCardMsg.append("+------+ ");	
			}
		}
		selectCardMsg.append("\n");
		System.out.println(selectCardMsg.toString());
		

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
				} else {
					System.out.println("1～" + comCards.size() + "の数字で指定してください");
				}
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
		System.out.println(CardConst.TURN_MSG_COM);
		System.out.println("カード選択中...");
		int selected =  (int) (Math.random() * userCards.size());
		String selectedCard = userCards.get(selected);
		try {
			// 1秒間、実行を停止する
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// 本課題では例外時の処理を考慮しない
			e.printStackTrace();
		}
		System.out.println("完了");
		userCards.remove(selected);
		comCards.add(selectedCard);
		
		// 手札を精査する
		comCards = clearCards(comCards);
		Collections.shuffle(comCards);
	}

	/**
	 * 勝敗を判定
	 * @return 0:ユーザーの勝ち、1:COMの勝ち、−1:まだ勝敗がついていない
	 */
	private int checkResult() {
		if(userCards.size() == 0) {
			return 0; // ユーザーが勝ちと記録
		}

		if(comCards.size() == 0) {
			return 1; // COMが勝ちと記録
		}
		return -1; // まだどちらも手札が残っている
	}

}
