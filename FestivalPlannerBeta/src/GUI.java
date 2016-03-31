import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;

	Object[][] rows;
	Agenda agenda;
	File festivalFile = null;
	DefaultTableModel model;
	TiledTileMap tiled = new TiledTileMap();

	public GUI() {

		super("Festival Planner");

		String[] columns = new String[] { "Artist", "Stage", "Start Time", "End Time", "Popularity" };
		agenda = new Agenda();

		JPanel content = new JPanel(new BorderLayout());
		JTable table = new JTable(new DefaultTableModel(rows, columns));

		model = (DefaultTableModel) table.getModel();

		JScrollPane scrollPane = new JScrollPane(table);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new FlowLayout(FlowLayout.LEFT));

		this.setJMenuBar(menuBar);

		JMenu options = new JMenu("Options");
		menuBar.add(options);

		JMenuItem open = new JMenuItem("Open");
		options.add(open);

		JMenuItem save = new JMenuItem("Save");
		options.add(save);

		JMenuItem deleteAll = new JMenuItem("Delete all rows");
		options.add(deleteAll);

		JMenuItem undo = new JMenuItem("Undo");
		options.add(undo);

		JMenuItem changeActivity = new JMenuItem("Change activity");
		options.add(changeActivity);

		JMenuItem addArtist = new JMenuItem("Add artist");
		menuBar.add(addArtist);

		JMenuItem addItem = new JMenuItem("Add activity");
		menuBar.add(addItem);

		JMenuItem simulation = new JMenuItem("Start stimulation");
		menuBar.add(simulation);

		JMenuItem info = new JMenuItem("Information");
		menuBar.add(info);

		JMenuItem exit = new JMenuItem("Exit");
		menuBar.add(exit);

		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser(festivalFile);
				if (fileChooser.showSaveDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {
					festivalFile = fileChooser.getSelectedFile();
					ObjectOutputStream oos = null;
					try {
						oos = new ObjectOutputStream(new FileOutputStream(festivalFile));
						oos.writeObject(agenda);
						oos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						if (oos != null) {

						}
					}
				}

			}
		});

		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(festivalFile);
				if (fileChooser.showOpenDialog(GUI.this) == JFileChooser.APPROVE_OPTION) {

					festivalFile = fileChooser.getSelectedFile();
					try {
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream(festivalFile));
						agenda = (Agenda) ois.readObject();
						System.out.println(agenda);
						updateTable();
						agenda.test();
						repaint();
					} catch (IOException e1) {
						e1.printStackTrace();
					} catch (ClassNotFoundException e2) {
						e2.printStackTrace();
					}
				}
			}
		});

		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteAllRows();
			}
		});

		addArtist.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				addArtist();
			}
		});

		addItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				addActivity();
			}
		});

		changeActivity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeActivity(0);

			}
		});

		simulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tiled.makeGUI();
			}
		});

		info.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(GUI.this, "This project was made by A4 o3o");
			}
		});

		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (SwingUtilities.isLeftMouseButton(me) == true) {
					int row = table.rowAtPoint(me.getPoint());

					table.clearSelection();
					table.addRowSelectionInterval(row, row);
				}
				JPopupMenu popupMenu = new JPopupMenu();
				JMenuItem menuItemAdd = new JMenuItem("Add activity");
				JMenuItem menuItemArtist = new JMenuItem("Add artist");
				JMenuItem menuItemRemove = new JMenuItem("Remove selected activity");
				JMenuItem menuItemChange = new JMenuItem("Change selected activity");
				JMenuItem menuItemRemoveAll = new JMenuItem("Remove all activities");

				menuItemAdd.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addActivity();
					}
				});

				menuItemArtist.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						addArtist();
					}
				});

				menuItemRemove.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int selectedRow = table.getSelectedRow();
						if (selectedRow != -1) {
							model.removeRow(selectedRow);
							agenda.getActivity().remove(selectedRow);
						}
					}
				});

				menuItemChange.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int selectedRow = table.getSelectedRow();
						if (selectedRow != -1) {
							changeActivity(table.getSelectedRow());
						}
					}
				});

				menuItemRemoveAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						deleteAllRows();
					}
				});
				if (SwingUtilities.isLeftMouseButton(me) == true) {
					popupMenu.add(menuItemRemove);
					popupMenu.add(menuItemChange);
				} else {
					table.clearSelection();
					popupMenu.remove(menuItemRemove);
					popupMenu.remove(menuItemChange);
				}
				popupMenu.add(menuItemAdd);
				popupMenu.add(menuItemArtist);
				popupMenu.add(menuItemRemoveAll);

				table.setComponentPopupMenu(popupMenu);
			}

		});

		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		table.setAutoCreateRowSorter(true);
		table.setFillsViewportHeight(true);
		setContentPane(content);
		content.add(scrollPane);
		table.setEnabled(false);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
		setResizable(false);

	}

	public void addActivity() {

		JFrame addFrame = new JFrame("Add activity");
		JPanel addPanel = new JPanel(null);

		addFrame.setContentPane(addPanel);

		JButton addButton = new JButton("Add");
		JLabel artistLabel = new JLabel("Artist");
		JLabel stageLabel = new JLabel("Stage");
		JLabel startLabel = new JLabel("Start time");
		JLabel endLabel = new JLabel("End time");
		JLabel hdLabel = new JLabel(":");
		JLabel mdLabel = new JLabel(":");
		JTextField startHourField = new JTextField();
		JTextField endHourField = new JTextField();
		JTextField startMinField = new JTextField();
		JTextField endMinField = new JTextField();
		JComboBox<String> stagesBox = new JComboBox<String>();
		JComboBox<String> artistBox = new JComboBox<String>();

		for (Stage s : agenda.getStages())
			stagesBox.addItem(s.getStage());

		for (Artist a : agenda.getArtists()) {
			artistBox.addItem(a.getName());
		}

		addPanel.add(artistLabel);
		addPanel.add(stageLabel);
		addPanel.add(startLabel);
		addPanel.add(endLabel);
		addPanel.add(addButton);
		addPanel.add(stagesBox);
		addPanel.add(artistBox);
		addPanel.add(hdLabel);
		addPanel.add(mdLabel);
		addPanel.add(startHourField);
		addPanel.add(endHourField);
		addPanel.add(startMinField);
		addPanel.add(endMinField);

		artistLabel.setBounds(22, 30, 70, 20);
		stageLabel.setBounds(20, 60, 70, 20);
		stagesBox.setBounds(120, 60, 110, 20);
		artistBox.setBounds(120, 30, 110, 20);
		startLabel.setBounds(20, 90, 90, 20);
		endLabel.setBounds(21, 120, 90, 20);
		startHourField.setBounds(120, 90, 30, 20);
		endHourField.setBounds(120, 120, 30, 20);
		hdLabel.setBounds(155, 90, 10, 20);
		mdLabel.setBounds(155, 120, 10, 20);
		startMinField.setBounds(165, 90, 30, 20);
		endMinField.setBounds(165, 120, 30, 20);
		addButton.setBounds(90, 150, 60, 20);

		addButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String name = (String) artistBox.getSelectedItem();
				Stage stage = agenda.getStages().get(stagesBox.getSelectedIndex());
				int rating = agenda.getArtists().get(artistBox.getSelectedIndex()).getPop();
				int bHour = Integer.parseInt(startHourField.getText());
				int bMin = Integer.parseInt(startMinField.getText());
				int eHour = Integer.parseInt(endHourField.getText());
				int eMin = Integer.parseInt(endMinField.getText());

				agenda.addActivity(name, bHour, bMin, eHour, eMin, rating, stage);
				int counter = agenda.getActivity().size() - 1;
				model.addRow(new Object[] { name, (String) stagesBox.getSelectedItem(),
						agenda.getActivity().get(counter).getStartTime(),
						agenda.getActivity().get(counter).getEndTime(),
						agenda.getActivity().get(counter).getPopularity() });

				addFrame.setVisible(false);
			}
		});

		addFrame.setVisible(true);
		addFrame.setSize(270, 240);
		addFrame.setLocationRelativeTo(null);
	}

	public void addArtist() {
		JFrame artistFrame = new JFrame("Artist");
		JPanel artistPanel = new JPanel(null);

		JButton addArtistButton = new JButton("Add");
		JTextField addArtistNameField = new JTextField();
		JTextField addArtistPopField = new JTextField();
		JLabel nameLabel = new JLabel("Name");
		JLabel ratingLabel = new JLabel("Rating");

		artistFrame.setContentPane(artistPanel);
		artistFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		artistPanel.add(nameLabel);
		artistPanel.add(ratingLabel);
		artistPanel.add(addArtistButton);
		artistPanel.add(addArtistNameField);
		artistPanel.add(addArtistPopField);

		nameLabel.setBounds(100, 20, 70, 20);
		ratingLabel.setBounds(180, 20, 70, 20);
		addArtistButton.setBounds(20, 50, 70, 20);
		addArtistNameField.setBounds(100, 50, 70, 20);
		addArtistPopField.setBounds(180, 50, 40, 20);

		artistFrame.setVisible(true);
		artistFrame.setSize(280, 150);
		artistFrame.setLocationRelativeTo(null);
		artistFrame.setResizable(false);

		addArtistButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String artistName = addArtistNameField.getText();
				addArtistNameField.setText("");

				int artistRating = Integer.parseInt(addArtistPopField.getText());
				addArtistPopField.setText("");

				if (artistRating < 0 || artistRating > 10) {
					JOptionPane.showMessageDialog(GUI.this, "No valid data!");
					return;
				}

				agenda.addArtist(artistName, artistRating);

				artistFrame.setVisible(false);
				artistFrame.getRootPane().setDefaultButton(addArtistButton);
				artistPanel.removeAll();

			}
		});

	}

	public void updateTable() {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		for (Activity a : agenda.getActivity()) {
			model.addRow(new Object[] { a.getName(), a.getStage().getStage(), a.getStartTime(), a.getEndTime(),
					a.getPopularity() });

		}
	}

	public void changeActivity(int index) {
		JFrame changeFrame = new JFrame("Change activity");
		JPanel changePanel = new JPanel(null);
		changeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		changeFrame.setContentPane(changePanel);

		JButton changeButton = new JButton("Change");

		JTextField beginHourField = new JTextField();
		JTextField endHourField = new JTextField();
		JTextField beginMinField = new JTextField();
		JTextField endMinField = new JTextField();
		JTextField popularityField = new JTextField();

		JComboBox<String> activityBox = new JComboBox<String>();
		int item = 0;

		for (Activity a : agenda.getActivity()) {
			item++;
			activityBox.addItem("Activity: " + item);
		}

		activityBox.setSelectedIndex(index);

		JComboBox<String> stageBox = new JComboBox<String>();
		for (Stage s : agenda.getStages())
			stageBox.addItem(s.getStage());

		JLabel beginTimeLabel = new JLabel("Begin time");
		JLabel endTimeLabel = new JLabel("End time");
		JLabel hd = new JLabel(":");
		JLabel md = new JLabel(":");
		JLabel popularityLabel = new JLabel("Rating");
		JLabel stageLabel = new JLabel("Stages");
		JLabel activityLabel = new JLabel("Activities");

		changePanel.add(changeButton);
		changePanel.add(beginHourField);
		changePanel.add(endHourField);
		changePanel.add(beginMinField);
		changePanel.add(endMinField);
		changePanel.add(hd);
		changePanel.add(md);
		changePanel.add(popularityField);
		changePanel.add(stageBox);
		changePanel.add(activityBox);
		changePanel.add(beginTimeLabel);
		changePanel.add(endTimeLabel);
		changePanel.add(popularityLabel);
		changePanel.add(stageLabel);
		changePanel.add(activityLabel);

		if (agenda.getActivity().size() > 0) {
			beginHourField.setText("" + agenda.getActivity().get(index).getStartHour());
			endHourField.setText("" + agenda.getActivity().get(index).getEndHour());
			beginMinField.setText("" + agenda.getActivity().get(index).getStartMin());
			endMinField.setText("" + agenda.getActivity().get(index).getEndMin());
			popularityField.setText("" + agenda.getActivity().get(index).getRating());
			stageBox.setSelectedItem(agenda.getActivity().get(index).getStage().getStage());
		}

		activityBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beginHourField.setText("" + agenda.getActivity().get(activityBox.getSelectedIndex()).getStartHour());
				endHourField.setText("" + agenda.getActivity().get(activityBox.getSelectedIndex()).getEndHour());
				beginMinField.setText("" + agenda.getActivity().get(activityBox.getSelectedIndex()).getStartMin());
				endMinField.setText("" + agenda.getActivity().get(activityBox.getSelectedIndex()).getEndMin());
				popularityField.setText("" + agenda.getActivity().get(activityBox.getSelectedIndex()).getRating());
				stageBox.setSelectedItem(
						agenda.getActivity().get(activityBox.getSelectedIndex()).getStage().getStage());

			}
		});

		changeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Integer.parseInt(popularityField.getText()) < 0
						|| Integer.parseInt(popularityField.getText()) > 10) {
					JOptionPane.showMessageDialog(GUI.this, "M8, u fucked up");
					return;
				}

				agenda.getActivity().get(activityBox.getSelectedIndex())
						.setPopularity(Integer.parseInt(popularityField.getText()));
				agenda.getActivity().get(activityBox.getSelectedIndex()).getStage()
						.setStage(stageBox.getSelectedItem().toString());
				agenda.getActivity().get(activityBox.getSelectedIndex())
						.setStartHour(Integer.parseInt(beginHourField.getText()));
				agenda.getActivity().get(activityBox.getSelectedIndex())
						.setStartMin(Integer.parseInt(beginMinField.getText()));
				agenda.getActivity().get(activityBox.getSelectedIndex())
						.setEndHour(Integer.parseInt(endHourField.getText()));
				agenda.getActivity().get(activityBox.getSelectedIndex())
						.setEndMin(Integer.parseInt(endMinField.getText()));
				agenda.getActivity().get(activityBox.getSelectedIndex()).setStartTime(
						Integer.parseInt(beginHourField.getText()), Integer.parseInt(beginMinField.getText()));
				agenda.getActivity().get(activityBox.getSelectedIndex())
						.setEndTime(Integer.parseInt(endHourField.getText()), Integer.parseInt(endMinField.getText()));
				// model.removeRow(row);

				updateTable();

				changeFrame.setVisible(false);
				changePanel.removeAll();
			}
		});

		activityBox.setBounds(120, 50, 100, 20);
		stageBox.setBounds(120, 80, 100, 20);

		changeButton.setBounds(160, 140, 100, 20);
		beginHourField.setBounds(120, 110, 30, 20);
		endHourField.setBounds(290, 110, 30, 20);
		beginMinField.setBounds(170, 110, 30, 20);
		endMinField.setBounds(340, 110, 30, 20);
		hd.setBounds(155, 110, 10, 20);
		md.setBounds(325, 110, 10, 20);
		popularityField.setBounds(290, 80, 70, 20);

		beginTimeLabel.setBounds(50, 110, 70, 20);
		endTimeLabel.setBounds(230, 110, 70, 20);
		popularityLabel.setBounds(230, 80, 70, 20);
		stageLabel.setBounds(50, 80, 70, 20);
		activityLabel.setBounds(50, 50, 70, 20);

		changeFrame.setVisible(true);
		changeFrame.setSize(440, 250);
		changeFrame.setLocationRelativeTo(null);

	}

	public void deleteAllRows() {
		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}

		for (int i = 0; i <= agenda.getActivity().size(); i++) {
			agenda.getActivity().remove(i);
		}
	}
}
