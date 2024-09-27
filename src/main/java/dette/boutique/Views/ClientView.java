package dette.boutique.Views;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.services.ClientService;

public class ClientView extends ViewImpl<Client> {
    private final ClientService clientService;

    public ClientView(ClientService clientService) {
        this.clientService = clientService;
    }

    public void menu() {
        System.out.println("1. Ajouter un client");
        System.out.println("2. Afficher tous les clients");
        System.out.println("3. Trouver un client avec son numéro de téléphone");
        System.out.println("4. Quitter");
    }

    public void menuClient() {
        while (true) {
            menu();
            int choix = obtenirChoixUtilisateur(1, 4);
            switch (choix) {
                case 1:
                    create();
                    break;
                case 2:
                    List<Client> clients = clientService.list();
                    System.out.println("-----Liste des clients-----");
                    for (Client client : clients) {
                        System.out.println(client);
                    }
                    System.out.println("Fin de Liste");
                    break;
                case 3:
                    Client client = findClient();
                    System.out.println(client);
                    break;
                case 4:
                    System.out.println("Sortie du menu client.");
                    return;
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }
        }
    }

    @Override
    public void create() {
        String nom = saisieNom();
        String prenom = saisiePrenom();
        String adresse = saisieAdresse();
        String telephone = saisieTelephone();
        Client client = new Client(nom, prenom, adresse, telephone);
        clientService.create(client);
    }

    public Client choisirCLient() {
        List<Client> clients = clientService.listeClientsDispo();
        if (clients == null || clients.isEmpty()) {
            System.out.println("Aucun client disponible.");
            return null;
        } else {
            afficherList(clients);
            System.out.println("Veuillez saisir l'index du client que vous voulez choisir.");
            int choix = obtenirChoixUtilisateur(1, clients.size());
            return clients.get(choix - 1);
        }
    }

    public String saisieNom() {
        Scanner scanner = Main.getScanner();
        String libelle = "";
        boolean isValide = false;

        while (!isValide) {
            System.out.println("Veuillez saisir le nom  du client (pas de chiffres ni de caractères spéciaux):");
            libelle = scanner.nextLine().trim();
            try {
                if (libelle.isEmpty()) {
                    System.out.println("Erreur : le nom ne doit pas être vide.");
                } else if (!libelle.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : le nom ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Nom accepté !");
                    System.out.println("---------------------------");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }

        return libelle;
    }

    public String saisiePrenom() {
        Scanner scanner = Main.getScanner();
        String libelle = "";
        boolean isValide = false;

        while (!isValide) {
            System.out.println(
                    "Veuillez saisir le prénom du client (pas de chiffres ni de caractères spéciaux) :");
            libelle = scanner.nextLine().trim();
            try {
                if (libelle.isEmpty()) {
                    System.out.println("Erreur : le prénom ne doit pas être vide.");
                } else if (!libelle.matches("[a-zA-Z ]+")) {
                    System.out.println("Erreur : le prénom ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Prénom accepté !");
                    System.out.println("---------------------------");
                    
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
            }
        }

        return libelle;
    }

    public String saisieTelephone() {
        Scanner scanner = Main.getScanner();
        String numero = "";
        boolean isValide = false;

        while (!isValide) {
            System.out.println("Veuillez saisir le numéro du client (uniquement des chiffres) :");
            numero = scanner.nextLine().trim();
            try {
                if (numero.isEmpty()) {
                    System.out.println("Erreur : le numéro ne doit pas être vide.");
                } else if (numero.length() != 9) {
                    System.out.println("Erreur : le numéro doit contenir exactement 9 chiffres.");
                } else if (!numero.matches("[0-9]+")) {
                    System.out.println("Erreur : le numéro ne doit contenir que des chiffres.");
                } else if (clientService.numDispo(numero)) {
                    System.out.println("Ce numéro est déjà attribué.");
                } else {
                    isValide = true;
                    System.out.println("Numéro accepté!");
                    System.out.println("---------------------------");
                }
            } catch (InputMismatchException e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }
        return numero;
    }

    public String saisieAdresse() {
        Scanner scanner = Main.getScanner();
        String libelle = "";
        boolean isValide = false;

        while (!isValide) {
            System.out.println("Veuillez saisir l'adresse du client :");
            libelle = scanner.nextLine();
            try {
                if (libelle.isEmpty()) {
                    System.out.println("Erreur : l'adresse ne doit pas être vide.");
                } else if (!libelle.matches("[a-zA-Z0-9 _-]+")) {
                    System.out.println("Erreur : l'adresse ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Adresse acceptée !");
                }
            } catch (Exception e) {
                System.out.println("Veuillez saisir des caractères valides.");
                scanner.nextLine();
            }
        }

        return libelle;
    }

    public Client findClient() {
        Client client = null;
        Scanner scanner = Main.getScanner();
        String numero = "";
        boolean isValide = false;

        while (!isValide) {
            numero = scanner.nextLine().trim();
            System.out.println("Veuillez saisir le numéro du client à rechercher:");
            numero = scanner.nextLine();
            if (clientService.findClient(numero) != null) {
                System.out.println("Client trouvé !");
                client = clientService.findClient(numero);
                isValide = true;
                break;
            } else {
                System.out.println("Client non trouvé. Veuillez réessayer.");

            }
        }
        return client;
    }
}
