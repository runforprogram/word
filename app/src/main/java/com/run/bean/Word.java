package com.run.bean;

import java.io.Serializable;


public class Word implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3623659289998244639L;
    public  static  int MAX_PROGRESS=9;
    public  static  int MIN_PROGRESS=0;
    public static  int  NOT_SHOW_PROGRESS=-1;
	private String word;
	private String phonetic;
	private String trans;
    private int  progress;  //单词记住的概率，作为随机再次出现的权重
    private long  lastTime; //最后显示出来的时间
    public long getLastTime() {
        return lastTime;
    }
    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}



	public String getTrans() {
		return trans;
	}

	public void setTrans(String trans) {
		this.trans = trans;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public int  getProgress() {
		return progress;
	}

	public void setProgress(int  progress) {
		this.progress = progress;
	}
	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
	
		    if(!(o instanceof Word)) return false;
		    Word other = (Word) o;
		    return this.word.equals(other.word);
		}
	

}
