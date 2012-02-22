package uk.ac.ed.inf.sdp2012.group7.vision;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;

import uk.ac.ed.inf.sdp2012.group7.vision.ui.ControlGUI;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.VideoDevice;

public class SimulatorFeed extends WindowAdapter {
	private VideoDevice videoDev;
	private JLabel label;
	private JFrame windowFrame;
	private JLabel labelThresh;
	private JFrame windowFrameThresh;
	private FrameGrabber frameGrabber;
	private int width, height;
	private BufferedImage frameImage;
	public boolean paused = false;
	int count = 0;
	
	private String simHost = "localhost";
	private int    simPort = 10002;

	private Socket       socket;
	private OutputStream os;
	private InputStream  is;
	
	/**
	 * Default constructor.
	 *
	 * @param thresholdsGUI
	 *
	 */
	public SimulatorFeed(ControlGUI thresholdsGUI) {

		/* Initialise the GUI that displays the video feed. */
		initGUI();
		initFrameGenerator();
		Simulator.logger.info("SimulatorFeed Initialised");
		System.out.println("Please select what colour we are using the GUI.");

		/* TODO Let them set who's "us" and "opponent" */
		Simulator.logger.info("Simulator System Calibrated");
		Simulator.worldState.setClickingDone(true);
	}


	public BufferedImage getFrameImage(){
		return this.frameImage;
	}

	private Thread receiver = new Thread() {
		
		public void run() {
			/*try {
				socket = new Socket(simHost, simPort);
				os = socket.getOutputStream();
				os.flush();
				is = socket.getInputStream();
			} catch (Exception e) {
				Simulator.logger.fatal("Connecting to simulator failed: "+e.toString());
			}*/
			
			/*while(true) {
				int[] buf = new int[8];
				
				for (int i = 0; i < 8; ++i) {
					int recv = 0;
					for (int j = 0; j < 8; ++j) {
						recv = recv << 8;
						try {
							recv = recv | is.read();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							Simulator.logger.fatal("Receiving from simulator failed: "+e.toString());
						}
					}
					buf[i] = recv;
				}*/

				/*Simulator.worldState.setBlueRobotPosition(buf[0], buf[1]);
				//Simulator.worldState.setBlueRobotAngle(buf[2]);
				Simulator.worldState.setYellowRobotPosition(buf[3], buf[4]);
				//Simulator.worldState.setYellowRobotAngle(buf[5]);
				Simulator.worldState.setBallPosition(buf[6], buf[7]);*/
				
				try {
					frameImage = ImageIO.read(new File("testData/.background.png"));
				} catch (IOException e) {
					Simulator.logger.fatal("Failed to load backgroundImage");
				}
				
				//frameImage.getGraphics().setColor(Color.blue);
				
				
			//}
			
		}
		
		

	};
	
	private void initFrameGenerator() {
		
		receiver.run();
		
	}

	/**
	 * Creates the graphical interface components and initialises them
	 */
	private void initGUI() {
		windowFrame = new JFrame("Vision Window");
		label = new JLabel();
		windowFrame.getContentPane().add(label);
		windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		windowFrame.addWindowListener(this);
		windowFrame.setVisible(true);
		windowFrame.setSize(width+5, height+25);

		windowFrameThresh = new JFrame("Vision Window Threshed");
        labelThresh = new JLabel();
        windowFrameThresh.getContentPane().add(labelThresh);
        windowFrameThresh.addWindowListener(this);
        windowFrameThresh.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        windowFrameThresh.setVisible(true);
        windowFrameThresh.setSize(width+5, height+25);  
	}

	//useless, had to be included because of the MouseEvent interface


	//can output the buffered image to disk, can normalise if necessary
	public static void writeImage(BufferedImage image, String fn){
		try {
			File outputFile = new File(fn);
			ImageIO.write(image, "png", outputFile);
		} catch (Exception e) {
			Vision.logger.error("Failed to write image: " + e.getMessage());
		}
	}

	/**
	 * Catches the window closing event, so that we can free up resources
	 * before exiting.
	 *
	 * @param e         The window closing event.
	 */
	public void windowClosing(WindowEvent e) {
		/* Dispose of the various swing and v4l4j components. */
		frameGrabber.stopCapture();
		videoDev.releaseFrameGrabber();

		windowFrame.dispose();
		Vision.logger.info("Vision System Ending...");
		System.exit(0);
	}
}
