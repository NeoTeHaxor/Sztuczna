import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;

public class Swiat extends JFrame
{

    //początkowe pozycje śmieciarki - prosta animacja
    int xSmieciarka=0;
    int ySmieciarka=75;


    //tablica przechowujace informacje o danej kratke
    int[][] world = new int[60][50];


    // zmienna sprawdzająca kierunek ruchu śmieciarki animacji początkowej
    boolean goLeft = false;


    // zmienne .png
    private BufferedImage smieciarka_prawo;
    private BufferedImage smieciarka_lewo;
    private BufferedImage smieciarka_dol;
    private BufferedImage smieciarka_gora;
    private BufferedImage wysypisko;
    private BufferedImage arrow;

    private BufferedImage plastik;
    private BufferedImage szklo;
    private BufferedImage aluminium;
    private BufferedImage papier;

    private BufferedImage drzewo_1;
    private BufferedImage drzewo_2;
    static private int ILOSC_RZECZY = 60;




    public Swiat() throws InterruptedException {
        super("Sztuczna Inteligencja - projekt");


        try {


            smieciarka_prawo = ImageIO.read(new File("resources/garbage_right.png"));
            smieciarka_lewo = ImageIO.read(new File("resources/garbage_left.png"));
            smieciarka_dol = ImageIO.read(new File("resources/garbage_down.png"));
            smieciarka_gora = ImageIO.read(new File("resources/garbage_up.png"));
            wysypisko = ImageIO.read(new File("resources/landfill.png"));
            arrow = ImageIO.read(new File("resources/arrow.png"));
            plastik = ImageIO.read(new File("resources/plastik.png"));
            szklo = ImageIO.read(new File("resources/szklo.png"));
            aluminium = ImageIO.read(new File("resources/aluminium.png"));
            papier = ImageIO.read(new File("resources/papier.png"));
            drzewo_1 = ImageIO.read(new File("resources/tree_1.png"));
            drzewo_2 = ImageIO.read(new File("resources/tree_2.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }


        setContentPane(new Init());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 1000);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        setVisible(true);
        setResizable(false);


        gerenowanieSwiata();

        // pętla gry
        while(true)
        {
            prawo();
        }
    }


    public void gerenowanieSwiata()
    {
        for (int i=0; i<60; i++)
        {
            for (int j=0; j<50; j++)
            {
                world[i][j] = 0;
            }
        }


        for(int i=0; i<ILOSC_RZECZY; i++)
        {
            int randomX = ThreadLocalRandom.current().nextInt(0, 59 + 1);
            int randomY = ThreadLocalRandom.current().nextInt(0, 49 + 1);
            int random = ThreadLocalRandom.current().nextInt(0, 6 + 1);
            world[randomX][randomY] = random;
        }
    }

    public void prawo() throws InterruptedException {
        for (int i = 0; i < 950; i = i + 10) {
            xSmieciarka = xSmieciarka + 10;
            System.out.println(xSmieciarka);
            Thread.sleep(200);
            repaint();
            if (i >= 940)
            {
                goLeft=true;
                lewo();
                repaint();
            }
        }
    }


    public void lewo() throws InterruptedException {
        for (int i = 950; i > 50; i = i - 10) {
            xSmieciarka = xSmieciarka - 10;
            System.out.println(xSmieciarka);
            Thread.sleep(200);
            repaint();
        }
        goLeft=false;
    }


     class Init extends JPanel
     {
        Droga droga = new Droga();


        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);



            g.setColor(Color.WHITE);
            g.fillRect( 0,0,1200, 1000);

            droga.draw((Graphics2D) g);



            g.setColor(Color.BLACK);

            g.setFont(new Font("Courier New", Font.BOLD, 22));
            g.drawString("WYSYPISKO", 1080, 50);
            g.drawImage(arrow, 1050, 85, this);
            g.drawImage(wysypisko, 1100, 50, this);




          //  g.fillRect(i*20,j*20,20,20);
         //**** generowanie śmieci oraz drzew ****//
            /* poczatek */

            for (int i=0; i<60; i++)
            {
                for (int j=0; j<50; j++)
                {

                    if (world[i][j] == 1 )
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(plastik, i*20, j*20, this);
                        g.setColor(Color.BLACK);

                    }
                    else if (world[i][j] == 2)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(aluminium, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                    }
                    else if (world[i][j] == 3)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(szklo, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                    }
                    else if (world[i][j] == 4)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(papier, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                    }
                    else if (world[i][j] == 5)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(drzewo_1, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                    }
                    else if (world[i][j] == 6)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(drzewo_2, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                    }


                }
            }

            /* koniec */


            /* prosty ruch śmieciarki */
            /* początek */
            g.setColor(Color.LIGHT_GRAY);
            if (!goLeft) {
                g.drawImage(smieciarka_prawo, xSmieciarka, ySmieciarka, this);
                if (xSmieciarka % 20 == 0) {
                    g.fillRect(xSmieciarka-40, ySmieciarka+5, 20, 20);
                    g.fillRect(xSmieciarka-60, ySmieciarka+5, 20, 20);
                }
            }
            else
            {
                g.drawImage(smieciarka_lewo, xSmieciarka, ySmieciarka, this);
                if (xSmieciarka % 20 == 0) {
                    g.fillRect(xSmieciarka+60, ySmieciarka+5, 20, 20);
                    g.fillRect(xSmieciarka+80, ySmieciarka+5, 20, 20);
                }
            }
            /* koniec */


        }
    }

    public static void main(String args[]) throws InterruptedException {
        new Swiat();
    }
}