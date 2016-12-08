package com.abc.wave;

public class StartMain {
	public static MainFrame frame;
	public static SerialReader serial;
	
	

	public static void main(String[] args) {
		try {
			frame = new MainFrame();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
