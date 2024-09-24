package dette.boutique.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import com.ibm.icu.text.SimpleDateFormat;

import dette.boutique.Main;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.repository.DetteRepository;

public class DetteService {
    ArticleService articleService;
    ClientService clientService;

    private DetteRepository detteRepository;
    Scanner scanner = Main.getScanner();

    public DetteService(DetteRepository detteRepository, ArticleService articleService, ClientService clientService) {
        this.detteRepository = detteRepository;
        this.articleService = articleService;
        this.clientService = clientService;
    }

    public List<Dette> list() {
        return detteRepository.selectAll();
    }

    public Dette tekDette(Client client) {
        String date = getDateSysteme();
        List<Details> details = saisirDétailsArticles();

        int montant = 0;
        for (Details détail : details) {
            montant += détail.getPrixTotal();
        }

        int montantVerse = saisirMontantVerse(montant);
        Dette dette = new Dette(date, montant, montantVerse, client, details);
        ajouterDetteAuClient(client, dette);

        System.out.println("Dette créée avec succès !");
        return dette;
    }

    public void ajouterDetteAuClient(Client clientBi, Dette dette) {
        List<Dette> dettes = clientBi.getDettes();
        if (dettes == null) {
            dettes = new ArrayList<>();
        }
        dettes.add(dette);

        clientBi.setDettes(dettes);
    }

    private String getDateSysteme() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(new Date());
    }

    private int saisirMontantVerse(int montantTotal) {
        int montantVerse = -1;
        while (montantVerse < 0 || montantVerse > montantTotal) {
            System.out.println("Veuillez saisir le montant versé par le client :");
            if (scanner.hasNextInt()) {
                montantVerse = scanner.nextInt();
                if (montantVerse < 0) {
                    System.out.println("Le montant versé ne peut pas être négatif.");
                } else if (montantVerse > montantTotal) {
                    System.out.println("Le montant versé ne peut pas dépasser le montant total de la dette.");
                }
            } else {
                System.out.println("Entrée invalide. Veuillez saisir un nombre entier.");
                scanner.next();
            }
        }
        scanner.nextLine();
        return montantVerse;
    }

    private List<Details> saisirDétailsArticles() {
        List<Details> détails = new ArrayList<>();
        List<Article> tousArticles = articleService.list();

        while (true) {
            afficherListeArticles(tousArticles);
            Article articleChoisi = choisirArticle(tousArticles);

            int quantité = saisirQuantitéArticle();

            Details détail = new Details(articleChoisi, quantité);
            détails.add(détail);

            if (!demanderAjouterAutreArticle()) {
                break;
            }
        }
        return détails;
    }

    private void afficherListeArticles(List<Article> articles) {
        System.out.println("Liste des articles disponibles :");
        for (int i = 0; i < articles.size(); i++) {
            Article article = articles.get(i);
            System.out.println((i + 1) + ". " + article);
        }
    }

    private Article choisirArticle(List<Article> articles) {
        int choixArticle = -1;
        while (choixArticle < 1 || choixArticle > articles.size()) {
            System.out.println("Veuillez choisir l'article en entrant le chiffre correspondant :");
            choixArticle = lireEntier();
            if (choixArticle < 1 || choixArticle > articles.size()) {
                System.out.println("Choix invalide. Veuillez entrer un chiffre correspondant à un article.");
            }
        }
        return articles.get(choixArticle - 1);
    }

    private int saisirQuantitéArticle() {
        int quantité = -1;
        while (quantité <= 0) {
            System.out.println("Veuillez saisir la quantité de l'article :");
            quantité = lireEntier();
            if (quantité <= 0) {
                System.out.println("La quantité doit être un nombre positif.");
            }
        }
        return quantité;
    }

    private boolean demanderAjouterAutreArticle() {
        System.out.println("Voulez-vous ajouter un autre article ? (oui/non)");
        String réponse = scanner.nextLine().trim().toLowerCase();
        return réponse.equals("oui");
    }

    private int lireEntier() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrée invalide. Veuillez saisir un nombre entier.");
            scanner.next();
        }
        return scanner.nextInt();
    }

}