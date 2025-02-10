package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.CardConst;

/**
 * カードゲームのUtilsクラス
 * @author K Kawamura
 *
 */
public class CardsUtils {
	/**
	 * トランプをユーザーとCOMに配布する。
	 * @return ユーザーとCOMの手札を入れたMap
	 */
	public static Map<String, List<String>> handOutTrump(List<String> userCards, List<String> comCards) {
		System.out.print(CardConst.MSG_CARD_HAND_OUT);
		// 配列を1次元にフラット化
		List<String> cards = new ArrayList<>();
		for (String[] suit : CardConst.CARD_TRUMP) {
			cards.addAll(List.of(suit));
		}
		// シャッフルする
		Collections.shuffle(cards);
		// ユーザーとCOMにカードを配る
		userCards.addAll(cards.subList(0, CardConst.CARD_NUM_HALF));
		comCards.addAll(cards.subList(CardConst.CARD_NUM_HALF, CardConst.CARD_NUM_MAX));
		
		// ジョーカーを配る
		int randomInt = (int) (Math.random() * 2);
		if (randomInt == 0) {
			userCards.add(CardConst.TRUMP_CALL_JOKER);
			Collections.shuffle(userCards);
		} else {
			comCards.add(CardConst.TRUMP_CALL_JOKER);
			Collections.shuffle(comCards);
		}
		
		// ユーザーとCOMに分けた手札をMapに格納する
		Map<String, List<String>> cardsMap = new HashMap<>();
		cardsMap.put("userCards", userCards);
		cardsMap.put("comCards", comCards);

		// 1秒間処理を止める
		waitProcess(1000);
		System.out.println(CardConst.MSG_FINISHED);

		return cardsMap;
	}

	/**
	 * カードを精査する（同じ数字を削除する）。
	 * @param cards 手札
	 * @return 精査完了した手札
	 */
	public static List<String> clearCards(List<String> cards) {
		// 重複を削除した手札
		List<String> uniqueCards = new ArrayList<>();
		for (String card : cards) {
			
			// カードのマークを削除
			String filteredCard = card.replace(CardConst.TRUMP_CALL_HEART, "")
					.replace(CardConst.TRUMP_CALL_DIAGRAM, "")
					.replace(CardConst.TRUMP_CALL_SPADE, "")
					.replace(CardConst.TRUMP_CALL_CLOVER, "")
					.replace(CardConst.TRUMP_CALL_JOKER, "");

			boolean isFound = false;
			// 重複を削除した手札に同じ数字のカードがないかチェックする
			for (int i = 0; i < uniqueCards.size(); i++) {
				String c = uniqueCards.get(i);
				String filtered = c.replace(CardConst.TRUMP_CALL_HEART, "")
						.replace(CardConst.TRUMP_CALL_DIAGRAM, "")
						.replace(CardConst.TRUMP_CALL_SPADE, "")
						.replace(CardConst.TRUMP_CALL_CLOVER, "")
						.replace(CardConst.TRUMP_CALL_JOKER, "");

				// 同じ数字のカードが見つかった場合
				if (filtered.equals(filteredCard)) {
					// 重複を削除した手札から削除する
					uniqueCards.remove(i);
					isFound = true;
					break;
				}
			}
			
			// 同じ数字のカードが見つ駆らなかった場合、
			// 重複を削除した手札にカードを追加
			if (!isFound) {
				uniqueCards.add(card);
			}
		}
		return uniqueCards;
	}

	/**
	 * 手札を表示する。
	 * @param cards 手札
	 */
	public static void printCards(List<String> cards) {
		System.out.println(CardConst.MSG_USER_CARD);
		for (String card : cards) {
			System.out.print(card);
		}
		System.out.println();
	}

	/**
	 * 指定の秒数処理を停止する。
	 * @param seconds 秒数
	 */
	public static void waitProcess(int seconds) {
		try {
			// 1秒間、実行を停止する
			Thread.sleep(seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * COMの手札の一覧を作成する。
	 * @return メッセージ
	 */
	public static void printCOMCardsList(List<String> comCards) {
		System.out.println(CardConst.MSG_SLECT_COM_CARD);
		int cardCount = comCards.size();
		StringBuilder comCardsList = new StringBuilder();

		// 上部の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "+----+ ");
		appendNewLine(comCardsList);

		// 空白の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "|    | ");
		appendNewLine(comCardsList);

		// 番号を中央に配置
		for (int i = 0; i < cardCount; i++) {
			comCardsList.append(String.format("| %2d | ", i + 1));
		}
		appendNewLine(comCardsList);

		// 空白の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "|    | ");
		appendNewLine(comCardsList);

		// 下部の枠を追加
		appendRepeatedMessage(comCardsList, cardCount, "+----+ ");
		appendNewLine(comCardsList);

		System.out.println(comCardsList.toString());
	}

	/**
	 * StringBuilder に繰り返しメッセージを追加。
	 * @param sb StringBuilder
	 * @param count 繰り返し回数
	 * @param message 追加するメッセージ
	 */
	private static void appendRepeatedMessage(StringBuilder sb, int count, String message) {
		for (int i = 0; i < count; i++) {
			sb.append(message);
		}
	}

	/**
	 * 新しい行を追加。
	 * @param sb StringBuilder
	 */
	private static void appendNewLine(StringBuilder sb) {
		sb.append("\n");
	}
}
