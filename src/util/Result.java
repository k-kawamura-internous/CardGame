package util;

import java.util.List;

import common.CardConst;
/**
 * ゲーム結果に関するクラス。
 * @author K Kawamura
 */
public class Result {
	
	/**
	 * ユーザー、COMどちらかに手札が残っているか確認
	 * @param userCards ユーザーの手札
	 * @param comCards COMの手札
	 * @return 0:ユーザーの手札が無くなった、1:COMの手札が無くなった、−1:どちらの手札も残っている
	 */
	public static int checkResult(List<String> userCards, List<String> comCards) {
		if(userCards.size() == 0) {
			return 0; // ユーザーの手札が無くなった
		}

		if(comCards.size() == 0) {
			return 1; // COMの手札がなくなった
		}
		return -1; // まだどちらも手札が残っている
	}
	
	/**
	 * 勝敗を判定する。
	 * @param result どちらが勝ったのか
	 * @return true:勝利、false:敗北
	 */
	public static boolean checkWin(int result) {
		return result == 0 ? true : false;
	}
	
	/**
	 * 今回の結果を今までの記録に加算し、取得する。
	 * @param isWin 勝利か敗北か
	 */
	public static String getResult(Boolean isWin) {
		// テキストファイルの内容を取得する
		String gameResultMSG = FileUtils.getGameResults(CardConst.GAME_RESULT_FILE);
		
		int winCount = 0;
		int looseCount = 0;
		if(gameResultMSG != null) {
			// X勝Y敗 を X,Yに分断する
            String[] results = gameResultMSG.split("勝|敗");
            winCount = Integer.parseInt(results[0]);
            looseCount = Integer.parseInt(results[1]);
		}
		
		if(isWin) {
			// 勝った場合、winCountを+1し、勝利時のメッセージを表示
			winCount++;
			System.out.println(CardConst.MSG_WIN);
		} else {
			// 負けた場合、looseCountを+1し、敗北時のメッセージを表示
			looseCount++;
			System.out.println(CardConst.MSG_LOSE);
		}
		
		// X勝Y敗に連結する
		StringBuilder resultMsg = new StringBuilder();
		resultMsg.append(winCount);
		resultMsg.append("勝");
		resultMsg.append(looseCount);
		resultMsg.append("敗");
		gameResultMSG = resultMsg.toString();

		return gameResultMSG;
	}
	
}
