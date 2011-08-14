package com.wing.game.wzq.provider;

import java.util.ArrayList;

import com.wing.game.wzq.Application;

public class RecordStack {
	private  ArrayList<Position> record ;
	private static RecordStack instance = new RecordStack();
	private RecordStack(){
		record = new ArrayList<Position>() ;
	}
	public static RecordStack getInstance(){
		return instance;
	}
	public void pop(){
		if(record.size()>0){
			Position temp;
			temp =record.remove(getCount()-1);//抛出最后一步黑棋
			temp.setID(Application.NO);
			QiJu.getInstance().setPosition(temp);
		}
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
