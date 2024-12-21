package dette.boutique.views;

import java.util.List;

import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Dette;

public class DetteView extends ViewImpl<Dette> {

    public int saisieVersement(int montantPrecedent) {
        boolean saisieValide = false;
        int montant = 0;

        System.out.println("Veuillez saisir le montant");
        while (!saisieValide) {
            montant = saisieEntierPositif();
            if ((montant) <= montantPrecedent) {
                saisieValide = true;
            } else{
                System.out.println("Montant saisi supérieur au montant restant : il reste uniquement "+ montantPrecedent + " à payer.");
            }
        }
        return montant;
    }

    public Dette choisirDette(List<Dette> dettes) {
        if (dettes == null || dettes.isEmpty()) {
            System.out.println("Aucune dette disponible.");
            return null;
        } else {
            afficherList(dettes);
            System.out.println("Veuillez saisir l'index de la dette que vous voulez choisir.");
            int choix = obtenirChoixUtilisateur(1, dettes.size());
            return dettes.get(choix -1);
        }
    }
}