package com.fc.coveringArray;

public interface AnnelInf {
	public void initAnneling(); //��ʼ���˻�
	public void startAnneling();//��ʼ�˻�
	public void makeChange();  //��һ��С�ı䶯
	public boolean isEnd();    //�˻����Ƿ����
	public boolean isAccept();  //����Ķ��Ƿ񱻽���
	public boolean isOk();     //�����������Ƿ�õ�һ���⼯
}
