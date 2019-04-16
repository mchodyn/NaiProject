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
    private BufferedImage image;
    private VideoCapture videoCapture;
    private CascadeClassifier faceDetector;
    private MatOfRect faceDetections;
    private Boolean isMusicPlaying = false;
    private PreviousPositions previousPositions;
    private int range = 3;
    Image face;

    CameraPanel() {
        setOpaque(false);
        previousPositions = new PreviousPositions();
        faceDetector = new CascadeClassifier(CameraPanel.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1));
        faceDetections = new MatOfRect();
        try {
            face = ImageIO.read(CameraPanel.class.getResource("face.png"));
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
        final byte[] target = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(source, 0, target, 0, source.length);


    }

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (image == null) {
            return;
        } else {
            checkMusic();
            graphics.drawImage(image, 10, 40, image.getWidth(), image.getHeight(), null);
            for (Rect rect : faceDetections.toArray()) {
                if (previousPositions.previousX.size() == 0) {
                    graphics.drawRect(rect.x + 10, rect.y + 40, rect.width, rect.height);
                    graphics.drawImage(face, rect.x + 10, rect.y + 40, rect.width, rect.height,null);
                    updatePrevious(rect);
                }
                else{
                    System.out.println(previousPositions.getAverageX() + 10);
                    graphics.drawRect(previousPositions.getAverageX()+ 10, previousPositions.getAverageY() + 40,
                            previousPositions.getAverageWidth(), previousPositions.getAverageHeight());
                    graphics.drawImage(face, previousPositions.getAverageX() + 10, previousPositions.getAverageY() + 40,
                            previousPositions.getAverageWidth(), previousPositions.getAverageHeight(),null);
                    updatePrevious(rect);
                }
            }
        }

    }

    private void updatePrevious(Rect rect) {
        if(previousPositions.previousX.size()<range) {
            previousPositions.previousX.add(rect.x);
            previousPositions.previousY.add(rect.y);
            previousPositions.previousWidth.add(rect.width);
            previousPositions.previousHeight.add(rect.height);
        }
        else {
            previousPositions.previousX.remove(0);
            previousPositions.previousY.remove(0);
            previousPositions.previousWidth.remove(0);
            previousPositions.previousHeight.remove(0);
            previousPositions.previousX.add(range-1,rect.x);
            previousPositions.previousY.add(range-1,rect.y);
            previousPositions.previousWidth.add(range-1,rect.width);
            previousPositions.previousHeight.add(range-1,rect.height);

        }
    }

    private void checkMusic() {
        if (!isMusicPlaying && faceDetections.toArray().length > 0) {
            isMusicPlaying = true;
           // System.out.println("start playing music");
        } else if (isMusicPlaying && faceDetections.toArray().length < 1) {
            isMusicPlaying = false;
            previousPositions = new PreviousPositions();
            //System.out.println("stop playing music");
        } else if (isMusicPlaying) {
           // System.out.println("music is playing");
        }


    }
}

