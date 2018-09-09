//not implemented yet
//
//package com.csdApp.model;
//
//
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.GC;
//import org.eclipse.swt.widgets.Canvas;
//
//public class Point implements DrawablePoint{
//
//	public GC gc;
//	private int x, y;
//	private Color c;
//	
//	public Point(int x, int y) {
//		this.x = x;
//		this.y = y;
//	}
//	
//	public Point(int x, int y, Color c){
//		this.x = x;
//		this.y = y;
//		this.c = c;
//	}
//
//	@Override
//	public void drawPoint(Canvas canvas) {
//		gc.setBackground(c);
//		gc.fillRectangle(x, y, 1, 2);
//	}
//	
//	public void drawPoint() {
//		gc.setBackground(c);
//		gc.fillRectangle(x, y, 1, 2);
//	}
//
//	@Override
//	public void setAll(GC gc) {
//		this.gc = gc;
//	}
//
//	public int getX() {
//		return x;
//	}
//
//	public void setX(int x) {
//		this.x = x;
//	}
//
//	public int getY() {
//		return y;
//	}
//
//	public void setY(int y) {
//		this.y = y;
//	}
//
//}
