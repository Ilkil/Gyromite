/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.util.Random;
import modele.deplacements.*;

import javax.naming.ldap.Control;
import javax.security.auth.x500.X500Principal;

//import apple.laf.JRSUIConstants.Size;

import java.awt.Point;
import java.util.HashMap;

/** Actuellement, cette classe gère les postions
 * (ajouter conditions de victoire, chargement du plateau, etc.)
 */
public class Jeu {

    public static final int SIZE_X = 40;
    public static final int SIZE_Y = 20;

    // compteur de déplacements horizontal et vertical (1 max par défaut, à chaque pas de temps)
    private HashMap<Entite, Integer> cmptDeplH = new HashMap<Entite, Integer>();
    private HashMap<Entite, Integer> cmptDeplV = new HashMap<Entite, Integer>();

    private Heros hector;
    private Bot bot;
    private Colonne col;

    private HashMap<Entite, Point> map = new  HashMap<Entite, Point>(); // permet de récupérer la position d'une entité à partir de sa référence
    private Entite[][] grilleEntites = new Entite[SIZE_X][SIZE_Y]; // permet de récupérer une entité à partir de ses coordonnées

    private Ordonnanceur ordonnanceur = new Ordonnanceur(this);

    public Jeu() {
        initialisationDesEntites();
    }

    public void resetCmptDepl() {
        cmptDeplH.clear();
        cmptDeplV.clear();
    }

    public void start(long _pause) {
        ordonnanceur.start(_pause);
    }
    
    public Entite[][] getGrille() {
        return grilleEntites;
    }
    
    public Heros getHector() {
        return hector;
    }

    /*public boolean est_libre( , int x, int y)
    {
        if ((e.peutServirDeSupport()==true)) ;  
    } 

     public boolean est_vide(int x, int y)
    {
        if ( tabJLabel[x][y].getIcone()==icoVide)
    }*/


    

    private void initialisationDesEntites() {
        //Création Entités Dynamiques
        bot = new Bot(this);
        hector = new Heros(this);
        col = new Colonne(this);
        addEntite(hector,1, 17); // sur un pillier 
        addEntite(bot, 3, 5);
        addEntite(col, 6,2);

            /*Random rand = new Random();
            for (int x=1; x<19; x=x+2) {
                int myrand = rand.nextInt(39);
                addEntite(new Bot(this),myrand , x);
                Entite monBot = objetALaPosition(myrand,x);
                Entite eBas = regarderDansLaDirection(monBot,Direction.bas);
                if(eBas.peutServirDeSupport()) { //on peut pas regarder si ca peut servir de support??
                    System.out.println(eBas);
                    System.out.println(myrand);
                    removeEntite(monBot);
                }
            }*/

        //Mouvements des Entités
        IA ia = new IA();   
        ia.addEntiteDynamique(bot);
        ordonnanceur.add(ia);

        Gravite g = new Gravite();
        g.addEntiteDynamique(hector);
        ordonnanceur.add(g);

        Controle4Directions.getInstance().addEntiteDynamique(hector);
        ordonnanceur.add(Controle4Directions.getInstance());

        modele.deplacements.Colonne.getInstance().addEntiteDynamique(col);
        ordonnanceur.add(modele.deplacements.Colonne.getInstance());

        //Création Entités Statiques
        // murs extérieurs horizontaux
        for (int x = 0; x < 40; x++) {
            addEntite(new Mur(this), x, 0);
            addEntite(new Mur(this), x, 19);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 19; y++) {
            addEntite(new Mur(this), 0, y);
            addEntite(new Mur(this), 39, y);
        }

        // ajout niveau de sol 
        for (int x = 10; x< 29; x++){
            addEntite(new Sol(this), x, 2);
        }
        for (int x = 3; x < 8; x++){
            addEntite(new Sol(this),x, 4);
        }
        for (int x = 7; x< 14; x++){
            addEntite(new Sol(this), x, 6);
        }
        for (int x = 25; x< 38; x++){
            addEntite(new Sol(this), x, 8);
        }
        for (int x = 1; x< 4; x++){
            addEntite(new Sol(this), x, 10);
        }
        for (int x = 25; x< 38; x++){
            addEntite(new Sol(this), x, 12);
        }
        for (int x = 14; x< 24; x++){
            addEntite(new Sol(this), x, 14);
        }
        for (int x = 1 ; x< 8; x++){
             addEntite(new Sol(this), x, 16);
        }
        for (int x = 1; x<10; x++){
            addEntite(new Sol(this), x, 18);
        }

        // ajout des cordes 
        for (int y = 7 ; y < 18 ; y++) {
            addEntite(new Corde(this), 9, y);
        }

        for (int y = 3 ; y < 14; y ++ ) {
            addEntite(new Corde(this), 17 , y);
        }

        /*for (int x=0; x<6; x++ )
        {   //do
            addEntite(new Bombe(this), rand.nextInt(38)+1, rand.nextInt(18)+1); // bombe soit dans le jeu
            // while( Bombe.est_libre==false ) //tant qui'il y a rien sur la case
        }*/

    }

