package com.run.xml;

import com.run.app.MyApplication;
import com.run.bean.Word;
import com.run.db.DbHelper;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/*<word>uncluttered</word>
 <trans><![CDATA[vt.   整理]]></trans>
 <phonetic><![CDATA[[,ʌn'klʌtə(r)]]]></phonetic>
 <tags></tags>
 <progress>3</progress>*/
public class parseWordXml {
	Word mWord;

	public parseWordXml() {

	}

	public void parse(InputStream is) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance(); // 取得SAXParserFactory实例
		SAXParser parser = factory.newSAXParser(); // 从factory获取SAXParser实例
		MyHandler handler = new MyHandler(); // 实例化自定义Handler
		parser.parse(is, handler); // 根据自定义Handler规则解析输入流

	}

	// 需要重写DefaultHandler的方法
	private class MyHandler extends DefaultHandler {

		private StringBuilder builder;

		@Override
		public void endDocument() throws SAXException {
			// TODO Auto-generated method stub
			super.endDocument();
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			if (localName.equals("item")) {
				mWord = new Word();
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) // 每一个element这个函数可能被调用多次
				throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length); // 将读取的字符数组追加到builder中
			// Log.e("chenrun", "builder is " + builder);

		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("word")) {
				mWord.setWord(builder.toString().trim());
			} else if (localName.equals("trans")) {
				mWord.setTrans(builder.toString().trim());
			} else if (localName.equals("phonetic")) {
				mWord.setPhonetic(builder.toString().trim());
			} else if (localName.equals("progress")) {
                int progress=Integer.parseInt(builder.toString().trim());
                if (progress==Word.NOT_SHOW_PROGRESS){
                    mWord.setProgress(Word.MAX_PROGRESS);    //初始化数据
                }else{
                    mWord.setProgress(Integer.parseInt(builder.toString().trim()));
                }
			}else if (localName.equals("item")) {
				//todo insert db
				new DbHelper(MyApplication.getInstance()).insert(mWord);
			}
			builder.setLength(0); // 将字符长度设置为0 以便重新开始读取元素内的字符节点

		}
	}

}
