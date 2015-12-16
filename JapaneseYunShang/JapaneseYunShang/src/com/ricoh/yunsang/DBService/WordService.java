package com.ricoh.yunsang.DBService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.os.Environment;
import android.util.Log;

import com.ricoh.yunsang.DAO.DicDatabaseHelper;
import com.ricoh.yunsang.DAO.WordDAO;

/**
 * ���û�ͬʱѧϰһ�ſγ̳�ͻ���⣬Ҫ�󴴽�database helperʱ�����û���
 * 
 * @author zhuwei
 *
 */
public class WordService {
	WordDAO wordDao;
	// �û����ݿ����ֵĸ�ʽΪ (dictionaryName)_(UserName)
	private String dicname_username;
	private String dicname;
	private String username;
	public static String WordReserve = null;

	/**
	 * @param context
	 * @param dicname
	 * @param username
	 * @param version
	 */
	public WordService(Context context, String dicname, String username,
			int version) {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/ricoh/";
		// �˴�Ӧ�����û���Ϣ
		String tempStr = dicname + "_" + username;
		dicname_username = tempStr;
		String str = path + dicname_username + ".db";
		DicDatabaseHelper dbhelper = new DicDatabaseHelper(context, str,
				version);
		wordDao = new WordDAO(dbhelper);
	}

	// ����Ŀ¼���ɹ�true,ʧ��false
	public static boolean initialize() {
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			File dir = Environment.getExternalStorageDirectory();
			String path = dir.getPath() + "/";
			dir = new File(path + "ricoh");
			if (dir.exists()) {
				return true;
			}
			dir.mkdir();
			return true;
		} else {
			return false;
		}
	}

	// ��ӿγ�ʱ���ȵ��ô˺����������ݿ�
	public static void addNewCourse(Context context, String dicname,
			String username) {
		InputStream fis = null;
		FileOutputStream fos = null;
		try {
			String path = Environment.getExternalStorageDirectory().getPath()
					+ "/ricoh/";
			fis = context.getAssets().open(dicname);
			byte[] buff = new byte[fis.available()];
			fis.read(buff);
			fis.close();
			fos = new FileOutputStream(path + dicname + "_" + username + ".db");
			fos.write(buff);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ���ô˺�����ÿ�δ����ʱ�������ݿ�recited��ebmsnhaus_sign,�˲������ܺ�ʱ�ϳ�
	public void updateEbmsnhaus_sign(Context context, int version) {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/ricoh/";
		File file = new File(path);
		DicDatabaseHelper dbhelper = null;
		File[] list = file.listFiles();
		for (int i = 0; i < list.length; i++) {
			dbhelper = new DicDatabaseHelper(context, list[i].getAbsolutePath()
					+ "/" + list[i].toString(), version);
			dbhelper.getWritableDatabase()
					.execSQL(
							"update recited set ebmsnhaus_sign = ebmsnhaus_sign + 1 where ebmsnhaus_sign not in (1,2,3,5,7,9,11,13)");
		}
	}

	// ����¿γ̺���ô˺���!!!!!
	public void initializeDictionary() {
		wordDao.copyToUnderlearn();
	}

	/**
	 * @param flag
	 *            ��ѡ��false 7ѡ��true ��һ������ȷѡ������Ǹ�����
	 */
	public Cursor getQuestion(boolean flag) {
		if (flag == true) {
			Random random = new Random();
			boolean temp = random.nextBoolean();
			if (temp == false)
				return getFourQuestion();
			if (temp == true)
				return getSevenQuestion();
		} else {
			return getFourQuestion();
		}
		return null;

	}

	private Cursor getSevenQuestion() {
		Cursor keyCursor = wordDao.getQuestion();
		if (keyCursor == null)
			return null;
		keyCursor.moveToNext();
		WordReserve = keyCursor.getString(1);
		keyCursor.moveToFirst();
		Cursor otherOptionCursor = wordDao.getOtherOption(7);
		Cursor cursor = new MergeCursor(new Cursor[] { keyCursor,
				otherOptionCursor });
		return cursor;
	}

	private Cursor getFourQuestion() {
		Cursor keyCursor = wordDao.getQuestion();
		if (keyCursor == null || keyCursor.getCount() == 0) {
			return null;
		}
		keyCursor.moveToNext();
		WordReserve = keyCursor.getString(1);
		keyCursor.moveToFirst();
		Cursor otherOptionCursor = wordDao.getOtherOption(4);
		Cursor cursor = new MergeCursor(new Cursor[] { keyCursor,
				otherOptionCursor });
		return cursor;
	}

	// ��Ŀ����ύ
	public void submitResult(Boolean result) {
		if (wordDao.flagOfQuestionFrom == false) {
			// ����recited
			wordDao.changeRecitedByResult(result, WordReserve);
		} else if (wordDao.flagOfQuestionFrom == true) {
			// ����underlearn
			wordDao.changeUnderlearnByResult(result, WordReserve);

		}
	}

	// wordnote opt
	public void addWordToWordnote(String word) {
		wordDao.insertIntoWordnote(word);
	}

	public void deleteWordFromWordnote(String word) {
		wordDao.deleteFromWordnote(word);
	}

	public void clearWordnote() {
		wordDao.clearTable("word_note");
	}

	/**
	 * ��ʾ���ʱ����е���
	 */
	public Cursor seeWordnote() {
		return wordDao.seeWordnote();
	}

	/**
	 * ���� ���û������пγ��ֵ�
	 */
	public static List<String> getWordnote(String username) {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/ricoh/";
		File file = new File(path);
		File[] list = file.listFiles();
		ArrayList<String> userDic = new ArrayList<String>();
		for (int i = 0; i < list.length - 1; i++) {
			String temp = list[i].toString();
			if (temp.endsWith(username + ".db")) {
				String[] strs = temp.split("_");
				userDic.add(strs[0]);
			}
		}

		return userDic;
	}

	/**
	 * @return list<String>{dicname,username},list<Integer> ����id
	 */
	public Map<List<String>, List<Integer>> synchroWordnote() {
		Map<List<String>, List<Integer>> syncWordnote = new HashMap<List<String>, List<Integer>>();
		List<Integer> wordList = new ArrayList<Integer>();
		List<String> info = new ArrayList<String>();
		info.add(dicname);
		info.add(username);
		Cursor cursor = wordDao.getWordnoteWordId();
		while (cursor.moveToNext()) {
			wordList.add(cursor.getInt(0));
		}
		syncWordnote.put(info, wordList);
		return syncWordnote;

	}

	// ɾ���ÿγ�
	public boolean deleteCourse() {
		String path = Environment.getExternalStorageDirectory().getPath()
				+ "/ricoh/";
		File file = new File(path + dicname_username + ".db");
		return file.delete();
	}

	public int allWordCount() {
		//int number = wordDao.getWordCount("dictionary");
		int number = wordDao.getAllWordCount();
		Log.v("ZW", String.valueOf(number));
		return number;
	}

	public int redictedWordCount() {
		int number = wordDao.getRecitedWordCount();
		return number;
	}

}
