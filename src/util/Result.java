package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	public int checkResult(List<String> userCards, List<String> comCards) {
		if(userCards.size() == 0) {
			return 0; // ユーザーが勝ちと記録
		}

		if(comCards.size() == 0) {
			return 1; // COMが勝ちと記録
		}
		return -1; // まだどちらも手札が残っている
	}
	
	public void printResult(int flag) {
		if(flag == 0) {
			System.out.println(CardConst.MSG_WIN);
		} else {
			System.out.println(CardConst.MSG_LOSE);
		}
		System.out.println(CardConst.MSG_GAME_END);


		int winCount = 0;
		int looseCount = 0;
		String message = getGameResults(CardConst.GAME_RESULT_FILE);
		if(message != null) {
			// X勝Y敗 を X,Yに分断する
            String[] results = message.split("勝|敗");
            winCount = Integer.parseInt(results[0]);
            looseCount = Integer.parseInt(results[1]);
		}

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
		
		//ファイルに書き込む
		setGameResults(CardConst.GAME_RESULT_FILE, message);
	}
	
	/**
	 * 成績ファイルから成績を表す文字列を取得して、返す。
	 * @param fileName 成績ファイル名
	 * @return 成績を表す文字列。ただし、成績が取得できない場合はnull
	 */
	public String getGameResults(String fileName) {

		BufferedReader reader = null;
		String record = null;

		try {
			reader = new BufferedReader(new FileReader(fileName));
			record = reader.readLine();
		} catch (FileNotFoundException e) {
			// 本課題では例外処理を行わないため、ここで例外をキャッチする
			e.printStackTrace();
		} catch (IOException e) {
			// 本課題では例外処理を行わないため、ここで例外をキャッチする
			e.printStackTrace();
		}

		if (record != null) {
			return record;
		} else {
			return null;
		}
	}

	/**
	 * 指定の文字列を、成績ファイルへ書き込む。
	 * @param fileName 成績ファイル名
	 * @param resultsMsg 成績ファイルに書き込む文字列
	 */
	public void setGameResults(String fileName, String resultsMsg) {

		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
			writer.write(resultsMsg);
			writer.close();

		} catch (IOException e) {
			// 本課題では例外処理を行わないため、ここで例外をキャッチする
			e.printStackTrace();
		}
	}
}
