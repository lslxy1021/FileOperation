package fileoperation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import bufferedIO.BMerge;
import bufferedIO.BSegment;
import io.Merge;
import io.Segment;
import nio.NIOMerge;
import nio.NIOSegment;

public class FileGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel addr;
	private JTextField path;
	private JButton explorer;
	private JButton seperate;
	private JButton combination;
	private JLabel numberInfo;
	private JTextField number;
	private JLabel tips;
	private JLabel size;
	private JTextArea info;
	private JPanel mainPanel;
	private JFileChooser filechooser;
	private String suffix;
	private File file;

	public FileGUI(int type) {
		setTitle("File Operation");
		setSize(550, 420);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize(); // 获取屏幕分辨率
		int scrWidth = (int) scrSize.getWidth();
		int scrHeight = (int) scrSize.getHeight();
		setLocation((scrWidth - getWidth()) / 2, (scrHeight - getHeight()) / 2);
		setResizable(false);
		setLayout(new BorderLayout());

		addr = new JLabel("文件(路径): ");
		path = new JTextField(20);
		explorer = new JButton("浏览");
		seperate = new JButton("分割");
		combination = new JButton("合并");
		numberInfo = new JLabel("请输入分割个数:");
		number = new JTextField(5);
		tips = new JLabel("(大于0且小于文件大小的正整数)");
		info = new JTextArea();
		size = new JLabel();
		mainPanel = new JPanel();

		info.setLineWrap(true);// 激活自动换行

		Font f = new Font("微软雅黑", Font.PLAIN, 15);
		addr.setFont(f);
		explorer.setFont(f);
		seperate.setFont(f);
		combination.setFont(f);
		numberInfo.setFont(f);

		SimpleDateFormat dateFormat = new SimpleDateFormat("  HH:mm:ss");// 设置时间格式,HH:24小时制，hh:12小时制

		explorer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				filechooser = new JFileChooser();
				filechooser.showOpenDialog(null);
				try {
					file = filechooser.getSelectedFile();
					path.setText(file.getAbsolutePath());
					suffix = file.getName().substring(file.getName().indexOf("."), file.getName().length());// 后缀名
				} catch (NullPointerException ex) {

				}

			}

		});
		seperate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (path.getText().length() != 0) { // 此处是空字符串，不是null
					file = new File(path.getText());
					if (!file.exists() || file.isDirectory()) {
						String fDate = dateFormat.format(new Date());
						info.append("文件不存在" + fDate + "\r\n");
					} else {

						/*
						 * 当事件派发线程（EDT）中正在执行的事件监听函数执行完毕，才能进行UI组件的刷新，
						 * 因此将耗时的操作放在一个新的工作线程中执行。
						 * SwingUtilities的invokeLater和invokeAndWait方法可以将一个可执行对象（
						 * Runnable）实例追加到EDT的可执行队列中。
						 */
						new Thread(new Runnable() {

							@Override
							public void run() {
								suffix = file.getName().substring(file.getName().indexOf("."), file.getName().length());
								try {
									int fileNumber = Integer.parseInt(number.getText());
									SwingUtilities.invokeLater(new Runnable() {

										@Override
										public void run() {
											String fDate = dateFormat.format(new Date());
											info.append("文件开始分割......" + "   " + fDate + "\r\n");
										}

									});

									if (fileNumber > 0 && fileNumber < file.length()) {
										try {
											if (type == 0) {
												Segment.fileSeperate(file, fileNumber, suffix);// 非缓冲IO
											} else if (type == 1) {
												BSegment.fileSeperate(file, fileNumber, suffix);// 缓冲IO
											} else {
												NIOSegment.fileSeperate(file, fileNumber, suffix);// MappedByteBuffer
											}
										} catch (Exception e1) {
											e1.printStackTrace();
										}
										SwingUtilities.invokeLater(new Runnable() {

											@Override
											public void run() {
												String fDate = dateFormat.format(new Date());
												info.append("文件分割完成" + "   " + fDate + "\r\n");
											}

										});

									}
								} catch (NumberFormatException n) {
									SwingUtilities.invokeLater(new Runnable() {

										@Override
										public void run() {
											String fDate = dateFormat.format(new Date());
											info.append("请正确输入文件分割个数" + "   " + fDate + "\r\n");
										}

									});

								}
							}

						}).start();
						;

					}

				} else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							String fDate = dateFormat.format(new Date());
							info.append("请选择要分割的文件" + "  " + fDate + "\r\n");
						}

					});

				}

			}
		});

		combination.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						if (path.getText().length() != 0) {

							if (path.getText().endsWith("txt")) {
								SwingUtilities.invokeLater(new Runnable() {

									@Override
									public void run() {
										String fDate = dateFormat.format(new Date());
										info.append("文件开始合并......" + "   " + fDate + "\r\n");
									}

								});

								suffix = file.getName().substring(file.getName().indexOf("."),
										file.getName().length() - 4);
								File parentFile = file.getParentFile();
								String destPath = parentFile.toString() + "\\"
										+ file.getName().substring(0, file.getName().indexOf(".")) + suffix;
								try {
									if (type == 0) {
										Merge.fileCombination(file, destPath);
									} else if (type == 1) {
										BMerge.fileCombination(file, destPath);
									} else {
										NIOMerge.fileCombination(file, destPath);
									}

								} catch (Exception e1) {
									e1.printStackTrace();
								}
								SwingUtilities.invokeLater(new Runnable() {

									@Override
									public void run() {
										String fDate = dateFormat.format(new Date());
										info.append("文件合并完成" + "   " + fDate + "\r\n");
									}

								});

							} else {
								SwingUtilities.invokeLater(new Runnable() {

									@Override
									public void run() {
										String fDate = dateFormat.format(new Date());
										info.append("请选择正确的txt文档" + "   " + fDate + "\r\n");
									}

								});

							}
						} else {
							String fDate = dateFormat.format(new Date());
							SwingUtilities.invokeLater(() -> info.append("请选择合并文件的txt文档" + "   " + fDate + "\r\n"));

						}
					}

				}).start();
				;

			}
		});

		number.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String fDate;
				if (number.getText() != null) {
					if (path.getText() != null) {
						file = new File(path.getText());
						if (!file.exists() || file.isDirectory()) {
							fDate = dateFormat.format(new Date());
							info.append("请正确选择文件" + "   " + fDate + "\r\n");
						} else {
							if (number.getText().matches("[0-9]+")) {
								int numFile = 0;
								try {
									numFile = Integer.parseInt(number.getText());
								} catch (NumberFormatException en) {
									info.append("请输入合理的分割个数\r\n");
								}
								if (numFile > file.length() || numFile == 0) {
									info.append("请输入合理的分割个数\r\n");
								} else {
									long kBytes = (file.length() / numFile) / 1024;
									size.setText("   " + String.valueOf(kBytes) + "KB");
								}
							} else {
								fDate = dateFormat.format(new Date());
								info.append("请输入分割个数" + "   " + fDate + "\r\n");
							}
						}
					} else {
						fDate = dateFormat.format(new Date());
						info.append("请选择文件" + "   " + fDate + "\r\n");
					}
				} else {
					fDate = dateFormat.format(new Date());
					info.append("请输入分割个数" + "   " + fDate + "\r\n");
				}
			}

		});

		JPanel nPanel = new JPanel(), wPanel = new JPanel(), sPanel = new JPanel(), ePanel = new JPanel(),
				numPanel = new JPanel(), cPanel = new JPanel();

		mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		numPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

		cPanel.setLayout(new GridLayout(1, 1));
		nPanel.setLayout(new GridLayout(2, 1));

		mainPanel.add(addr);
		mainPanel.add(path);
		mainPanel.add(explorer);
		mainPanel.add(seperate);
		mainPanel.add(combination);
		numPanel.add(numberInfo);
		numPanel.add(number);
		numPanel.add(tips);
		numPanel.add(size);
		nPanel.add(mainPanel);
		nPanel.add(numPanel);
		cPanel.add(new JScrollPane(info));// 添加滚动条
		add(nPanel, BorderLayout.NORTH);
		add(cPanel, BorderLayout.CENTER);
		add(wPanel, BorderLayout.WEST);
		add(sPanel, BorderLayout.SOUTH);
		add(ePanel, BorderLayout.EAST);

		setVisible(true);
	}

	public static void main(String[] args) throws Exception {
		new FileGUI(0);// IO
		// new FileGUI(1);//BufferedIO
		// new FileGUI(2);//NIO

	}
}