    private void addEntite(Entite e, int x, int y) {
        grilleEntites[x][y] = e;
        map.put(e, new Point(x, y));
    }

    private void removeEntite(Entite e) {
        //map.get(e)
        //map.put(null, new Point(/*Pos de l'entité)*/
        Point p = map.get(e);
        grilleEntites[p.x][p.y] = null;
        map.put(null, p);
    }

    private void randomAdd(Entite e) {

    }
    
    /** Permet par exemple a une entité  de percevoir sont environnement proche et de définir sa stratégie de déplacement
     *
     */
    public Entite regarderDansLaDirection(Entite e, Direction d) {
        Point positionEntite = map.get(e);
        return objetALaPosition(calculerPointCible(positionEntite, d));
    }
    
    /** Si le déplacement de l'entité est autorisé (pas de mur ou autre entité), il est réalisé
     * Sinon, rien n'est fait.
     */
    public boolean deplacerEntite(Entite e, Direction d) {
        boolean retour = false;
        
        Point pCourant = map.get(e);
        
        Point pCible = calculerPointCible(pCourant, d);
        
        if (contenuDansGrille(pCible) && objetALaPosition(pCible) == null) { // a adapter (collisions murs, etc.)
            // compter le déplacement : 1 deplacement horizontal et vertical max par pas de temps par entité
            switch (d) {
                case bas:
                case haut:
                    if (cmptDeplV.get(e) == null) {
                        cmptDeplV.put(e, 1);

                        retour = true;
                    }
                    break;
                case gauche:
                case droite:
                    if (cmptDeplH.get(e) == null) {
                        cmptDeplH.put(e, 1);
                        retour = true;

                    }
                    break;
            }
        }

        if (retour) {
            deplacerEntite(pCourant, pCible, e);
        }

        return retour;
    }
    
    
    private Point calculerPointCible(Point pCourant, Direction d) {
        Point pCible = null;
        
        switch(d) {
            case haut: pCible = new Point(pCourant.x, pCourant.y - 1); break;
            case bas : pCible = new Point(pCourant.x, pCourant.y + 1); break;
            case gauche : pCible = new Point(pCourant.x - 1, pCourant.y); break;
            case droite : pCible = new Point(pCourant.x + 1, pCourant.y); break;
            case dbd : pCible = new Point(pCourant.x + 1, pCourant.y + 1); break;
            case dbg : pCible = new Point(pCourant.x - 1, pCourant.y + 1); break;
            
        }
        
        return pCible;
    }
    
    private void deplacerEntite(Point pCourant, Point pCible, Entite e) {
        grilleEntites[pCourant.x][pCourant.y] = null;
        grilleEntites[pCible.x][pCible.y] = e;
        map.put(e, pCible);
    }
    
    /** Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private boolean contenuDansGrille(int x, int y) {
        return x >= 0 && x < SIZE_X && y >= 0 && y < SIZE_Y;
    }

    private Entite objetALaPosition(Point p) {
        Entite retour = null;
        
        if (contenuDansGrille(p)) {
            retour = grilleEntites[p.x][p.y];
        }
        
        return retour;
    }

    private Entite objetALaPosition(int x, int y) {
        Entite retour = null;

        if (contenuDansGrille(x,y)) {
            retour = grilleEntites[x][y];
        }

        return retour;
    }

    public Ordonnanceur getOrdonnanceur() {
        return ordonnanceur;
    }
}
