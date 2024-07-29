import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoadScreen extends JFrame {

    public LoadScreen() {
      super();
      this.setTitle("Connecting to Chat Server...");
      this.setSize(300, 200);
      this.setModal(true);
      this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
      this.setLocationRelativeTo(null); // Center the window
  
      JLabel messageLabel = new JLabel("Connecting...");
      messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
      this.add(messageLabel, BorderLayout.CENTER);
    }

    private void setModal(boolean b) {
      // TODO Auto-generated method stub
      throw new UnsupportedOperationException("Unimplemented method 'setModal'");
    }
}
  