/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class FrMain extends JFrame {
    private DrawPanel drawPanel1 = new DrawPanel();
    private BorderLayout borderLayout1 = new BorderLayout();

    private GraphicsDevice gd;
    private Graphics gScr;
    private BufferStrategy bufferStrategy;

    private void initFullScreen() {
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();

        setUndecorated(true); // no menu bar, borders, etc.
        setIgnoreRepaint(true);
        // turn off paint events since doing active rendering
        setResizable(false);

        if (!gd.isFullScreenSupported()) {
            System.out.println("Full-screen exclusive mode not supported");
            System.exit(0);
        }


        gd.setFullScreenWindow(this); // switch on FSEM


    } // end of initFullScreen( )

    public FrMain() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.getContentPane().setLayout(borderLayout1);
        this.setSize(new Dimension(400, 300));
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        drawPanel1.setBackground(Color.white);
        drawPanel1.setLayout(null);
        this.getContentPane().add(drawPanel1, BorderLayout.CENTER);
        this.addKeyListener(drawPanel1);

        /* habilitar esta linea para fullscreen  */
        initFullScreen();
    }
}
