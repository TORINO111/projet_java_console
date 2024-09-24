package dette.boutique.Views;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.services.ArticleService;

public class ArticleView extends ViewImpl<Article> {
    private final ArticleService articleService;

    public ArticleView(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void menuUser() {
        while (true) {
            System.out.println("Menu Article:");
            System.out.println("1. Ajouter un article");
            System.out.println("2. Afficher tous les articles");
            System.out.println("3. Trouver un article par son libellé");
            System.out.println("4. Quitter");

            int choix = obtenirChoixUtilisateur(1, 4);
            switch (choix) {
                case 1:
                    create();
                    break;
                case 2:
                    articleService.list();
                    break;
                case 3:
                    List<Article> articles = articleService.list();
                    for (Article article : articles) {
                        System.out.println(article);
                    }
                    Article article = findArticle();
                    System.out.println(article);
                    break;
                case 4:
                    System.out.println("Au revoir !");
                    return; // Quitter le menu
                default:
                    System.out.println("Choix invalide. Veuillez réessayer.");
            }

        }

    }

    @Override
    public void create() {
        String libelle = saisieLibelle();
        int prixUnitaire = saisiePrixUnitaire();
        int qteStock = saisieQteStock();
        Article article = new Article(libelle, prixUnitaire, qteStock);
        articleService.create(article);
    }

    public String saisieLibelle() {
        Scanner scanner = Main.getScanner();
        String libelle = "";
        boolean isValide = false;

        while (!isValide) {
            System.out.println("Veuillez saisir le libellé de l'article (pas de chiffres ni de caractères spéciaux) :");
            libelle = scanner.nextLine().trim();
            try {
                if (libelle.isEmpty()) {
                    System.out.println("Erreur : le libellé de l'article ne doit pas être vide.");
                } else if (!libelle.matches("[a-zA-Z]+")) {
                    System.out.println("Erreur : le libellé de l'article ne doit contenir que des lettres.");
                } else {
                    isValide = true;
                    System.out.println("Prénom accepté!");
                }
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Saisie invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return libelle;
    }

    public int saisiePrixUnitaire() {
        int price = -1;
        Scanner scanner = Main.getScanner();

        while (price < 0) {
            System.out.println("Veuillez saisir le prix unitaire (nombre positif) :");
            price = scanner.nextInt();
            try {
                if (price < 0) {
                    System.out.println("Erreur : Le prix doit être un nombre positif.");
                }
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Saisie invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return price;
    }

    public int saisieQteStock() {
        int stock = -1;
        Scanner scanner = Main.getScanner();

        while (stock < 0) {
            System.out.println("Veuillez saisir la quantité en stock:");
            stock = scanner.nextInt();
            try {
                if (stock < 0) {
                    System.out.println("Erreur : La quantité doit être un nombre positif.");
                }
                scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Erreur : Saisie invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }
        return stock;
    }

    public Article findArticle() {
        Scanner scanner = Main.getScanner();
        boolean trouve = false;
        String libelle;
        Article article = null;

        while (!trouve) {
            System.out.println("Veuillez saisir textuellement le libellé de l'article :");
            libelle = scanner.nextLine();
            if (articleService.findArticle(libelle) != null) {
                System.out.println("Article trouvé !");
                article = articleService.findArticle(libelle);
                trouve = true;
                break;
            } else {
                System.out.println("Article non trouvé. Veuillez réessayer.");

            }
        }
        return article;
    }
}
