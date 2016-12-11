package com.sin.java.waveaccess.test;

// 关于JavaPlot查看https://github.com/sintrb/JavaPlot
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.sin.java.plot.Plot;
import com.sin.java.plot.PlotFrame;
import com.sin.java.waveaccess.WaveFileReader;

/**
 * 
 * @author RobinTang
 * 
 */
public class TestMain {

	private static FileOutputStream fis;
	private static BufferedOutputStream bis;
	
	// int 数组 转换到 double数组
	// JavaPlot 只支持double数组的绘制
	public static double[] Integers2Doubles(int[] raw) {
		double[] res = new double[raw.length];
		for (int i = 0; i < res.length; i++) {
			res[i] = raw[i];
		}
		return res;
	}

	// 绘制波形文件
	public static void drawWaveFile(String filename) throws IOException {
		WaveFileReader reader = new WaveFileReader(filename);
		//
		String[] pamss = new String[] { "-r", "-g", "-b" };
		if (reader.isSuccess()) {
			PlotFrame frame = Plot.figrue(String.format("%s %dHZ %dBit %dCH", filename, reader.getSampleRate(), reader.getBitPerSample(), reader.getNumChannels()));
			frame.setSize(500, 200);
			Plot.hold_on();
			for (int i = 0; i < reader.getNumChannels(); ++i) {
				// 获取i声道数据
				byte[] data = reader.getByteData()[i];
				// 绘图
				//Plot.plot(Integers2Doubles(data), pamss[i % pamss.length]);
				bis.write(data);
				bis.flush();

			}
			Plot.hold_off();
			bis.close();
			
		} else {
			System.err.println(filename + "不是一个正常的wav文件");
		}
	}

	public static void main(String[] args) throws IOException {
		try {
			fis = new FileOutputStream("rawwavs/outpcm");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bis = new BufferedOutputStream(fis);
		
//		drawWaveFile("rawwavs/wav_40_16_1_pcm.wav");
//		drawWaveFile("rawwavs/wav_40_16_2_pcm.wav");
		
		drawWaveFile("rawwavs/wav_20_8_1_pcm.wav");
//		drawWaveFile("rawwavs/wav_20_8_2_pcm.wav");
	}
}



