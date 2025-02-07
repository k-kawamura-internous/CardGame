package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	/**
	 * 成績ファイルから成績を表す文字列を取得して、返す。
	 * @param fileName 成績ファイル名
	 * @return 成績を表す文字列。ただし、成績が取得できない場合はnull
	 */
	public static String getGameResults(String fileName) {

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
	public static void setGameResults(String fileName, String resultsMsg) {

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
