package android.ricohkana.fq.utils;

import java.util.Map;

import com.ricoh.R;


public class Constant
{
  public static int firstBtnTextId;
  public static int paintStrokeWidth;
  public static int position;
  //�У�������ʮ��ͼ��һ��
  public static int row;	
  public static int secondBtnTextId;
  //�Σ�������ʮ��ͼ��һ��
  public static int section;
  public static int titleBtnTextId;
  //0ƽ������1Ƭ����
  public static int type;	
  public static int valume;
  public static float valumes = 0.6F;
  //0������1������2����
  public static int yin;
  
  public static Map<String, String> memoriesMap;

  static
  {
    valume = 6;
    position = -1;
    row = 0;
    section = 0;
    type = 0;
    titleBtnTextId = R.string.qing_yin;
    firstBtnTextId = R.string.zhuo_yin;
    secondBtnTextId = R.string.ao_yin;
    paintStrokeWidth = 25;
    yin = 0;
  }
}