/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.Timer;

public class DrawPanel extends JPanel implements KeyListener, ActionListener {

    private int mNumeroDeLados = 7;
    private Color mColor = Color.BLUE;
    private Color mBackColor = Color.WHITE;
    private boolean mHasCloseLines
            = false; // dibuja las lineas que cierran el poligono
    private boolean mHasCentricLines
            = true; // dibuja las lineas hacia el centro
    private double offset = 0.0d;

    private long paintTime = 0;

    private Timer t = new Timer(100, new PaintListener()); // paint cada 100 ms
    private Timer t1 = new Timer(100, this); // timer que cambia el angulo de la figura

    BufferedImage backBuffer = null;

    private int anchoAnterior = 0;
    private int altoAnterior = 0;

    private boolean mNeedRedraw = false;

    public DrawPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        t.setRepeats(true);
        t.start();
        t1.setRepeats(true);
        t1.start();
        this.setIgnoreRepaint(true);
    }

    public void createBackBuffer() {
        backBuffer
                = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
        //System.out.println("Back Buffer Creaded " + this.getWidth() + " " + this.getHeight());
    }

    public void paintBackBuffer() {

        Graphics2D g2 = (Graphics2D) backBuffer.getGraphics();

        g2.setBackground(mBackColor);
        g2.clearRect(0, 0, backBuffer.getWidth(), backBuffer.getHeight());

        int xc = (int) (this.getWidth() / 2);
        int yc = (int) (this.getHeight() / 2);

        double radianes = 0.0d;
        double grados = 0.0d;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2.setColor(mColor);
        int x = 0;
        int y = 0;
        int radio = 100;
        double paso = 360.0d / mNumeroDeLados;

        LinkedList<Point2D> puntos = new LinkedList<Point2D>();
        Point2D punto;

        while (grados < 360) {
            radianes = grados * Math.PI / 180;
            radianes += offset;
            x = (int) (Math.cos(radianes) * radio) + xc;
            y = (int) (Math.sin(radianes) * radio) + yc;
            punto = new Point2D.Float(x, y);
            puntos.add(punto);
            grados += paso;
            if (mHasCentricLines) {
                g2.drawLine(xc, yc, x, y);
            } else if (!mHasCloseLines) {
                g2.drawOval(x, y, 1, 1);
            }

        }
        if (mHasCloseLines) {
            GeneralPath p = new GeneralPath();
            if (puntos.size() > 0) {
                punto = puntos.get(0);
                p.moveTo((float) punto.getX(), (float) punto.getY());
                puntos.remove(punto);
                for (Point2D tmpPunto : puntos) {
                    p.lineTo((float) tmpPunto.getX(), (float) tmpPunto.getY());
                }
                p.closePath();
            }
            g2.draw(p);
        }
        mNeedRedraw = false;
    }

    private long paintInit = 0;
    private long paintEnd = 0;

    public void doPaint() {
        paintInit = System.nanoTime();
        try {
            if (anchoAnterior != this.getWidth()
                    || altoAnterior != this.getHeight()) {
                createBackBuffer();
                anchoAnterior = this.getWidth();
                altoAnterior = this.getHeight();
                needRedraw();
            }

            if (mNeedRedraw) {
                paintBackBuffer();
            }

            Graphics2D g2 = (Graphics2D) this.getGraphics();
            g2.drawImage(backBuffer, 0, 0, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        }
        paintEnd = System.nanoTime();
        paintTime = (paintEnd - paintInit) / 1000000;

        int Delay = (int) (33 - paintTime);
        if (Delay <= 0) {
            Delay = 5;
        }
        t.setDelay(Delay);
       // System.out.println("Retardo " + Delay);
        //System.out.println("Paint Time " + paintTime);
    }

    private void needRedraw() {
        mNeedRedraw = true;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_D: {
                mHasCentricLines = !mHasCentricLines;
                needRedraw();
                break;
            }
            case KeyEvent.VK_L: {
                mHasCloseLines = !mHasCloseLines;
                needRedraw();
                break;
            }
            case KeyEvent.VK_C: {
                doColor();
                break;
            }
            case KeyEvent.VK_UP: {
                mNumeroDeLados++;
                needRedraw();
                break;
            }
            case KeyEvent.VK_DOWN: {
                if (mNumeroDeLados > 2) {
                    mNumeroDeLados--;
                    needRedraw();
                }
                break;
            }
        }
    }

    private void doColor() {
        int r = 0;
        int g = 0;
        int b = 0;
        r = (int) (Math.random() * 255);
        g = (int) (Math.random() * 255);
        b = (int) (Math.random() * 255);
        mColor = new Color(r, g, b);
        needRedraw();
    }

    public void keyReleased(KeyEvent e) {
    }

    // este metodo se llama con el timer para cambiar el angulo de la figura
    public void actionPerformed(ActionEvent e) {
        offset += 0.07d;
        needRedraw();
    }

    class PaintListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            doPaint();
        }
    }

}
