package com.abc.wave;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class StartMain {
	
	
	private static void convertAudioFiles(String src, String target)
			throws Exception {
		FileInputStream fis = new FileInputStream(src);
		FileOutputStream fos = new FileOutputStream(target);

		// ���㳤��
		byte[] buf = new byte[1024 * 4];
		int size = fis.read(buf);
		int PCMSize = 0;
		while (size != -1) {
			PCMSize += size;
			size = fis.read(buf);
		}
		fis.close();

		// ��������������ʵȵȡ������õ���16λ������ 8000 hz
		WaveHeader header = new WaveHeader();
		// �����ֶ� = ���ݵĴ�С��PCMSize) + ͷ���ֶεĴ�С(������ǰ��4�ֽڵı�ʶ��RIFF�Լ�fileLength������4�ֽ�)
		header.fileLength = PCMSize + (44 - 8);
		header.FmtHdrLeth = 16;
		header.BitsPerSample = 8;
		header.Channels = 1;
		header.FormatTag = 0x0001;
		header.SamplesPerSec = 20000;
		header.BlockAlign = (short) (header.Channels * header.BitsPerSample / 8);
		header.AvgBytesPerSec = header.BlockAlign * header.SamplesPerSec;
		header.DataHdrLeth = PCMSize;

		byte[] h = header.getHeader();

		assert h.length == 44; // WAV��׼��ͷ��Ӧ����44�ֽ�
		// write header
		fos.write(h, 0, h.length);
		// write data stream
		fis = new FileInputStream(src);
		size = fis.read(buf);
		while (size != -1) {
			fos.write(buf, 0, size);
			size = fis.read(buf);
		}
		fis.close();
		fos.close();
		System.out.println("Convert OK!");
	}

	public static void main(String[] args) {
		try {
			convertAudioFiles("rawwavs/outpcm", "rawwavs/outwav.wav");
			PlaySounds thread = new PlaySounds("/rawwavs/outwav.wav");
			
			new Thread(thread).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}