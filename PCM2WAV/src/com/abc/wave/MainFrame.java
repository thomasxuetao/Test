package com.abc.wave;

import java.awt.AWTEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	byte[] message;
	int iWavIndex = 0;
	
	private JLabel jComLabel;
	private JTextField jComText;
	private JButton jOpenButton;
	private JButton jCloseButton;
	private JButton jSingButton;
	private JTextArea jMsgArea;
	private JPanel jComPanel;

	public MainFrame() {
		message = new byte[1024*500];
		init();

		this.setSize(500, 300);
		this.setVisible(true);
		this.setLocationRelativeTo(null);

		enableEvents(AWTEvent.WINDOW_EVENT_MASK);
	}

	public void init() {
		jComLabel = new JLabel("����");
		jComText = new JTextField("COM1");
		jOpenButton = new JButton("�򿪴���");
		jCloseButton = new JButton("�رմ���");
		jSingButton = new JButton("����WAV����");
		jMsgArea = new JTextArea();

		jComPanel = new JPanel();
		jComPanel.add(jComLabel);
		jComPanel.add(jComText);
		jComPanel.add(jOpenButton);
		jComPanel.add(jCloseButton);
		jComPanel.add(jSingButton);
//		jComPanel.add(jMsgArea);

		this.add(jComPanel);

		jOpenButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				initSerial(jComText.getText());

			}
		});

		jCloseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (StartMain.serial != null) {
					StartMain.serial.close();
					StartMain.serial = null;
				}

			}
		});

		jSingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(iWavIndex != 0){
						convertAudioFiles("outwav.wav");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				PlaySounds thread = new PlaySounds("/outwav.wav");
				new Thread(thread).start();
				iWavIndex = 0;
			}
		});

	}

	public void initSerial(String str) {
		if (StartMain.serial == null) {
			StartMain.serial = new SerialReader();
		}
		if (StartMain.serial.isOpen() == false) {
			StartMain.serial.openSerialPort(str);
		}
	}

	public void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			System.exit(0);
		} else {
			super.processWindowEvent(e);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		byte[] msg = (byte[])arg;
		System.arraycopy(msg, 0, message, iWavIndex, msg.length);	
		iWavIndex += msg.length;


	}

	private void convertAudioFiles(String target)
			throws Exception {
		FileOutputStream fos = new FileOutputStream(target);

		// ���㳤��
		int PCMSize = iWavIndex;

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
		fos.write(message, 0, PCMSize);
		fos.close();
	}
}