package util;

import java.util.List;

import common.CardConst;
/**
 * ゲーム結果に関するクラス。
 * @author K Kawamura
 */
public class Result {
	
	/**
	 * 勝敗を判定する。
	 * @param userCards ユーザーの手札
	 * @param comCards COMの手札
	 * @return 0:ユーザーの勝ち、1:COMの勝ち、−1:まだ勝敗がついていない
	 */
	public static int checkResult(List<String> userCards, List<String> comCards) {
		if(userCards.size() == 0) {
			return 0; // ユーザーが勝ちと記録
		}

		if(comCards.size() == 0) {
			return 1; // COMが勝ちと記録
		}
		return -1; // まだどちらも手札が残っている
	}
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	public static boolean checkWin(int result) {
		return result == 0 ? true : false;
	}
	
	/**
	 * 
	 * @param isWin
	 */
	public static String getResult(Boolean isWin) {
		if(isWin) {
			System.out.println(CardConst.MSG_WIN);
		} else {
			System.out.println(CardConst.MSG_LOSE);
		}
		System.out.println(CardConst.MSG_GAME_END);

		int winCount = 0;
		int looseCount = 0;
		String gameResultMSG = FileUtils.getGameResults(CardConst.GAME_RESULT_FILE);
		if(gameResultMSG != null) {
			// X勝Y敗 を X,Yに分断する
            String[] results = gameResultMSG.split("勝|敗");
            winCount = Integer.parseInt(results[0]);
            looseCount = Integer.parseInt(results[1]);
		}

		if(isWin) {
			winCount++; // 勝った場合
		} else {
			looseCount++; // 負けた場合
		}
		
		StringBuilder resultMsg = new StringBuilder();
		resultMsg.append(winCount);
		resultMsg.append("勝");
		resultMsg.append(looseCount);
		resultMsg.append("敗");
		gameResultMSG = resultMsg.toString();
		
		return gameResultMSG;
	}
	
}
