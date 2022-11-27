package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

/**
 * Controle4Directions permet d'appliquer une direction (connexion avec le clavier) à un ensemble d'entités dynamiques
 */
public class Colonne extends RealisateurDeDeplacement {
    private Direction directionCourante;
    // Design pattern singleton
    private static Colonne c3d;

    public static Colonne getInstance() {
        if (c3d == null) {
            c3d = new Colonne();
        }
        return c3d;
    }

    public void setDirectionCourante(Direction _directionCourante) {
        directionCourante = _directionCourante;
    }

    public boolean realiserDeplacement() {
        boolean ret = false;
        for (EntiteDynamique e : lstEntitesDynamiques) {
            if (directionCourante != null)
                switch (directionCourante) {
                    case haut:
                    case bas:
                        if(e.avancerDirectionChoisie(directionCourante))
                            ret = true;
                        break;
                }
        }

        return ret;

    }

    public void resetDirection() {
        directionCourante = null;
    }
}
