import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

public class Swiat extends JFrame
{

    //początkowe pozycje śmieciarki - prosta animacja
    int xSmieciarka=0;
    int ySmieciarka=75;


    //tablica przechowujace informacje o danej kratke
    int[][] world = new int[60][40];
    ArrayList<Integer> zablokowaneX = new ArrayList<>();
    ArrayList<Integer> zablokowaneY = new ArrayList<>();


    ArrayList<Integer> zablokowaneXdrzewa = new ArrayList<>();
    ArrayList<Integer> zablokowaneYdrzewa = new ArrayList<>();

    ArrayList<Integer> przebyteX = new ArrayList<>();
    ArrayList<Integer> przebyteY = new ArrayList<>();

    List<Integer> xSorted;
    List<Integer> ySorted;






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
    static private int ILOSC_RZECZY = 500;




    Pozycje poz = new Pozycje();
    Node initialNode;
    //= new Node(0, 39);
    Node finalNode;
    //= new Node(51, 5);
    int rows = 60;
    int cols = 40;
    List<Node> path;
    Node node;

    AStar aStar;


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






        Thread.sleep(1500);
        idz(0, 39);






        // pętla gry
      /*  while(true)
        {
            prawo();
        } */
    }

    public void idz(int x, int y) throws InterruptedException {


        ArrayList<Integer> list = new ArrayList<>(zwrocNajblizszaPrzeszkode(x,y));
        initialNode = new Node(x, y);

        finalNode = new Node(list.get(0), list.get(1));
        aStar = new AStar(rows, cols, initialNode, finalNode);

        aStar.setBlocks(zablokowaneXdrzewa, zablokowaneYdrzewa);
       // aStar.setBlocks(zablokowaneX, zablokowaneY);

        path = aStar.findPath();

        for (int i=0; i<path.size(); i++)
        {

            node = path.get(i);
            Thread.sleep(100);
            repaint();

            System.out.println("X na: " + node.getRow() + " Y na: " + node.getCol());
        }



        zablokowaneX.remove(new Integer(node.getRow()));
        zablokowaneY.remove(new Integer(node.getCol()));

        poz.setZablokowaneX(zablokowaneX);
        poz.setZablokowaneY(zablokowaneY);

        idz(node.getRow(), node.getCol());

    }


    public void gerenowanieSwiata() {

        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 40; j++) {
                world[i][j] = 0;
            }
        }




        ArrayList<Integer> x = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();


        for (int i = 0; i < ILOSC_RZECZY; i++) {


            int randomX = ThreadLocalRandom.current().nextInt(0, 59 + 1);
            int randomY = ThreadLocalRandom.current().nextInt(0, 39 + 1);

            if (!x.contains(randomX) && !y.contains(randomY)) {
                x.add(randomX);
                y.add(randomY);

                int random = ThreadLocalRandom.current().nextInt(0, 6 + 1);
                world[randomX][randomY] = random;
                System.out.println("X: " + randomX + " Y: " + randomY + " -" + " RAN: " + world[randomX][randomY]);
                if (world[randomX][randomY] != 0) {
                    if (world[randomX][randomY] == 5 || world[randomX][randomY] == 6) {
                        System.out.println("DRZEWO");
                        zablokowaneXdrzewa.add(randomX);
                        zablokowaneYdrzewa.add(randomY);
                    } else {
                        System.out.println("BLOK");
                        zablokowaneX.add(randomX);
                        zablokowaneY.add(randomY);
                    }

                }

            } else {
                continue;
            }

        }




    /*    for (int i=0; i<100; i++)
        {
            int randomX = ThreadLocalRandom.current().nextInt(0, 59 + 1);
            int randomY = ThreadLocalRandom.current().nextInt(0, 39 + 1);
            int random = ThreadLocalRandom.current().nextInt(5, 6 + 1);
            world[randomX][randomY] = random;
            zablokowaneXdrzewa.add(randomX);
            zablokowaneYdrzewa.add(randomY);
        } */


        poz.setZablokowaneX(zablokowaneX);
        poz.setZablokowaneY(zablokowaneY);


        for (int i = 0; i < 60; i++) {
            for (int j = 0; j < 40; j++) {
                System.out.println(i + " " + " " + j + " " + world[i][j]);
            }
        }
    }
  /*  public void prawo() throws InterruptedException {
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
    } */



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



            g.setColor(Color.BLUE);
            for (int i=0; i<przebyteX.size(); i++)
            {
                g.fillRect(przebyteX.get(i)*20,przebyteY.get(i)*20,20,20);
            }




         //**** generowanie śmieci oraz drzew ****//
            /* poczatek */

            for (int i=0; i<60; i++)
            {
                for (int j=0; j<40; j++)
                {






                    if (world[i][j] == 1 )
                    {


                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(plastik, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));

                    }
                    else if (world[i][j] == 2)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(aluminium, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 3)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(szklo, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 4)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(papier, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 5)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(drzewo_1, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }
                    else if (world[i][j] == 6)
                    {
                        g.setColor(Color.LIGHT_GRAY);
                        g.fillRect(i*20,j*20,20,20);
                        g.drawImage(drzewo_2, i*20, j*20, this);
                        g.setColor(Color.BLACK);
                        g.setFont(new Font("Courier New", Font.BOLD, 10));
                        g.drawString((i+1) + "," + (j+1), i*20  ,j*20);
                        g.setFont(new Font("Courier New", Font.BOLD, 22));
                    }


                }
            }


                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(node.getRow()*20,node.getCol()*20,20,20);
                g.setColor(Color.BLACK);
                g.drawImage(smieciarka_prawo, node.getRow()*20, node.getCol()*20, this);
                g.setFont(new Font("Courier New", Font.BOLD, 10));
                g.drawString((node.getRow()+1) + "," + (node.getCol()+1), node.getRow()*20  ,node.getCol()*20);


            przebyteX.add(node.getRow());
            przebyteY.add(node.getCol());







            g.setColor(Color.GREEN);
            g.fillRect(0*20,39*20,20,20);
            g.setColor(Color.RED);
            g.fillRect(51*20,5*20,20,20);



            /* koniec */


            /* prosty ruch śmieciarki */
            /* początek */
       /*     g.setColor(Color.LIGHT_GRAY);
            if (!goLeft) {
                if (xSmieciarka % 20 == 0) {
                    g.fillRect(xSmieciarka+20, ySmieciarka+5, 20, 20);
                    g.fillRect(xSmieciarka+40, ySmieciarka+5, 20, 20);
                    g.fillRect(xSmieciarka, ySmieciarka+5, 20, 20);

                    g.fillRect(xSmieciarka+20, ySmieciarka+25, 20, 20);
                    g.fillRect(xSmieciarka+40, ySmieciarka+25, 20, 20);
                    g.fillRect(xSmieciarka, ySmieciarka+25, 20, 20);
                }
                g.drawImage(smieciarka_prawo, xSmieciarka, ySmieciarka, this);

            }
            else
            {
                if (xSmieciarka % 20 == 0) {
                    g.fillRect(xSmieciarka+20, ySmieciarka+5, 20, 20);
                    g.fillRect(xSmieciarka+40, ySmieciarka+5, 20, 20);
                    g.fillRect(xSmieciarka, ySmieciarka+5, 20, 20);

                    g.fillRect(xSmieciarka+20, ySmieciarka+25, 20, 20);
                    g.fillRect(xSmieciarka+40, ySmieciarka+25, 20, 20);
                    g.fillRect(xSmieciarka, ySmieciarka+25, 20, 20);
                }
                g.drawImage(smieciarka_lewo, xSmieciarka, ySmieciarka, this);

            }
            /* koniec */



        }
    }



    public ArrayList<Integer> zwrocNajblizszaPrzeszkode(int xSmieciarki, int ySmieciarki)
    {

        ArrayList<Integer> zwracanaLista = new ArrayList<>();
        zwracanaLista.clear();





        xSorted = new ArrayList<>(poz.getZablokowaneX());
        ySorted = new ArrayList<>(poz.getZablokowaneY());

        ArrayList<Integer> odleglosciElementow = new ArrayList<>();
        for (int i=0; i<zablokowaneX.size(); i++)
        {

                float odleglosc = (float) Math.sqrt(
                        Math.pow(xSmieciarki - zablokowaneX.get(i), 2) +
                                Math.pow(ySmieciarki - zablokowaneY.get(i), 2));
                odleglosciElementow.add((int) odleglosc);

        }





        int minIndex = odleglosciElementow.indexOf(Collections.min(odleglosciElementow));



        int najblizszyOdleglosc = odleglosciElementow.get(minIndex);
        int pozycjaXNajblizszy = xSorted.get(minIndex);
        int pozycjaYNajblizszy = ySorted.get(minIndex);

        System.out.println("X: " + pozycjaXNajblizszy + " | Y: " + pozycjaYNajblizszy + " | E: " + najblizszyOdleglosc);

        zwracanaLista.add(0, pozycjaXNajblizszy);
        zwracanaLista.add(1, pozycjaYNajblizszy);
        zwracanaLista.add(2, najblizszyOdleglosc);

        return zwracanaLista;
    }





    public final class Pozycje {
        public ArrayList<Integer> listX = new ArrayList<>();
        public ArrayList<Integer> listY = new ArrayList<>();

        public ArrayList<Integer> getZablokowaneX() {
            return listX;
        }

        public void setZablokowaneX(ArrayList<Integer> list) {
            this.listX = list;
        }

        public ArrayList<Integer> getZablokowaneY() {
            return listY;
        }

        public void setZablokowaneY(ArrayList<Integer> list) {
            this.listY = list;
        }


    }





    public static void main(String args[]) throws InterruptedException {
        new Swiat();
    }
}