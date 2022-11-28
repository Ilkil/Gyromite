package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class Colonne extends RealisateurDeDeplacement {
    private boolean flipflop = false;
    private static Colonne c2d;

    public static Colonne getInstance() {
        if (c2d == null) {
            c2d = new Colonne();
        }
        return c2d;
    }

    public void addFlipflop(){
        if(flipflop) flipflop = false;
        else flipflop = true;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if(flipflop){
                e.avancerDirectionChoisie(Direction.bas);
                ret = true;
            }
            else {
                e.avancerDirectionChoisie(Direction.haut);
                ret = true;
            }
        }
        return ret;
    }
    //ANCIENT MOUVEMENTS 'Z' & 'S'
        /*for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
                switch (directionCourante) {
                    case haut:
                    case bas:
                        if(e.avancerDirectionChoisie(directionCourante))
                            ret = true;
                        break;
                }
        }*/

}
