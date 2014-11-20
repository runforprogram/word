package com.run.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.run.app.Datas;
import com.run.app.MyApplication;
import com.run.bean.Word;
import com.run.until.MyLogger;

import java.util.List;
import java.util.Random;

/**
 * @author Administrator
 * 
 */
public class DbHelper extends SQLiteOpenHelper {

	private String word = "word";
	private String phonetic = "phonetic";
	private String trans = "trans";
	private String progress = "progress"; // 值为-1到10
											// 记为显示的次数，作为随机显示时候的一个参考权重。-1表示记住了
											// 。。
    private  String lastTime="lasttime";   //最后没有被记住的时间初始化的值为null

	private final static int DATABASE_VERSION = 1;
	private final static String TABLE_ALL_WORD = "wordinfo";

	public final static String TABLE_LASTEST_WORD = "wordlastest";

	public final static String FIELD_ID = "word";
	public final static String FIELD_TITLE = "sec_Title";

	public DbHelper(Context context) {
		super(context, Datas.DATABASE_NAME, null, DATABASE_VERSION);

	}

	/**
	 * @param context
	 * @param databaseVersion
	 *            传入databaseVersion 如果比之前的大的话 会调用onUpgrade.
	 */
	public DbHelper(Context context, int databaseVersion) {

		super(context, Datas.DATABASE_NAME, null, databaseVersion);
	}

	// 第一次创建的时候调用，在MyApplication中通过xml插入数据库的时候。
	@Override
	public void onCreate(SQLiteDatabase db) {
		MyLogger.i("oncreate in SQLiteOpenHelper");
		String videoSql = "Create table " + TABLE_ALL_WORD + "(" + this.word
				+ " text primary key," + this.phonetic + " text ," + this.trans
				+ " text ," + this.progress + " text ," + this.lastTime+" integer "+");";
		Log.i("tag", "sql=" + videoSql);
		db.execSQL(videoSql);
	//	createTable(db, TABLE_LASTEST_WORD);
	}

	/**
	 * @param db
	 * @param tableName
	 *            创建表
	 */
	public void createTable(SQLiteDatabase db, String tableName) {

		String sql = " DROP TABLE IF EXISTS " + "'" + tableName + "'"; // 如果已经有这个表了
																		// 先删除
		db.execSQL(sql);
		String videoSql = "Create table if not exists " + "'" + tableName + "'"
				+ "(" + this.word + " text primary key," + this.phonetic
				+ " text ," + this.trans + " text ," + this.progress + " text "
				+ ");";
		MyLogger.i("", "sql=" + videoSql);
		db.execSQL(videoSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		// String sql=" DROP TABLE IF EXISTS "+TABLE_NAME; //如果已经有这个表了 先删除
		// db.execSQL(sql);
		// onCreate(db);
		MyLogger.i("on Upgreade ");
	
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		MyLogger.i("on onDowngrade ");
		// super.onDowngrade(db, oldVersion, newVersion);
	}

	/**
	 * 插入上次显示的word到表中
	 */

	public long insert(Word word,String tableName) {
        if (ifInDb(word.getWord(),tableName)) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(this.word, word.getWord());
		cv.put(this.trans, word.getTrans());
		cv.put(this.phonetic, word.getPhonetic());
		cv.put(this.progress, word.getProgress());
       // cv.put(this.lastTime,System.currentTimeMillis());
		long row = db.insert(tableName, null, cv);
		db.close();
		return row;

	}

	public Cursor select() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(TABLE_ALL_WORD, null, null, null, null, null,
				" _id desc");
		return cursor;
	}


	/* 主键id是否存在数据库中 */
	public boolean ifInDb(String word) {

		return ifInDb(word, TABLE_ALL_WORD);
	}

