package android.ricohkana.fq.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.ricohkana.fq.db.KanaObject;

public class HiraganaXmlParser {
	static String K_DICT = "dict";
	static String K_EXAMPLES = "examples";
	static String K_KANA = "kana";
	static String K_MEANING = "meaning";
	static String K_MEMORYHINT = "memoryHint";
	static String K_ROMAJI = "romaji";
	static final int S_EX = 0;
	static final int S_KANA = 1;
	static final int S_MEANING = 4;
	static final int S_MEM = 2;
	static final int S_ROMAJI = 3;
	static String TAG = "TRANSLATE";
	int step = 0;
	int stepExamples = 0;

	static {
		K_KANA = "kana";
		K_MEANING = "meaning";
		K_EXAMPLES = "examples";
		K_MEMORYHINT = "memoryHint";
		K_ROMAJI = "romaji";
	}

	private void setKanaValue(KanaObject paramKana, Node paramNode) {

		switch (this.step) {
		case 0:
			break;
		case 1:
			paramKana.setKana(paramNode.getTextContent());
			break;
		case 2:
			paramKana.setMemoryHint(paramNode.getTextContent());
			break;
		case 3:
			break;
		}
	}

	private void setStep(Node paramNode) {
		if (paramNode.getTextContent().equals(K_EXAMPLES)) {
			this.step = 0;
			return;
		} else if (paramNode.getTextContent().equals(K_KANA)) {
			this.step = 1;
			return;
		} else if (paramNode.getTextContent().equals(K_MEMORYHINT)) {
			this.step = 2;
			return;
		} else if (paramNode.getTextContent().equals(K_ROMAJI)) {
			this.step = 3;
			return;
		}
	}

	public void parse(String paramString) {
		NodeList localNodeList1 = null;
		Node localNode1;
		NodeList localNodeList2;
		KanaObject localKana;
		int j;
		int i = 0;

		ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
				paramString.getBytes());
		DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory
				.newInstance();

		try {
			localNodeList1 = localDocumentBuilderFactory.newDocumentBuilder()
					.parse(localByteArrayInputStream).getDocumentElement()
					.getChildNodes();
			if (localNodeList1.getLength() <= 0) {
				return;
			}
		} catch (SAXException localSAXException) {
			localSAXException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (ParserConfigurationException localParserConfigurationException) {
			localParserConfigurationException.printStackTrace();
		}

		while (i < localNodeList1.getLength()) {

			localNode1 = localNodeList1.item(i);

			if (localNode1.getNodeName().equals(K_DICT)) {
				localNodeList2 = localNode1.getChildNodes();
				localKana = new KanaObject();

				// ÿѭ��һ������ͬһ�������Ĳ�ͬ���Ե�ֵ��������������ֵ����ͬһ������setKanaValue��
				// ��ô���������ô֪���ô�ѭ�����õ���ʲô�����أ�ͨ��setStep����һ����ǣ���ͬ�ı�Ƕ�Ӧ��ͬ������
				for (j = 0;; j++) {
					if (j >= localNodeList2.getLength()) {
						Constant.memoriesMap.put(localKana.getKana(),
								localKana.getMemoryHint());
						break;
					}

					Node localNode2 = localNodeList2.item(j);

					// ����Ϊʲô��һ������������������[#text:] ���ǲ��˰������ϣ�ԭ����XML
					// �ļ�Ԫ��֮��Ŀհ��ַ�Ҳ��һ��Ԫ��
					if ((localNode2.getNodeName().equals("key"))
							&& (localNode2.getTextContent() != null)) {
						setStep(localNode2);
					} else if (!localNode2.getNodeName().equals("key")
							&& !localNode2.getNodeName().equals("#text")) {
						setKanaValue(localKana, localNode2);
					}
				}
			}
			i++;
		}
		return;
	}
}