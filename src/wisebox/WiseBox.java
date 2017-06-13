package wisebox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JFrame;

public class WiseBox implements Runnable, KeyListener {

    public static final int WIDTH = 700, HEIGHT = 700;

    public static WiseBox wisebox;

    public boolean yukariGonder, atesEt = false;

    public static int score = 0, yCoor = 0, gelenYCoor;

    public static int[] yCoorDizisi = {485, 495, 510, 525, 500, 490, 535, 540, 545, 550, 555, 560, 565, 570, 575};

    public JFrame frame;

    public Renderer renderer;

    public ArrayList<Rectangle> boxs = new ArrayList<Rectangle>();

    public ArrayList<Rectangle> insects = new ArrayList<Rectangle>();

    public ArrayList<Rectangle> mermiler = new ArrayList<Rectangle>();

    public Rectangle kutu;

    public Random random;

    public Thread thread;

    public boolean start = false;

    public WiseBox() {

        frame = new JFrame("WiseBox!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(WIDTH, HEIGHT));
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.add(renderer = new Renderer());

        kutu = new Rectangle(WIDTH - 400, HEIGHT / 2 - 140, 30, 30);
        frame.addKeyListener(this);

        start();

        frame.setVisible(true);
    }

    public void start() {

        thread = new Thread(this);

        random = new Random();

        addBox(true);
        addBox(true);
        addBox(true);
        addBox(true);

        addInsects(true);
        addInsects(true);
        addInsects(true);
        addInsects(true);

        thread.start();

    }

    public void KutuMove(boolean input) {

        if (input) {
            kutu.y -= 2;

        } else {
            kutu.y += 2;
        }
    }

    public static void main(String[] args) {
        wisebox = new WiseBox();
    }

    public void Atesle() {
        addMermi();
    }

    public void addBox(boolean start) {
        int width = 200;
        if (start) {

            boxs.add(new Rectangle(width + WIDTH + boxs.size() * 300, 600, width, 25));
        } else {
            boxs.add(new Rectangle(boxs.get(boxs.size() - 1).x + 300, 600, width, 25));
        }

    }

    public void addInsects(boolean start) {
        int width = 20;

        yCoor = random.nextInt(14);
        gelenYCoor = yCoorDizisi[yCoor];

        if (start) {
            insects.add(new Rectangle(width + 275 + WIDTH + insects.size() * 300, gelenYCoor, width, 20));
        } else {
            insects.add(new Rectangle(insects.get(insects.size() - 1).x + 300, gelenYCoor, width, 20));
        }

    }

    public void addMermi() {
        mermiler.add(new Rectangle(kutu.x, kutu.y, 15, 15));
    }

    public void drawBoxs(Graphics g, Rectangle box) {
        g.setColor(Color.green.darker());
        g.fillRoundRect(box.x, box.y, box.width, box.height, 30, 30);
    }

    public void drawInsects(Graphics g, Rectangle inseks) {
        g.setColor(Color.RED);
        g.fillRect(inseks.x, inseks.y, inseks.width, inseks.height);
    }

    public void drawMermi(Graphics g, Rectangle mermi) {
        g.setColor(Color.YELLOW);
        g.fillOval(mermi.x, mermi.y, mermi.width, mermi.height);
    }

    public void repaint(Graphics g) {

        g.setColor(Color.CYAN);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.RED);
        g.fillRect(0, 200, WIDTH, 50);

        if (start) {
            g.setColor(Color.BLACK);
            g.fillRect(kutu.x, kutu.y, kutu.width, kutu.height);

            for (Rectangle box : boxs) {
                drawBoxs(g, box);
            }

            for (Rectangle inseckt : insects) {
                drawInsects(g, inseckt);
            }

            for (Rectangle mermi : mermiler) {
                drawMermi(g, mermi);
            }

            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.BOLD, 25));
            g.drawString("Score : " + score, WIDTH - 400, 100);

        } else {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Verdana", Font.BOLD, 25));
            g.drawString("Oyuna Başlamak İçin Enter'a basın.", WIDTH - 600, 100);
            score = 0;
        }

    }

    @Override
    public void run() {
        for (;;) {

            KutuMove(yukariGonder);

            for (int i = 0; i < boxs.size(); i++) {
                Rectangle box = boxs.get(i);
                box.x -= 3;
            }

            for (int i = 0; i < insects.size(); i++) {
                Rectangle insect = insects.get(i);
                insect.x -= 3;
            }

            for (int i = 0; i < mermiler.size(); i++) {
                Rectangle mermi = mermiler.get(i);
                mermi.x += 3;
            }

            for (int i = 0; i < boxs.size(); i++) {
                Rectangle box = boxs.get(i);
                if (box.x + box.width < 0) {
                    boxs.remove(box);
                    if (box.y == 600) {
                        addBox(false);
                    }

                }
            }

            for (int j=0;j<mermiler.size();j++) {

                Rectangle mermi=mermiler.get(j);
                
                for (int i = 0; i < insects.size(); i++) {
                    Rectangle insect = insects.get(i);
                    if (insect.x + insect.width < 0) {                       
                        insects.remove(insect);
                        if (insect.y > 0) {
                            addInsects(false);
                        }
                       
                    }
                   if(insect.intersects(mermi)) {

                            mermiler.remove(mermi);
                            insects.remove(insect);
                            if (insect.y > 0) {
                            addInsects(false);
                           }  
                            
                            System.out.println("vurdum Yok Oldum");
                       
                        score++;
                       
                   }
                   else{
                       
                        if (mermi.x+mermi.width>WIDTH) {
                            mermiler.remove(mermi);
                            System.out.println("vurmadım Yok Oldum");
                       }
                       
                   }
                   
                       
                        
                    
                }

            }

            for (Rectangle box : boxs) {
                if (box.intersects(kutu)) {
                    if (kutu.x <= box.x) {
                        kutu.x = box.x - kutu.width;
                    } else if (box.y != 0) {
                        kutu.y = box.y - kutu.height;
                    } else if (kutu.y < box.height) {
                        kutu.y = box.height;
                    }
                }
            }

            if (kutu.y > HEIGHT - 130 && kutu.y < 100) {
                kutu.y = HEIGHT - 130;
            }

            if (kutu.y > 580) {
                start = false;
            }

            renderer.repaint();

            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            yukariGonder = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            start = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Atesle();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            yukariGonder = false;
        }

    }

}
