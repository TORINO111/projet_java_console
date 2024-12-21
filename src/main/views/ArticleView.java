package dette.boutique.views;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dette.boutique.Main;
import dette.boutique.core.ViewImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Details;
import dette.boutique.services.ArticleService;

public class ArticleView extends ViewImpl<Article> {
    private final ArticleService articleService;

    public ArticleView(ArticleService articleService) {
        this.articleService = articleService;
    }

    public Article saisieArticle() {
        String libelle = saisieLibelle();
        int prixUnitaire = saisiePrixUnitaire();
        int qteStock = saisieQteStock();
        Article article = new Article(libelle, prixUnitaire, qteStock);
        return article;
    }

    public Article choisirArticle(List<Article> articles) {
        if (articles == null || articles.isEmpty()) {
            System.out.println("Aucun article disponible.");
            return null;
        } else {
            afficherList(articles);
            System.out.println("Veuillez saisir l'index de l'article que vous voulez choisir.");
            int choix = obtenirChoixUtilisateur(1, articles.size());
            return articles.get(choix - 1);
        }
    }

    public List<Details> selectionArticles() {
        List<Article> listArticles = articleService.listeArticlesDispo();
    
        if (listArticles.isEmpty()) {
            System.out.println("Aucun article disponible !");
            return new ArrayList<>();
        }
    
        List<Details> panier = new ArrayList<>();
    
        while (true) {
            afficherList(listArticles);
    
            // Sélection de l'article
            int choixArticle = obtenirChoixUtilisateur(1, listArticles.size());
            Article article = listArticles.get(choixArticle - 1);
    
            // Saisie de la quantité
            int quantite = saisieQuantite(article);
    
            // Mettre à jour le stock et ajouter au panier
            article.setQteStock(article.getQteStock() - quantite);
            Details detail = new Details(article, quantite);
            panier.add(detail);
    
            // Description du choix
            System.out.println("Article ajouté au panier : " + article.getLibelle() + " | Quantité : " + quantite);
    
            // Demander si l'utilisateur souhaite continuer
            int choixContinuer = choix("Souhaitez-vous ajouter un nouvel article ? (1-oui | 2-non)", 1, 2);
            if (choixContinuer == 2) {
                break;
            }
        }
    
        return panier;
    }
    
    public int saisieQuantite(Article article) {
        int quantite;
        System.out.println("Quelle quantité voulez-vous ?");
        while (true) {
            quantite = saisieEntierPositif();
            if (quantite > article.getQteStock()) {
                System.out.println("Quantité insuffisante en stock. Veuillez réessayer.");
            } else {
                break;
            }
        }
        return quantite;
    }
    // public int montantPanier(List<Article> panierArticles){
    // int montant = 0;
    // for (Article article : panierArticles) {
    // montant += article.get
    // }
    // return montant;
    // }

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
                } else if (!libelle.matches("[a-zA-ZÀ-ÿ\\s]+")) {
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

    public Article saisieArticleRecherche() {
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

    public int saisieNewQte() {
        System.out.println("Veuillez saisir la nouvelle quantité en stock : ");
        int newQteStock = saisieEntierPositif();
        return newQteStock;
    }

}
