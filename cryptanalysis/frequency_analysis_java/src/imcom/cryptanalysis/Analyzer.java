package imcom.cryptanalysis;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Analyzer {
	private JFrame main_frame = new JFrame("Substitution Analyzer");
	private JMenuBar menu_bar = new JMenuBar();
	private JMenuItem open_file = new JMenuItem("Open");
	private JMenuItem quit = new JMenuItem("Quit");
	private JFileChooser file_chooser = new JFileChooser();
	private JPanel mainPanel = new JPanel(new GridLayout(5, 1));
	private JPanel controlPanel = new JPanel(new GridLayout(2, 26));
	private JPanel cipher_statPanel = new JPanel(new GridLayout(3, 1));
	private JPanel english_statPanel = new JPanel(new GridLayout(3, 1));
	private JTextArea cipherArea = new JTextArea("copy ciphertext here...");
	private JTextArea plainArea = new JTextArea("plaintext will be shown here...");
	
	private JTextArea cipher_single = new JTextArea("cipher stat here...");
	private JTextArea cipher_digram = new JTextArea("cipher stat here...");
	private JTextArea cipher_trigram = new JTextArea("cipher stat here...");
	
	private JTextArea english_single = new JTextArea("english stat here...");
	private JTextArea english_digram = new JTextArea("english stat here...");
	private JTextArea english_trigram = new JTextArea("english stat here...");
	
	private HashMap<String, String> subs_records = new HashMap<String, String>();
	
	private JTextField text_A = new JTextField(1);
	private JTextField text_B = new JTextField(1);
	private JTextField text_C = new JTextField(1);
	private JTextField text_D = new JTextField(1);
	private JTextField text_E = new JTextField(1);
	private JTextField text_F = new JTextField(1);
	private JTextField text_G = new JTextField(1);
	private JTextField text_H = new JTextField(1);
	private JTextField text_I = new JTextField(1);
	private JTextField text_J = new JTextField(1);
	private JTextField text_K = new JTextField(1);
	private JTextField text_L = new JTextField(1);
	private JTextField text_M = new JTextField(1);
	private JTextField text_N = new JTextField(1);
	private JTextField text_O = new JTextField(1);
	private JTextField text_P = new JTextField(1);
	private JTextField text_Q = new JTextField(1);
	private JTextField text_R = new JTextField(1);
	private JTextField text_S = new JTextField(1);
	private JTextField text_T = new JTextField(1);
	private JTextField text_U = new JTextField(1);
	private JTextField text_V = new JTextField(1);
	private JTextField text_W = new JTextField(1);
	private JTextField text_X = new JTextField(1);
	private JTextField text_Y = new JTextField(1);
	private JTextField text_Z = new JTextField(1);
	
	private JLabel label_A = new JLabel("A");
	private JLabel label_B = new JLabel("B");
	private JLabel label_C = new JLabel("C");
	private JLabel label_D = new JLabel("D");
	private JLabel label_E = new JLabel("E");
	private JLabel label_F = new JLabel("F");
	private JLabel label_G = new JLabel("G");
	private JLabel label_H = new JLabel("H");
	private JLabel label_I = new JLabel("I");
	private JLabel label_J = new JLabel("J");
	private JLabel label_K = new JLabel("K");
	private JLabel label_L = new JLabel("L");
	private JLabel label_M = new JLabel("M");
	private JLabel label_N = new JLabel("N");
	private JLabel label_O = new JLabel("O");
	private JLabel label_P = new JLabel("P");
	private JLabel label_Q = new JLabel("Q");
	private JLabel label_R = new JLabel("R");
	private JLabel label_S = new JLabel("S");
	private JLabel label_T = new JLabel("T");
	private JLabel label_U = new JLabel("U");
	private JLabel label_V = new JLabel("V");
	private JLabel label_W = new JLabel("W");
	private JLabel label_X = new JLabel("X");
	private JLabel label_Y = new JLabel("Y");
	private JLabel label_Z = new JLabel("Z");
	
	
	private File src_file;
	private String target_string = "";
	private ArrayList<List<Map.Entry<String, Float>>> english_frequency_list = new ArrayList<List<Map.Entry<String, Float>>>(3);
	private ArrayList<List<Map.Entry<String, Float>>> cipher_frequency_list = new ArrayList<List<Map.Entry<String, Float>>>(3);
	
	private class WatchDog implements Runnable {

		public WatchDog(){
			
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String input_buf = "";
			String result_buf = "";
			while (cipherArea.getText().length() == 0);
			input_buf = cipherArea.getText();
			for (char ch : input_buf.toCharArray()) {
				if (Character.isLetter((char) ch)) {
					result_buf += Character.toUpperCase(ch);
				}
			}			
			plainArea.setText(result_buf);	
			startAnalysis(result_buf);
		}
	}
	
	private void startAnalysis(String target) {
		ExecutorService workers = Executors.newFixedThreadPool(3);
		for (int i = 1; i <= 3; ++i) {
			workers.submit(createCounter(i, target, cipher_frequency_list));
		}
		workers.shutdown();
		try {
			workers.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setFrequencies(cipher_single, cipher_digram, cipher_trigram, cipher_frequency_list);
	}
	
	private void setFrequencies(JTextArea single, JTextArea digram, JTextArea trigram, ArrayList<List<Map.Entry<String, Float>>> frequency_list) {
		for (int i = 0; i < frequency_list.size(); ++i) {
			List<Map.Entry<String, Float>> list = frequency_list.get(i);
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<String, Float> entry : list) {
				sb.append(entry.getKey() + ":" + entry.getValue());
				sb.append("; ");
				//System.out.println(entry.getKey() + ":" + entry.getValue());
			}
			switch (i) {
				case 0:
					single.setText(sb.toString());
					break;
				case 1:
					digram.setText(sb.toString());
					break;
				case 2:
					trigram.setText(sb.toString());
					break;
				default:
					break;
			}
		}
	}
	
	private Runnable createCounter(final int step, final String target, final ArrayList<List<Map.Entry<String, Float>>> frequency_list) {
		return new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				HashMap<String, Float> frequency_dict = new HashMap<String, Float>();
				int length = target.length();
				for (int index = 0; index + step <= length; ++index) {
					String key = target.substring(index, index + step);					
					if (!frequency_dict.containsKey(key)) {
						frequency_dict.put(key, 1F);
					} else {
						frequency_dict.put(key, frequency_dict.get(key) + 1F);
					}
				}
				
				for (String key : frequency_dict.keySet()) {
					frequency_dict.put(key, frequency_dict.get(key) * 1F / (length - step + 1));
				}
				
				List<Map.Entry<String, Float>> sorted_list = new Vector<Map.Entry<String, Float>>(frequency_dict.entrySet());
				java.util.Collections.sort(sorted_list, new Comparator<Map.Entry<String, Float>>() {
							@Override
							public int compare(Entry<String, Float> arg0, Entry<String, Float> arg1) {
								// TODO Auto-generated method stub
								return (arg0.getValue().equals(arg1.getValue()) ? 0 :
										(arg0.getValue() > arg1.getValue() ? -1 : 1));
							}
						});
				
				frequency_dict.clear();
				
				frequency_list.add(step-1, sorted_list);
				
			}
			
		};
	}
	
	public Analyzer() {
		
		Container contentPane = main_frame.getContentPane();
		cipherArea.setLineWrap(true);
		plainArea.setLineWrap(true);
		cipher_statPanel.add(cipher_single);
		cipher_statPanel.add(cipher_digram);
		cipher_statPanel.add(cipher_trigram);
		english_statPanel.add(english_single);
		english_statPanel.add(english_digram);
		english_statPanel.add(english_trigram);
		
		mainPanel.add(cipherArea);
		mainPanel.add(plainArea);
		mainPanel.add(cipher_statPanel);
		mainPanel.add(english_statPanel);
		controlPanel.add(label_A);
		controlPanel.add(text_A);
		controlPanel.add(label_B);
		controlPanel.add(text_B);
		controlPanel.add(label_C);
		controlPanel.add(text_C);
		controlPanel.add(label_D);
		controlPanel.add(text_D);
		controlPanel.add(label_E);
		controlPanel.add(text_E);
		controlPanel.add(label_F);
		controlPanel.add(text_F);
		controlPanel.add(label_G);
		controlPanel.add(text_G);
		controlPanel.add(label_H);
		controlPanel.add(text_H);
		controlPanel.add(label_I);
		controlPanel.add(text_I);
		controlPanel.add(label_J);
		controlPanel.add(text_J);
		controlPanel.add(label_K);
		controlPanel.add(text_K);
		controlPanel.add(label_L);
		controlPanel.add(text_L);
		controlPanel.add(label_M);
		controlPanel.add(text_M);
		controlPanel.add(label_N);
		controlPanel.add(text_N);
		controlPanel.add(label_O);
		controlPanel.add(text_O);
		controlPanel.add(label_P);
		controlPanel.add(text_P);
		controlPanel.add(label_Q);
		controlPanel.add(text_Q);
		controlPanel.add(label_R);
		controlPanel.add(text_R);
		controlPanel.add(label_S);
		controlPanel.add(text_S);
		controlPanel.add(label_T);
		controlPanel.add(text_T);
		controlPanel.add(label_U);
		controlPanel.add(text_U);
		controlPanel.add(label_V);
		controlPanel.add(text_V);
		controlPanel.add(label_W);
		controlPanel.add(text_W);
		controlPanel.add(label_X);
		controlPanel.add(text_X);
		controlPanel.add(label_Y);
		controlPanel.add(text_Y);
		controlPanel.add(label_Z);
		controlPanel.add(text_Z);
		
		mainPanel.add(controlPanel);
		contentPane.add(mainPanel);
		main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		menu_bar.add(open_file);
		menu_bar.add(quit);
		main_frame.setJMenuBar(menu_bar);

		InputListener inputListener = new InputListener();
		text_A.setName("A");
		text_A.addKeyListener(inputListener);
		text_B.setName("B");
		text_B.addKeyListener(inputListener);
		text_C.setName("C");
		text_C.addKeyListener(inputListener);
		text_D.setName("D");
		text_D.addKeyListener(inputListener);
		text_E.setName("E");
		text_E.addKeyListener(inputListener);
		text_F.setName("F");
		text_F.addKeyListener(inputListener);
		text_G.setName("G");
		text_G.addKeyListener(inputListener);
		text_H.setName("H");
		text_H.addKeyListener(inputListener);
		text_I.setName("I");
		text_I.addKeyListener(inputListener);
		text_J.setName("J");
		text_J.addKeyListener(inputListener);
		text_K.setName("K");
		text_K.addKeyListener(inputListener);
		text_L.setName("L");
		text_L.addKeyListener(inputListener);
		text_M.setName("M");
		text_M.addKeyListener(inputListener);
		text_N.setName("N");
		text_N.addKeyListener(inputListener);
		text_O.setName("O");
		text_O.addKeyListener(inputListener);
		text_P.setName("P");
		text_P.addKeyListener(inputListener);
		text_Q.setName("Q");
		text_Q.addKeyListener(inputListener);
		text_R.setName("R");
		text_R.addKeyListener(inputListener);
		text_S.setName("S");
		text_S.addKeyListener(inputListener);
		text_T.setName("T");
		text_T.addKeyListener(inputListener);
		text_U.setName("U");
		text_U.addKeyListener(inputListener);
		text_V.setName("V");
		text_V.addKeyListener(inputListener);
		text_W.setName("W");
		text_W.addKeyListener(inputListener);
		text_X.setName("X");
		text_X.addKeyListener(inputListener);
		text_Y.setName("Y");
		text_Y.addKeyListener(inputListener);
		text_Z.setName("Z");
		text_Z.addKeyListener(inputListener);
		
		cipherArea.addMouseListener(new MouseListener() { // clear the text in text area when mouse clicked.

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				cipherArea.setText("");
				Thread textDeamon = new Thread(new WatchDog());
				textDeamon.start();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		open_file.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent action) {
				// TODO Auto-generated method stub
				file_chooser.setDialogTitle("Open File");
				int flag = file_chooser.showOpenDialog(main_frame);
				if (flag == JFileChooser.APPROVE_OPTION) {
					ExecutorService workers = Executors.newFixedThreadPool(3);
					src_file = file_chooser.getSelectedFile();
					Reader f_reader = null;
					try {
						f_reader = new InputStreamReader(new FileInputStream(src_file));
						int r_buf;
						while ((r_buf = f_reader.read()) != -1) {							
							if (Character.isLetter((char) r_buf)) {
								target_string += Character.toUpperCase((char) r_buf);
							}
						}				
						f_reader.close();
						for (int i = 1; i <= 3; ++i) {
							workers.submit(createCounter(i, target_string, english_frequency_list));
						}
						workers.shutdown();
						workers.awaitTermination(10, TimeUnit.SECONDS);
						
//						for (List<Map.Entry<String, Float>> list : english_frequency_list) {
//							for (Map.Entry<String, Float> entry : list) {
//								System.out.println(entry.getKey() + ":" + entry.getValue());
//							}
//						}
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					setFrequencies(english_single, english_digram, english_trigram, english_frequency_list);
				}
			}
		});
		
		quit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent action) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
	}
	
	private void launch() {
		main_frame.pack();
		main_frame.setVisible(true);
	}
	
	public static void main(String args[]) {
		Analyzer analyzer = new Analyzer();
		analyzer.launch();
	}

	private class InputListener extends KeyAdapter {
		public void keyReleased(KeyEvent e) {	
			String plaintext = plainArea.getText();
            JTextField sourceField = (JTextField) e.getSource();
            String text = sourceField.getText();
            if (text.length() != 0) {
            	//System.out.println(text + ", " + plaintext + ", " + sourceField.getName());
	            sourceField.setText(text.toUpperCase());
	            subs_records.put(sourceField.getName(), text);
	            plaintext = plaintext.replace(sourceField.getName(), text);
            } else {         
            	
            	text = subs_records.get(sourceField.getName());
            	//System.out.println(text + ", " + plaintext + ", " + sourceField.getName());
	            subs_records.remove(sourceField.getName());
	            plaintext = plaintext.replace(text, sourceField.getName());
            }
            
            plainArea.setText(plaintext);
        }

        public void keyTyped(KeyEvent e) {
            // TODO: Do something for the keyTyped event
        }

        public void keyPressed(KeyEvent e) {
            // TODO: Do something for the keyPressed event
        }
	}
	
}
