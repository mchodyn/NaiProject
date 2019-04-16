import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class CameraPanel extends JPanel implements Runnable, ActionListener {
    BufferedImage image;
    VideoCapture videoCapture;
    CascadeClassifier faceDetector;
    MatOfRect faceDetections;
    Image face;

    public CameraPanel() {
        faceDetector = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
        faceDetections = new MatOfRect();
        try {
            face = ImageIO.read(CameraPanel.class.getResource("face.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void actionPerformed(ActionEvent e) {

    }

    public void run() {
        videoCapture = new VideoCapture(0);
        Mat webcamImage = new Mat();
        if (videoCapture.isOpened()) {
            while (true) {
                videoCapture.read(webcamImage);
                if (!webcamImage.empty()) {
                    JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                    topFrame.setSize(webcamImage.width() + 40, webcamImage.height() + 110);
                    matToBufferedImage(webcamImage);
                    faceDetector.detectMultiScale(webcamImage, faceDetections);
                    repaint();
                }
            }
        }

    }

    private void matToBufferedImage(Mat webcamImage) {
        int width = webcamImage.width();
        int height = webcamImage.height();
        int channels = webcamImage.channels();
        byte[] source = new byte[width * height * channels];
        webcamImage.get(0, 0, source);
        image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        final byte[] targer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(source, 0, targer, 0, source.length);


    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (image == null)
            return;

        graphics.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);
        graphics.setColor(Color.GREEN);
        for (Rect rect : faceDetections.toArray()) {
            graphics.drawRect(rect.x + 10, rect.y + 40, rect.width, rect.height);
            graphics.drawImage(face,rect.x+10,rect.y+40,rect.width,rect.height,Color.GREEN,null);
        }

    }
}

