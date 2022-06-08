package view;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * An implementation of the PhotoEditorGUI interface. This implementation creates a GUI that can
 * be controlled by a controller.
 */
public class PhotoEditorGUIView extends JFrame implements PhotoEditorGUI {
  private JPanel mainPanel;
  private JComboBox<String> commands;
  private JLabel imageLabel;

  private JButton commandButton;
  private JButton saveButton;
  private JButton loadButton;

  /**
   * Creates a JFrame to display the GUI
   */
  public PhotoEditorGUIView() {
    super();
    setTitle("Photo Editor");
    setSize(400, 400);
    mainPanel = new JPanel();


    add(mainPanel, BorderLayout.CENTER);
    setLocationRelativeTo(null);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

    JPanel imagePanel = new JPanel();
    imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(imagePanel);

    this.imageLabel = new JLabel();
    JScrollPane imageScrollPane = new JScrollPane(imageLabel);

    imageLabel.setIcon(new ImageIcon(""));
    imagePanel.add(imageScrollPane);

    JPanel commandPanel = new JPanel();
    commandPanel.setBorder(BorderFactory.createTitledBorder("Select an action then press \"Perform Action\""));
    commandPanel.setLayout(new BoxLayout(commandPanel, BoxLayout.PAGE_AXIS));
    mainPanel.add(commandPanel);

    String[] options = {"brighten", "red-component", "green-component", "blue-component",
            "value-component", "intensity-component", "luma-component", "horizontal-flip",
            "vertical-flip", "sepia", "greyscale", "blur", "sharpen"};
    this.commands = new JComboBox<String>();
    commands.setActionCommand("set command");

    for (int i = 0; i < options.length; i++) {
      commands.addItem(options[i]);
    }

    commandPanel.add(commands);

    JPanel buttonPanel = new JPanel();
    mainPanel.add(buttonPanel);

    this.commandButton = new JButton("Perform Action");
    commandButton.setActionCommand("execute");
    buttonPanel.add(commandButton);

    this.saveButton = new JButton("Save");
    saveButton.setActionCommand("save");
    buttonPanel.add(saveButton);

    this.loadButton = new JButton("Load");
    loadButton.setActionCommand("load");
    buttonPanel.add(loadButton);
  }


  @Override
  public void showGUI(boolean b) {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(b);
  }

  @Override
  public void setListener(ActionListener l) {
    commands.addActionListener(l);
    commandButton.addActionListener(l);
    saveButton.addActionListener(l);
    loadButton.addActionListener(l);
  }

  @Override
  public void setPhoto(Image i) {
    this.imageLabel.setIcon(new ImageIcon(i));
    validate();
    repaint();
  }

  @Override
  public String getInput(String message) {
    return JOptionPane.showInputDialog(message);
  }

  @Override
  public void renderMessage(String message) {
    if (!message.contains("//")) {
      return;
    }

    String title = message.split("//")[0];
    String note = message.split("//", 2)[1];
    JOptionPane.showMessageDialog(PhotoEditorGUIView.this, note, title,
            JOptionPane.PLAIN_MESSAGE);
  }
}
