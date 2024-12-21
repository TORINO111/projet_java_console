package dette.boutique.services;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.Item;
import dette.boutique.core.Service;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;

public class UserService extends Service<Dette> implements Item<User> {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(User user) {
        userRepository.insert(user);
    }

    @Override
    public List<User> list() {
        return userRepository.selectAll();
    }

    @Override
    public void update(User user) {
        userRepository.insert(user);
    }

    public boolean updateClientForUser(User user) {
        userRepository.updateClientForUser(user);
        return true;
    }

    public void afficherListeUsers(List<User> users) {
        for (User user : users) {
            System.out.println(user);
        }
    }

    public List<User> listParRole(String role) {
        return list().stream()
                .filter(user -> user.getRole().getNom().compareTo(role) == 0)
                .toList();
    }

    public boolean findLogin(String login) {
        return list().stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst()
                .isPresent();
    }

    public List<User> listUserActifs() {
        return list().stream()
                .filter(user -> user.isActive())
                .toList();
    }

    public List<User> listUserInactifs() {
        return list().stream()
                .filter(user -> !(user.isActive()))
                .toList();
    }

    public User findUserByLoginAndPassword(String login, String password) {
        return list().stream()
                .filter(user -> user.getLogin().equals(login) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    public List<User> listUserParRole(Role role) {
        return list().stream()
                .filter(user -> user.getRole().equals(role))
                .toList();
    }

    public List<User> listeUsersDispo() {
        return userRepository.selectAll().stream()
                .filter(user -> user.getClient() == null)
                .toList();
    }

    public List<User> listUserInactive() {
        return list().stream()
                .filter(user -> user.isActive() == false)
                .toList();
    }

    public User choixUserDesactiver() {
        List<User> users = listUserActifs();

        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur actif.");
            return null;
        }

        System.out.println("Liste des utilisateurs actifs disponibles :");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
        int choix = numUserChoisi(users);
        return users.get(choix - 1);
    }

    public User choixUserActiver() {
        List<User> users = listUserInactive();
        if (users == null || users.isEmpty()) {
            System.out.println("Aucun utilisateur actif.");
            return null;
        }
        System.out.println("Liste des utilisateurs inactifs :");
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
        int choix = numUserChoisi(users);
        return users.get(choix - 1);
    }

    public void activerUtilisateur(User user) {
        user.setActive(true);
    }

    public void desactiverUtilisateur(User user) {
        user.setActive(true);
    }

    public static int numUserChoisi(List<User> userYi) {
        int choix = -1;
        Scanner scanner = Main.getScanner();

        while (true) {
            System.out.print("Veuillez choisir un client (entrez le numéro correspondant) : ");
            try {
                choix = scanner.nextInt();
                if (choix < 1 || choix > userYi.size()) {
                    System.out.println(
                            "Erreur : choix invalide. Veuillez entrer un numéro entre 1 et " + userYi.size() + ".");
                } else {
                    break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Erreur : saisie invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return choix;
    }



}
