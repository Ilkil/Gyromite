package VueControleur;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

import modele.deplacements.Controle4Directions;
import modele.deplacements.Direction;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction Pacman, etc.))
 *
 */
public class VueControleurGyromite extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private ImageIcon icoHero;
    private ImageIcon icoBot;
    private ImageIcon icoVide;
    private ImageIcon icoMur;
    private ImageIcon icoColonne;
    private ImageIcon icoBombe;
    private ImageIcon icoBonus;
    private ImageIcon icoCorde;
    private ImageIcon icoSol;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)
    private modele.plateau.Timer timer;

    public VueControleurGyromite(Jeu _jeu) {
        sizeX = _jeu.SIZE_X;
        sizeY = _jeu.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : Controle4Directions.getInstance().setDirectionCourante(Direction.gauche); break;
                    case KeyEvent.VK_RIGHT : Controle4Directions.getInstance().setDirectionCourante(Direction.droite); break;
                    case KeyEvent.VK_DOWN : Controle4Directions.getInstance().setDirectionCourante(Direction.bas); break;
                    case KeyEvent.VK_UP : Controle4Directions.getInstance().setDirectionCourante(Direction.haut); break;

                    case KeyEvent.VK_Z: modele.deplacements.Colonne.getInstance().addFlipflop(); break;
                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero = chargerIcone("Images/player.png", 0, 0, 32, 40);  //chargerIcone("Images/Pacman.png");
        icoBot = chargerIcone("Images/smick.png", 0, 0, 35, 25); //chargerIcone("Images/Pacman.png");
        icoVide = chargerIcone("Images/Vide.png"); //chargerIcone("Images/Vide.png");
        icoColonne = chargerIcone("Images/Colonne.png", 0, 0, 18, 18); //chargerIcone("Images/Colonne.png");
        icoMur = chargerIcone("Images/Mur.png", 0, 0, 18, 18); //chargerIcone("Images/Mur.png");
        icoBombe = chargerIcone("Images/bomb.png", 0, 0, 70, 50); //chargerIcone("Images/bomb.png");
        icoBonus = chargerIcone("Images/particles.png", 0, 0, 20, 20); //chargerIcone("Images/particules.png");
        icoCorde = chargerIcone("Images/tileset.png", 16, 0, 16, 16); //chargerIcone("Images/tileset.png");
        icoSol = chargerIcone("Images/tileset.png", 0, 0, 5, 5);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Gyromite");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);

        timer = new modele.plateau.Timer();
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (jeu.getGrille()[x][y] instanceof Heros) { // si la grille du modèle contient un Pacman, on associe l'icône Pacman du côté de la vue

                    tabJLabel[x][y].setIcon(icoHero);
                    //Transparence de l'image icoHero

                    //si transparence : images avec canal alpha + dessins manuels (voir ci-dessous + créer composant qui redéfinie paint(Graphics g)), se documenter
                    //BufferedImage bi = getImage("Images/smick.png", 0, 0, 20, 20);
                    //tabJLabel[x][y].getGraphics().drawImage(bi, 0, 0, null);

                }
                else if (jeu.getGrille()[x][y] instanceof Mur) {
                    tabJLabel[x][y].setIcon(icoMur);
                }
                else if (jeu.getGrille()[x][y] instanceof Bot) {
                    tabJLabel[x][y].setIcon(icoBot);
                }
                else if (jeu.getGrille()[x][y] instanceof Colonne) {
                    tabJLabel[x][y].setIcon(icoColonne);
                }
                else if (jeu.getGrille()[x][y] instanceof Bombe) {
                    tabJLabel[x][y].setIcon(icoBombe);
                }
                else if (jeu.getGrille()[x][y] instanceof Bonus) {
                    tabJLabel[x][y].setIcon(icoBonus);
                }
                else if (jeu.getGrille()[x][y] instanceof Corde) {
                    tabJLabel[x][y].setIcon(icoCorde);
                }
                else if (jeu.getGrille()[x][y] instanceof Sol) {
                    tabJLabel[x][y].setIcon(icoSol);
                }
                else if (x==0 && y==0) {
                    String temps = this.timer.getStr();
                    tabJLabel[x][y].setText(temps);
                    tabJLabel[x][y].setForeground(Color.red);
                    tabJLabel[x][y].setBackground(Color.white);
                }
                else {
                    tabJLabel[x][y].setIcon(icoMur);
                }
				/*tabJLabel[x][y].setOpaque(true);
				tabJLabel[x][y].setBackground(new Color(0, 0, 0, 0));*/
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                });

       */
    }


    // chargement de l'image entière comme icone
    private ImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


        return new ImageIcon(image);
    }

    // chargement d'une sous partie de l'image
    private ImageIcon chargerIcone(String urlIcone, int x, int y, int w, int h) {
        // charger une sous partie de l'image à partir de ses coordonnées dans urlIcone
        BufferedImage bi = getSubImage(urlIcone, x, y, w, h);
        // adapter la taille de l'image a la taille du composant (ici : 20x20)
        int scale_x = 1280 / this.sizeX;
        int scale_y = 720 / this.sizeY;
        return new ImageIcon(bi.getScaledInstance(scale_x, scale_y, java.awt.Image.SCALE_SMOOTH));
    }

    private BufferedImage getSubImage(String urlIcone, int x, int y, int w, int h) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleurGyromite.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        BufferedImage bi = image.getSubimage(x, y, w, h);
        return bi;
    }

}
