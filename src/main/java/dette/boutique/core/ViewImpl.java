package dette.boutique.core;

import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;

public abstract class ViewImpl<T> implements View {
    @Override
    public int obtenirChoixUtilisateur(int min, int max) {
        Scanner scanner = Main.getScanner();
        while (true) {
            System.out.println("Veuillez choisir (Entre " + min + " et " + max + ") : ");
            if (scanner.hasNextInt()) {
                int choix = scanner.nextInt();
                scanner.nextLine();

                if (choix >= min && choix <= max) {
                    return choix;
                } else {
                    System.out.println("Choix invalide. Veuillez entrer un nombre entre " + min + " et " + max + ".");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez saisir un nombre entier.");
                scanner.next();
            }
        }
    }

    @Override
    public int choix(String phrase, int min, int max) {
        System.out.println(phrase);
        int choix = obtenirChoixUtilisateur(min, max);
        return choix;
    }

    public int saisieEntierPositif() {
        Scanner scanner = Main.getScanner();
        boolean valide = false;
        int entier = -1;
    
        while (!valide) {
            if (scanner.hasNextInt()) {
                entier = scanner.nextInt();
                scanner.nextLine();

                if (entier > 0) {
                    valide = true;
                } else {
                    System.out.println("Erreur : Veuillez entrer un nombre entier positif.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez saisir un nombre entier.");
                scanner.next();
            }
        }
    
        return entier;
    }
    

    public void afficherList(List<T> elements) {
        for (int i = 0; i < elements.size(); i++) {
            System.out.println((i + 1) + ". " + elements.get(i));
        }
    }

    public void menuCLient() {
        System.out.println("----------Menu Principal----------");
        System.out.println("1- Lister mes dettes");
        System.out.println("2- Faire une demande de Dette");
        System.out.println("3- Lister mes demandes de dette (En Cours ou Annulées)");
        System.out.println("4- Relancer une demande de dette annulée");
        System.out.println("5- Quitter");
        System.out.print("Choisissez une option : ");
    }

    public void menuAdmin() {
        System.out.println("----------Menu Principal----------");
        System.out.println("1- Créer un compte utilisateur à un client n’ayant pas de compte");
        System.out.println("2- Créer un compte utilisateur avec un rôle Boutiquier ou Admin");
        System.out.println("3- Afficher les comptes utilisateurs");
        System.out.println("4- Lister les articles (filtrer par disponibilité)");
        System.out.println("5- Créer un article");
        System.out.println("6- Mettre à jour la quantité en stock d’un article");
        System.out.println("7- Archiver les dettes soldées");
        System.out.println("8- Quitter");
        System.out.print("Choisissez une option : ");
    }

    public void menuBoutiquier() {
        System.out.println("----------Menu Principal----------");
        System.out.println("1- Créer un client ");
        System.out.println("2- Lister les clients");
        System.out.println("3- Rechercher un client par son numéro de téléphone");
        System.out.println("4- Créer une dette");
        System.out.println("5- Enregistrer un paiement pour une dette");
        System.out.println("6- Lister les dettes non soldées d’un client");
        System.out.println("7- Lister les demandes de dette en cours (valider ou refuser)");
        System.out.println("8- Quitter");
        System.out.print("Choisissez une option : ");
    }
}
