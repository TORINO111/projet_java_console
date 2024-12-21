package dette.boutique.core;

public interface View {
    int obtenirChoixUtilisateur(int min, int max);

    int choix(String phrase, int min, int max);
}
