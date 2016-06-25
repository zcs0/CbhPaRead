﻿package anjoyo.zhou.thread;

import anjoyo.zhou.util.BookFinal;


public class BookThread extends Thread{
	public static int state=0;//此变量是控制操作状态的
	
	public static boolean isStop=true;//此变量是控制线程循环状态的  ，当为false时，循环结束，线程退出
	
	public BookThread() {
		// TODO Auto-generated constructor stub
		Thread t=new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		try {
			while(isStop){
				switch (state) {
				case BookFinal.WAIT:
					break;
				case BookFinal.BOOK_IMPORT:
//					book_import();
					state=BookFinal.WAIT;
					break;

				default:
					break;
				}
				Thread.sleep(100);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
//	public void book_import(){
//		Message msg=HomeCbhPaReadActivity.handler.obtainMessage();
//		msg.what=0;
//		msg.sendToTarget();
//	}

}
