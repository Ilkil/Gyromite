package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class Gravite extends RealisateurDeDeplacement {
    @Override
    public boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            if (eBas == null || (eBas != null && !eBas.peutServirDeSupport())) {
                if (e.avancerDirectionChoisie(Direction.bas))
                    ret = true;
            }
        }

        /*for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
            if (eHaut == null || (eHaut != null && eHaut.peutServirDeSupport())) {
                if (e.avancerDirectionChoisie(Direction.haut))
                    ret = true;
            }
        }

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eBas = e.regarderDansLaDirection(Direction.bas);
            if (eBas == null || (eBas != null && eBas.peutPermettreDeMonterDescendre())) {
                if (e.avancerDirectionChoisie(Direction.bas))
                    ret = true;
            }
        }

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eHaut = e.regarderDansLaDirection(Direction.haut);
            if (eHaut == null || (eHaut != null && eHaut.peutPermettreDeMonterDescendre())) {
                if (e.avancerDirectionChoisie(Direction.haut))
                    ret = true;
            }
        }*/

       



        return ret;
    }
}
