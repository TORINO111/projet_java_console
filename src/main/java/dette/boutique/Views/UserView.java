package dette.boutique.Views;

import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.User;
import dette.boutique.services.UserService;

public class UserView extends ViewImpl<User> {

    private final UserService userService;

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void menu() {
        System.out.println("1. Créer un utilisateur");
        System.out.println("2. Créer un utilisateur associé à un client");
        System.out.println("3. Associer un client à un utilisateur");
        System.out.println("4. Afficher tous les utilisateurs");
        System.out.println("5. Quitter");
    }

    @Override
    public void create() {
    }

    public String saisieLogin() {
        Scanner scanner = Main.getScanner();
        String login = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.println(
                    "Veuillez saisir le login de l'utilisateur (au moins 4 caractères, lettres et chiffres seulement sans espace, tirets du 8 acceptés) :");
            login = scanner.nextLine().trim();

            if (login.isEmpty()) {
                System.out.println("Erreur : le login ne doit pas être vide.");
            } else if (login.length() < 4) {
                System.out.println("Erreur : le login doit contenir au moins 4 caractères.");
            } else if (!login.matches("[a-zA-Z0-9_]+")) {
                System.out.println(
                        "Erreur : le login ne doit contenir que des lettres des chiffres et/ou des underscore.");
            } else if (userService.findLogin(login)) {
                System.out.println("Erreur : le login est déjà attribué.");
            } else {
                isValid = true;
                System.out.println("Login accepté !");
            }
        }
        return login;
    }

    public String saisiePassword() {
        Scanner scanner = Main.getScanner();
        String password = "";
        boolean isValid = false;

        while (!isValid) {
            System.out.println(
                    "Veuillez saisir le mot de passe de l'utilisateur (au moins 4 caractères, lettres et chiffres seulement) :");
            password = scanner.nextLine().trim();

            if (password.isEmpty()) {
                System.out.println("Erreur : le mot de passe ne doit pas être vide.");
            } else if (password.length() < 4) {
                System.out.println("Erreur : le mot de passe doit contenir au moins 4 caractères.");
            } else if (!password.matches("[a-zA-Z0-9]+")) {
                System.out.println("Erreur : le mot de passe ne doit contenir que des lettres et des chiffres.");
            } else {
                isValid = true;
                System.out.println("Mot de passe accepté !");
            }
        }
        return password;
    }

    public User choisirUser() {
        List<User> users = userService.listeUsersDispo();
        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur disponible.");
            return null;
        } else {
            afficherList(users);
            System.out.println("Veuillez saisir l'index de l'utilisateur que vous voulez choisir.");
            int choix = obtenirChoixUtilisateur(1, users.size());
            return users.get(choix - 1);
        }
    }

}
