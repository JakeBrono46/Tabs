import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

public class Lists {

	private JFrame frame;
	private JPanel content, player, team;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem item;
	private JLabel name, num, position, points, rebounds, assists;
	private JTextField playerName, playerNum, playerPosition, playerPts, playerRbs, playerAsts;
	private JFileChooser fc;
	private TreeMap<Integer, Player> map;
	private JTabbedPane tabs;
	private JTextArea textarea;
	private JScrollPane scroll;
	
	public static void main(String[] args) {
		Lists list = new Lists();
		list.start();
	}

	public void start() {
		frame = new JFrame("Lists");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		content = (JPanel)frame.getContentPane();
		
		makeMenu();
		makeContent();
		
		frame.pack();
		frame.setVisible(true);
	}
	
	private JMenu help() {
		//Help menu
		menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		
		//add About menu item
		item = new JMenuItem("About Tabs");
		item.setMnemonic(KeyEvent.VK_I);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, "Tabs\n\nVersion 1.0\nBuild B200803275-1720\n\n" + "(c) Copyright Merrill Hall 2008\nAll rights reserved\n\n" + "Visit\nEducation To Go\n" + "Intermediate Java Course", "About Tabs", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		menu.add(item);
		
		return menu;
	}

	private JMenu view() {
		//View Menu
		menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		
		//Next Player menu item
		item = new JMenuItem("Next Player");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(map == null || map.size() == 0) {
					return;
				}
				
				Map.Entry<Integer, Player>entry = map.higherEntry(Integer.parseInt(playerNum.getText()));
				
				if(entry == null) {
					JOptionPane.showMessageDialog(frame, "There are no more players.\nYou have reached the end of the list", "End of List", JOptionPane.WARNING_MESSAGE);
				}else {
					getPlayer(entry.getValue());
				}
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, Event.ALT_MASK));
		menu.add(item);
		
		//Previous Player
		item = new JMenuItem("Previous Player");
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(map == null || map.size() == 0) {
					return;
				}
				
				Map.Entry<Integer, Player>entry = map.lowerEntry(Integer.parseInt(playerNum.getText()));
				
				if(entry == null) {
					JOptionPane.showMessageDialog(frame, "There are no previous players.\nYou are at the start of the list", "Start of List", JOptionPane.WARNING_MESSAGE);
				}else {
					getPlayer(entry.getValue());
				}
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, Event.ALT_MASK));
		menu.add(item);
		
		//add Find Player
		menu.addSeparator();
		item = new JMenuItem("Find Player");
		item.setMnemonic(KeyEvent.VK_F);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				findPlayer();
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK));
		menu.add(item);
		
		return menu;
	}

	private JMenu file() {
		//File Menu
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		
		//Open menu item
		item = new JMenuItem("Open...");
		item.setMnemonic(KeyEvent.VK_O);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fc = new JFileChooser();
				fc.showOpenDialog(frame);
				
				File playerFile = fc.getSelectedFile();
				
				if(playerFile == null) {
					return;
				}
				
				map = new TreeMap<Integer, Player>();
				
				try {
					Scanner console = new Scanner(playerFile);
					
					while(console.hasNext()) {
						String name = console.next() + " " + console.next();
						int num = console.nextInt();
						char position = console.next().charAt(0);
						double points = console.nextDouble();
						double rebounds = console.nextDouble();
						double assists = console.nextDouble();
						
						map.put(new Integer(num),(new Player(name, num, position, points, rebounds, assists)));
					}
					
					console.close();
					
				}catch(IOException ioe) {
					JOptionPane.showMessageDialog(frame, "I/O Error in file\n\n   " + playerFile.getName() + "\n\nThis program will close", "I/O Error", JOptionPane.ERROR_MESSAGE);
					
					System.exit(1);
				}
				
				findPlayer();
				
				for(Player p : map.values()) {
					textarea.setText(textarea.getText() + p.toString() + "\n\n");
				}
			}
		});
		
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK));
		menu.add(item);
		
		//Exit menu item
		menu.addSeparator();
		item = new JMenuItem("Exit");
		item.setMnemonic(KeyEvent.VK_X);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, Event.CTRL_MASK));
		menu.add(item);
		
		return menu;
	}
	
	private void findPlayer() {
		boolean isGoodNum = false;
		Integer playerNum = new Integer(0);
		
		while(!isGoodNum) {
			try {
				playerNum = new Integer(Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter a player number: ", "Player Entry", JOptionPane.QUESTION_MESSAGE)));
				
				isGoodNum = true;
			}catch(NumberFormatException n){
				JOptionPane.showMessageDialog(frame, "That wasn't a player number!", "Player Number Error", JOptionPane.ERROR_MESSAGE);
			}
			
			if(isGoodNum) {
				Player p = map.get(playerNum);
				
				if(p == null) {
					JOptionPane.showMessageDialog(frame, "Player number " + playerNum.intValue() + "does not exist!", "Player Number Error", JOptionPane.ERROR_MESSAGE);
					
					isGoodNum = false;
				}else {
					getPlayer(p);
				}
			}
		}
	}
	
	private void getPlayer(Player p) {
		playerName.setText(p.getName());
		playerNum.setText("" + p.getNum());
		playerPosition.setText("" + p.getPosition());
		playerPts.setText("" + p.getPoints());
		playerRbs.setText("" + p.getRebounds());
		playerAsts.setText("" + p.getAssists());
	}
		
	private void makeContent() {
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		tabs = new JTabbedPane();
		
		//Player tab
		player = new JPanel();
		player.setLayout(new BoxLayout(player, BoxLayout.Y_AXIS));
		player.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		//Team tab
		team = new JPanel();
		team.setLayout(new BoxLayout(team, BoxLayout.Y_AXIS));
		team.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		
		textarea = new JTextArea(15, 25);
		scroll = new JScrollPane(textarea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		team.add(scroll);
		
		Name();
		Number();
		Position();
		Points();
		Rebounds();
		Assists();
		
		//Add Player View tab
		tabs.addTab("Player View", player);
		tabs.setMnemonicAt(0, KeyEvent.VK_P);
		
		//Add Team View tab
		tabs.addTab("Team View", team);
		tabs.setMnemonicAt(1, KeyEvent.VK_T);
		
		content.add(tabs);
	}

	private void Assists() {
		//Player assists
		
		//Add label
		assists = new JLabel("Assists Per Game");
		assists.setFont(new Font("Trebuchet MS", Font.BOLD + Font.ITALIC, 14));
		player.add(assists);
		
		playerAsts = new JTextField();
		playerAsts.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		playerAsts.setForeground(Color.BLUE);
		player.add(playerAsts);
	}

	private void Rebounds() {
		//Player rebounds
		
		//Add label
		rebounds = new JLabel("Rebounds Per Game");
		rebounds.setFont(new Font("Trebuchet MS", Font.BOLD + Font.ITALIC, 14));
		player.add(rebounds);
		
		//Add textfield
		playerRbs = new JTextField();
		playerRbs.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		playerRbs.setForeground(Color.BLUE);
		player.add(playerRbs);
	}

	private void Points() {
		//Player points
		
		//Add label
		points = new JLabel("Points Per Game");
		points.setFont(new Font("Trebuchet MS", Font.BOLD + Font.ITALIC, 14));
		player.add(points);
		
		//Add textfield
		playerPts = new JTextField();
		playerPts.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		playerPts.setForeground(Color.BLUE);
		player.add(playerPts);
	}

	private void Position() {
		//Player Position
		
		//Add label
		position = new JLabel("Player Position");
		position.setFont(new Font("Trebuchet MS", Font.BOLD + Font.ITALIC, 14));
		player.add(position);
		
		//Add textfield
		playerPosition = new JTextField();
		playerPosition.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		playerPosition.setForeground(Color.BLUE);
		player.add(playerPosition);
	}

	private void Number() {
		//Player number
		
		//Add label
		num = new JLabel("Player Number");
		num.setFont(new Font("Trebuchet MS", Font.BOLD + Font.ITALIC, 14));
		player.add(num);
		
		//Add textfield
		playerNum = new JTextField();
		playerNum.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		playerNum.setForeground(Color.BLUE);
		player.add(playerNum);
	}

	private void Name() {
		//Player name
		
		//Add label
		name = new JLabel("Player Name");
		name.setFont(new Font("Trebuchet MS", Font.BOLD + Font.ITALIC, 14));
		player.add(name);
		
		//Add textfield
		playerName = new JTextField();
		playerName.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		playerName.setForeground(Color.BLUE);
		player.add(playerName);
	}

	private void makeMenu() {
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		//Set up menus
		menuBar.add(file());
		menuBar.add(view());
		menuBar.add(help());
	}
}
