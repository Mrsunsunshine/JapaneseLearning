package android.ricohkana.fq.dailyexpression;
//xml����

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SaxParser {
	
	private InputStream in;
	private MyHandler handler;
	public SaxParser(InputStream in) throws ParserConfigurationException, SAXException, IOException {
		this.in=in;
		init();
	}
	public List<XmlBean> parse() {
		 //�����Զ���Handler�������������
		return handler.getWords();
	}
	
	public void init() throws ParserConfigurationException, SAXException, IOException
	{
		SAXParserFactory factory = SAXParserFactory.newInstance();	//ȡ��SAXParserFactoryʵ��
		SAXParser parser = factory.newSAXParser();					//��factory��ȡSAXParserʵ��
		handler = new MyHandler();						//ʵ�����Զ���Handler
		parser.parse(in, handler);	
		in.close();
	}
	
	//��Ҫ��дDefaultHandler�ķ���
	private class MyHandler extends DefaultHandler {

		private List<XmlBean> words;
		private XmlBean word;
		private StringBuilder builder;
		
		//���ؽ�����õ���Book���󼯺�
		public List<XmlBean> getWords() {
			return words;
		}
		
		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			words = new ArrayList<XmlBean>();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			super.startElement(uri, localName, qName, attributes);
			
			if (qName.equals("content")) {
				word = new XmlBean();			
			}
			
			builder.setLength(0);	//���ַ���������Ϊ0 �Ա����¿�ʼ��ȡԪ���ڵ��ַ��ڵ�
		}
		
		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			super.characters(ch, start, length);
			builder.append(ch, start, length);	//����ȡ���ַ�����׷�ӵ�builder��
		}
		
		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (qName.equals("romanization")) {
				word.setRomanization(builder.toString());
			} else if (qName.equals("japanese")) {
				word.setJapanese(builder.toString());
			} else if (qName.equals("chinese")) {
				word.setChinese(builder.toString()); 
			} else if (qName.equals("soundAdress")) 
			{
				word.setSoundAdress(builder.toString());
			}
			else if(qName.equals("type")) {
				
				word.setType(Integer.parseInt(builder.toString()));
			}
			else if (qName.equals("content"))
			{
				words.add(word);
			}
		}
	}
}