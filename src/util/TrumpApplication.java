package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import app.Babanuki;
import common.CardConst;

/**
 * トランプゲーム用の起動クラスおよび、ユーティリティクラス。<br>
 * このクラスを実行することでカードゲームを開始する。<br>
 * また、成績ファイルへの入出力は本クラスのメソッドを使用。
 * @author  K　Kawamura
 */
public class TrumpApplication {
	/**
	 * 起動用メイン
	 *
	 * @param String[] args
	 * @return なし
	 */
	public static void main(String[] args) {
		// 1. Babanukiクラスをインスタンス化する。
		Babanuki b = new Babanuki();
		// 2. Babanukiクラスのexecuteメソッドを呼び出す。
		b.execute();

	}

	/**
	 * アプリケーションの起動メッセージを表す文字列を返します。
	 * @param version バージョン
	 * @return 起動メッセージ
	 */
	protected String startMsg(String version) {
		StringBuilder startMsg = new StringBuilder();
		startMsg.append(CardConst.APP_START_MSG);
		startMsg.append("\n");
		startMsg.append(CardConst.APP_START_MSG_1);
		startMsg.append("\n");
		startMsg.append(CardConst.APP_START_MSG_2);
		startMsg.append("\n");
		startMsg.append(CardConst.APP_START_MSG_3);
		startMsg.append("\n");
		return startMsg.toString();
	}

	/**
	 * 成績ファイルから成績を表す文字列を取得して、返します。
	 * @param fileName 成績ファイル名
	 * @return 成績を表す文字列。ただし、成績が取得できない場合はnull
	 */
	protected String getGameResults(String fileName) {

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
	protected void setGameResults(String fileName, String resultsMsg) {

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