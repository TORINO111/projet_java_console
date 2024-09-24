package dette.boutique.data.enums;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;

public enum Role {
    ADMIN(1),
    BOUTIQUIER(2),
    CLIENT(3);

    private int roleId;

    Role(int roleId) {
        this.roleId = roleId;
    }

    public int getRoleId() {
        return roleId;
    }

    public List<Role> listerRole() {
        return Arrays.asList(Role.values());
    }

    public List<Role> listerRoleExceptClient() {
        List<Role> rolesDisponibles = listerRole().stream()
                .filter(role -> role != CLIENT)
                .toList();
        return rolesDisponibles;
    }

    public Role choisirRoleExceptClient() {
        List<Role> rolesExceptClient = listerRoleExceptClient();

        System.out.println("Liste des rôles disponibles :");
        for (int i = 0; i < rolesExceptClient.size(); i++) {
            System.out.println((i + 1) + ". " + rolesExceptClient.get(i));
        }

        int choix = numRoleChoisi(rolesExceptClient);
        return rolesExceptClient.get(choix - 1);
    }

    public Role choisirRole() {
        List<Role> rolesDisponibles = listerRole();
        System.out.println("Liste des rôles disponibles :");
        for (int i = 0; i < rolesDisponibles.size(); i++) {
            System.out.println((i + 1) + ". " + rolesDisponibles.get(i));
        }
        int choix = numRoleChoisi(rolesDisponibles);
        return rolesDisponibles.get(choix - 1);
    }

    public int numRoleChoisi(List<Role> roleYi) {
        int choix = -1;
        Scanner scanner = Main.getScanner();

        while (true) {
            System.out.print("Veuillez choisir un rôle (entrez le numéro correspondant) : ");
            try {
                choix = scanner.nextInt();
                if (choix < 1 || choix > roleYi.size()) {
                    System.out.println(
                            "Erreur : choix invalide. Veuillez entrer un numéro entre 1 et " + roleYi.size() + ".");
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
