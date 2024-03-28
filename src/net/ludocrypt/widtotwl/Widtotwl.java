package net.ludocrypt.widtotwl;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Widtotwl extends JFrame {

	private static final long serialVersionUID = 4556194676401891632L;
	private JButton loadButton, saveButton, cycleButton, swap12Button, swap13Button, swap23Button;
	private JCheckBox geo, tiles, props;
	private JLabel feedback;
	private File selectedFile;
	private Level selectedLevel;

	public Widtotwl() {
		setTitle("Whoops I Did This on the Wrong Layer!");
		setSize(400, 200);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		loadButton = new JButton("Load");
		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		cycleButton = new JButton("Cycle 1 > 2 > 3");
		cycleButton.setEnabled(false);
		swap12Button = new JButton("Swap 1 & 2");
		swap12Button.setEnabled(false);
		swap13Button = new JButton("Swap 1 & 3");
		swap13Button.setEnabled(false);
		swap23Button = new JButton("Swap 2 & 3");
		swap23Button.setEnabled(false);

		JCheckBox geo = new JCheckBox("Geometry");
		geo.setEnabled(false);
		JCheckBox tiles = new JCheckBox("Tiles");
		tiles.setEnabled(false);
		JCheckBox props = new JCheckBox("Prop Depth");
		props.setEnabled(false);

		feedback = new JLabel("Drag and Drop File...", JLabel.CENTER);
		feedback.setFont(new Font("Arial", Font.PLAIN, 16));

		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		contentPane.add(loadButton);
		contentPane.add(saveButton);
		contentPane.add(cycleButton);
		contentPane.add(swap12Button);
		contentPane.add(swap13Button);
		contentPane.add(swap23Button);
		contentPane.add(geo);
		contentPane.add(tiles);
		contentPane.add(props);
		contentPane.add(feedback);

		loadButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					selectedLevel = Level.parse(selectedFile);

					if (selectedLevel != null) {
						saveButton.setEnabled(true);
						cycleButton.setEnabled(true);
						swap12Button.setEnabled(true);
						swap13Button.setEnabled(true);
						swap23Button.setEnabled(true);
						geo.setEnabled(true);
						tiles.setEnabled(true);
						props.setEnabled(true);
						geo.setSelected(true);
						tiles.setSelected(true);
						props.setSelected(true);
						feedback.setText("Successfully Loaded File!");
					} else {
						feedback.setText("Failed to Load File!");
					}

				}

			}

		});

		saveButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (selectedLevel != null) {

					String saveFilePath = selectedFile.getPath().replace(".txt", "_widtotwl.txt");

					if ((e.getModifiers() & ActionEvent.SHIFT_MASK) != 0) {
						JFileChooser fileChooser = new JFileChooser();
						int returnValue = fileChooser.showSaveDialog(null);

						if (returnValue == JFileChooser.APPROVE_OPTION) {
							saveFilePath = fileChooser.getSelectedFile().getPath();

							if (getFileExtension(saveFilePath).equals("")) {
								saveFilePath += ".txt";
							}

						}

					} else {

					}

					try {
						String modifiedFilePath = saveFilePath;
						BufferedWriter writer = new BufferedWriter(new FileWriter(modifiedFilePath));
						writer.write(selectedLevel.save().toString());
						writer.close();
						feedback.setText("Successfully Saved File!");
					} catch (IOException c) {
						feedback.setText("Failed to Save File!");
						c.printStackTrace();
					}

				}

			}

		});

		cycleButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (selectedLevel != null) {
					selectedLevel.cycle(geo.isSelected(), tiles.isSelected(), props.isSelected());
					feedback.setText("Successfully Cycled Layers");
				}

			}

		});

		swap12Button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (selectedLevel != null) {
					selectedLevel.swap12(geo.isSelected(), tiles.isSelected(), props.isSelected());
					feedback.setText("Successfully Swapped Layers 1 and 2");
				}

			}

		});

		swap13Button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (selectedLevel != null) {
					selectedLevel.swap13(geo.isSelected(), tiles.isSelected(), props.isSelected());
					feedback.setText("Successfully Swapped Layers 1 and 3");
				}

			}

		});

		swap23Button.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if (selectedLevel != null) {
					selectedLevel.swap23(geo.isSelected(), tiles.isSelected(), props.isSelected());
					feedback.setText("Successfully Swapped Layers 2 and 3");
				}

			}

		});

		DropTarget dropTarget = new DropTarget(this, new DropTargetAdapter() {

			public void drop(DropTargetDropEvent event) {

				try {
					event.acceptDrop(DnDConstants.ACTION_COPY);
					Transferable transferable = event.getTransferable();

					if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						List<File> fileList = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

						if (!fileList.isEmpty()) {
							selectedFile = fileList.get(0);
							selectedLevel = Level.parse(selectedFile);

							if (selectedLevel != null) {
								saveButton.setEnabled(true);
								cycleButton.setEnabled(true);
								swap12Button.setEnabled(true);
								swap13Button.setEnabled(true);
								swap23Button.setEnabled(true);
								geo.setEnabled(true);
								tiles.setEnabled(true);
								props.setEnabled(true);
								geo.setSelected(true);
								tiles.setSelected(true);
								props.setSelected(true);
								feedback.setText("Successfully Loaded File!");
							} else {
								feedback.setText("Failed to Load File!");
							}

						}

					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}

		});

		setDropTarget(dropTarget);

	}

	public static String getFileExtension(String fileName) {
		int lastDotIndex = fileName.lastIndexOf('.');

		if (lastDotIndex == -1 || lastDotIndex == 0 || lastDotIndex == fileName.length() - 1) {
			return "";
		} else {
			return fileName.substring(lastDotIndex + 1);
		}

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				new Widtotwl().setVisible(true);
			}

		});
	}

}
