package com.wing.game.wzq.provider;

import java.util.ArrayList;

public class RecordStack {
	private  ArrayList<Position> record ;
	private static RecordStack instance = new RecordStack();
	private RecordStack(){
		instance = this;
		record = new ArrayList<Position>() ;
	}
	public static RecordStack getInstance(){
		return instance;
	}
	public void pop(){
		record.remove(getCount()-1);//�Ƴ��������һ����
		record.remove(getCount()-1);//�Ƴ�������һ����
	}
	public void put(Position pos){
		record.add(pos);
	}
	public int getCount(){
		return record.size();
	}
	public Position getPosition(int index){
		return record.get(index);
	}
	public void clear(){
		record.clear();
	}
	public boolean isLast(int index){
		return index==(getCount()-1);
	}
}
