package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Result {
	
	/**
	 * 勝敗を判定
	 * @param userCards TODO
	 * @param comCards TODO
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
	
	/**
	 * 成績ファイルから成績を表す文字列を取得して、返します。
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
	 * 指定の文字列を、成績ファイルへ書き込みます。
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