	public boolean ifInDb(String word, String tableName) {

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + "'" + tableName + "'"
				+ " WHERE word LIKE " + "'" + word + "'", null);
		boolean value = cursor.getCount() == 0 ? false : true;
		cursor.close();
		db.close();
		return value;
	}

	public long insert(Word word) {
		if (ifInDb(word.getWord())) {
			return -1;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(this.word, word.getWord());
		cv.put(this.trans, word.getTrans());
		cv.put(this.phonetic, word.getPhonetic());
		cv.put(this.progress, word.getProgress());
		long row = db.insert(TABLE_ALL_WORD, null, cv);
		return row;
	}

	public void delete(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_ID + "=?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(TABLE_ALL_WORD, where, whereValue);
	}
	
	public void delete(Word word){
		SQLiteDatabase db = this.getWritableDatabase();
		String where = this.word + "=?";
		String[] whereValue = { word.getWord() };
		db.delete(TABLE_LASTEST_WORD, where, whereValue);
	}

	/**
	 * @param word
	 *            修改这个word的progress的值
	 */
	public void update(Word word) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = this.word + "=?";
		// where代表列的名字，and 
		// wherevalue代表列的值，结合起来找出哪一行，
		// wherevalue为一个数组 不定长参数。  相当于sql语句，word=word.getWord()的值。update TABLE_ALL_WORD set progress=this.getProgress where word=this.getWord();
		String[] whereValue = { word.getWord() };
		ContentValues cv = new ContentValues();
		cv.put(this.progress, word.getProgress());
		db.update(TABLE_ALL_WORD, cv, where, whereValue);

	}
    /*progress<3的单词的出现的概率是20%
    * 3<=progress<7的单词出现的概率是30%
    * 7<=progress的单词出现的概率是50%*/
    private void xxx(){
        int progressInt;
        Random random=new Random(10);
        int randomInt=random.nextInt();
        if (randomInt<2){
         //   progressInt//这里progress小于3
        }else if (randomInt<5){
            //这里的progress在3和7之间

        }else if(randomInt<10){
            //这里的progress>7
        }
    }
    /*其实就和选择2个值为3以下的，3个3到7的。5个progress>7的效果差不多吧   但是不会每次都从db中拿出10个单词*/
    /*每一个progress的值为其权重
    * return 获得应该选择progress为那个值得那些单词*/
    private int  getProgressValue(){
        int sum=0;
        for (int i=0;i<10;i++){
            sum+=i;
        }
        Random random=new Random();
        int randomInt=random.nextInt(sum);
        Log.i("","progeress random int ="+randomInt);
        int sumWeight=0;
        for (int progress=0;progress<10;progress++){
            sumWeight+=progress;
            if (randomInt<=sumWeight){
                return progress;
            }
        }
      return -1;
    }
    private Word getAWordFromCursor(Cursor cursor){
        Word word = new Word();
        word.setWord(cursor.getString(cursor.getColumnIndex(this.word)));
        word.setPhonetic(cursor.getString(cursor
                .getColumnIndex(this.phonetic)));
        word.setTrans(cursor.getString(cursor.getColumnIndex(this.trans)));
        word.setProgress(cursor.getInt(cursor
                .getColumnIndex(this.progress)));
        word.setLastTime(cursor.getLong(cursor.getColumnIndex(this.lastTime)));
        return word;
    }
    /*之前48小时天的的word，相当于复习*/
    public   void getBeforeWord(List<Word> words){
        long purposeTime=System.currentTimeMillis()-48*60*60*1000;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("select*from "+this.TABLE_ALL_WORD+" where "+this.lastTime+">"+"'"+ purposeTime+"'",null);
        while (cursor.moveToNext()){
            words.add(getAWordFromCursor(cursor));
        }
        cursor.close();
    }
    /*从总的数据库中拿出一个单词,新的学习，时间 这样的话会出现words会有word重复的情况*/
    public Word getAword(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=null;
        int times=50;
        do{
        int progress=getProgressValue();
        Log.i("","progeress="+progress);
           //加一个判断条件，一次都没有拿出来过
        cursor = db.rawQuery("SELECT * FROM " + "'" + this.TABLE_ALL_WORD + "' "
                +" where "+this.progress+"="+"'"+progress+"'"+"and "+this.lastTime+" is null"+" ORDER BY RANDOM()  limit "+"'"+1+"'", null);
        Log.i("","cursor size="+cursor.getCount());
        times--;
        }while (cursor.getCount()==0||times==0);
        if (times==0){
            Toast.makeText(MyApplication.getInstance(),"没有新的单词了",Toast.LENGTH_SHORT).show();
            return  null;
        }
        //写入再一次拿出来的时间
        cursor.moveToFirst();

        Word word = new Word();
        word.setWord(cursor.getString(cursor.getColumnIndex(this.word)));
        word.setPhonetic(cursor.getString(cursor
                .getColumnIndex(this.phonetic)));
        word.setTrans(cursor.getString(cursor.getColumnIndex(this.trans)));
        word.setProgress(cursor.getInt(cursor
                .getColumnIndex(this.progress)));
        word.setLastTime(System.currentTimeMillis());
        update(word);//写入取出来的时间
        return word;
    }
    public void getWord(List<Word> words,int num){
        for (int i = 0; i <num ; i++) {
            if (getAword()==null){
                return;
            }else {

                words.add(getAword());
            }
        }
    }
	public void getWord(List<Word> words, String tableName,int num) {
		
		SQLiteDatabase db = this.getReadableDatabase();

/*		Cursor cursor = db.rawQuery("SELECT * FROM " + "'" + tableName + "'"
				+ " ORDER BY progress desc limit "+"'"+selectNum+"'", null);
        Cursor cursor = db.rawQuery("SELECT * FROM " + "'" + tableName + "'"
                + " ORDER BY RANDOM()  limit "+"'"+num+"'", null);*/
  Cursor cursor = db.rawQuery("SELECT * FROM " + "'" + tableName + "'"
                + " ORDER BY RANDOM()  limit "+"'"+num+"'", null);
		for (int j = 0; j <cursor.getCount(); j++) {
            cursor.moveToNext();//todo  为什么会第一次就会调用moveToNext();
            Word word = new Word();
            word.setWord(cursor.getString(cursor.getColumnIndex(this.word)));
			word.setPhonetic(cursor.getString(cursor
					.getColumnIndex(this.phonetic)));
			word.setTrans(cursor.getString(cursor.getColumnIndex(this.trans)));
			word.setProgress(cursor.getInt(cursor
					.getColumnIndex(this.progress)));
			Log.i("", "word=" + word.getWord());
			words.add(word);
		}
		db.close();
	}

/*	public Word getAWord() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ALL_WORD
				+ " ORDER BY progress desc", null);
		int wordNumber = cursor.getCount();
		Random mRandom = new Random();
		int i = mRandom.nextInt(wordNumber);
		Word mWord = new Word();
		cursor.moveToPosition(i);
		mWord.setWord(cursor.getString(cursor.getColumnIndex(this.word)));
		mWord.setPhonetic(cursor.getString(cursor.getColumnIndex(this.phonetic)));
		mWord.setTrans(cursor.getString(cursor.getColumnIndex(this.trans)));
		mWord.setProgress(cursor.getString(cursor.getColumnIndex(this.progress)));
		db.close();
		return mWord;
	}*/
}
