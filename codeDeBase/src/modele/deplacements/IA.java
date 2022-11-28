package modele.deplacements;

import modele.plateau.Entite;
import modele.plateau.EntiteDynamique;

public class IA extends RealisateurDeDeplacement {
    protected boolean realiserDeplacement() {
        boolean ret = false;

        for (EntiteDynamique e : lstEntitesDynamiques) {
            Entite eDroite = e.regarderDansLaDirection(Direction.droite);
            Entite eGauche = e.regarderDansLaDirection(Direction.gauche);
            if(e.left){
                if(e.regarderDansLaDirection(Direction.dbg) == null){
                    e.left = false;
                    ret = true;
                    return ret;
                }
                else if(eGauche != null && eDroite == null && e.regarderDansLaDirection(Direction.dbd) != null){
                    e.left = false;
                    e.avancerDirectionChoisie(Direction.droite);
                    ret = true;
                }
                else e.avancerDirectionChoisie(Direction.gauche);
            }
            else{
                if(e.regarderDansLaDirection(Direction.dbd) == null){
                    e.left = true;
                    ret = true;
                    return ret;
                }
                else if(eDroite != null && eGauche == null && e.regarderDansLaDirection(Direction.dbg) != null){
                    e.left = true;
                    e.avancerDirectionChoisie(Direction.gauche);
                    ret = true;
                }
                else e.avancerDirectionChoisie(Direction.droite);
            }
        }
        return ret;
    }

}
