//not implemented yet
//
//package com.csdapp.model;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.graphics.Color;
//import org.eclipse.swt.graphics.GC;
//import org.eclipse.swt.graphics.Image;
//import org.eclipse.swt.graphics.ImageData;
//import org.eclipse.swt.graphics.ImageLoader;
//import org.eclipse.swt.widgets.Canvas;
//import org.eclipse.swt.widgets.Display;
//
//public class ImageDrawer {
//	private Mother[][] net;
//	private List<Point> pointList = new ArrayList<>();
//	private int netSideSize;
//	private int imageSize;
//	private int pointNum;
//	
//	public ImageDrawer(Mother[][] net, int netSideSize) {
//		this.net = net;
//		this.netSideSize = netSideSize;
//		if (netSideSize > 150) {
//			this.pointNum = 150;
//		} else {
//			this.pointNum = netSideSize;
//		}
//	}
//	
//	public void pointCollector(){
//		pointList.clear();
//		Point tmpPoint1;
//		Point tmpPoint2;
//		Mother tmpMother;
//		for(int i = 0; i < pointNum; i++){
//			for(int j = 0; j < pointNum; j++){
//				if(net[i][j] != null){
//					tmpMother = (net[i][j]);
//					if(i == 0 && j == 0){
//						tmpPoint1 = new Point(tmpMother.getX(), tmpMother.getY(), new Color(null, tmpMother.getAllelOne().getColor().getRed(), tmpMother.getAllelOne().getColor().getGreen(), tmpMother.getAllelOne().getColor().getBlue()));
//						tmpPoint2 = new Point(tmpMother.getX() + 1, tmpMother.getY(), new Color(null, tmpMother.getAllelTwo().getColor().getRed(), tmpMother.getAllelTwo().getColor().getGreen(), tmpMother.getAllelTwo().getColor().getBlue()));
//					} else {
//						tmpPoint1 = new Point(tmpMother.getX() + tmpMother.getX(), tmpMother.getY() + tmpMother.getY(), new Color(null, tmpMother.getAllelOne().getColor().getRed(), tmpMother.getAllelOne().getColor().getGreen(), tmpMother.getAllelOne().getColor().getBlue()));
//						tmpPoint2 = new Point(tmpMother.getX() + tmpMother.getX() + 1 , tmpMother.getY() + tmpMother.getY(), new Color(null, tmpMother.getAllelTwo().getColor().getRed(), tmpMother.getAllelTwo().getColor().getGreen(), tmpMother.getAllelTwo().getColor().getBlue()));						
//					}
//					pointList.add(tmpPoint1);
//					pointList.add(tmpPoint2);
//				}
//			}
//		}
//	}
//	
////	private void printPoint(GC gc, Canvas canvas) {
////		pointCollector();
////		Iterator<Point> iterator = pointList.iterator();
////		int flagCount = 0;
////		if(netSideSize > 300){
////			while(iterator.hasNext() && flagCount < 90000){
////				Point temp = iterator.next();
////				temp.setAll(gc);
////				temp.drawPoint(canvas);
////				flagCount++;
////			}
////		} else {
////			while(iterator.hasNext()){
////				Point temp = iterator.next();
////				temp.setAll(gc);
////				temp.drawPoint(canvas);
////			}
////		}
////	}
//	
//	public Image printImage(Display display) {
//		pointCollector();
//		Image image = new Image(display, 300, 300);
//		GC gc = new GC(image);
//		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
//		gc.fillRectangle(0, 0, 300, 300);
//		Iterator<Point> iterator = pointList.iterator();
//		while(iterator.hasNext()) {
//			Point temp = iterator.next();
//			temp.setAll(gc);
//			temp.drawPoint();
//		}
////		ImageLoader imageLoader = new ImageLoader();
////		imageLoader.data = new ImageData[] {image.getImageData()};
////		imageLoader.save("C:/temp/Idea_PureWhite.bmp",SWT.IMAGE_BMP); 
//		return image;
//	}
//
//}
