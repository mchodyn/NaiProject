import org.opencv.videoio.VideoCapture;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CameraFrame extends JFrame implements ActionListener {
    CameraPanel cameraPanel;
    public CameraFrame() {
        VideoCapture videoCapture = new VideoCapture(0);
        cameraPanel = new CameraPanel();
        Thread thread = new Thread(cameraPanel);
        thread.start();
        add(cameraPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

    }

}
