package dette.boutique;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import dette.boutique.core.services.EntityManagerCreator;
import dette.boutique.core.services.RepositoryFactory;
import dette.boutique.core.services.YamlService;
import dette.boutique.core.services.impl.YamlServiceImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.ArticleRepository;
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.DetailsRepository;
import dette.boutique.data.repository.DetteRepository;
import dette.boutique.data.repository.EtatRepository;
import dette.boutique.data.repository.RoleRepository;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.services.ArticleService;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetailsService;
import dette.boutique.services.DetteService;
import dette.boutique.services.EtatService;
import dette.boutique.services.RoleService;
import dette.boutique.services.UserService;
import dette.boutique.views.ArticleView;
import dette.boutique.views.ClientView;
import dette.boutique.views.DetteView;
import dette.boutique.views.RoleView;
import dette.boutique.views.UserView;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }

    public static void main(String[] args) {
        YamlService yamlService = new YamlServiceImpl();

        // Création de l'EntityManager ou de la liste selon la persistance
        EntityManagerCreator entityManagerCreator = new EntityManagerCreator(yamlService);
        Object persistenceHandler = entityManagerCreator.createEntityManagerFactory();
        String persistence = entityManagerCreator.getPersistenceName();

        // Repositories yi
        RepositoryFactory<User> userRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        UserRepository userRepository = (UserRepository) userRepositoryFactory.create(persistence, User.class);

        RepositoryFactory<Role> roleRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        RoleRepository roleRepository = (RoleRepository) roleRepositoryFactory.create(persistence, Role.class);

        RepositoryFactory<Client> clientRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        ClientRepository clientRepository = (ClientRepository) clientRepositoryFactory.create(persistence,Client.class);

        RepositoryFactory<Article> articleRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        ArticleRepository articleRepository = (ArticleRepository) articleRepositoryFactory.create(persistence,
                Article.class);

        RepositoryFactory<Dette> detteRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        DetteRepository detteRepository = (DetteRepository) detteRepositoryFactory.create(persistence, Dette.class);

        RepositoryFactory<Etat> etatRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        EtatRepository etatRepository = (EtatRepository) etatRepositoryFactory.create(persistence, Etat.class);

        RepositoryFactory<Details> detailsRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        DetailsRepository detailsRepository = (DetailsRepository) detailsRepositoryFactory.create(persistence,
                Details.class);

        // Services yi
        ArticleService articleService = new ArticleService(articleRepository);
        UserService userService = new UserService(userRepository);
        ClientService clientService = new ClientService(clientRepository);
        RoleService roleService = new RoleService(roleRepository);
        EtatService etatService = new EtatService(etatRepository);
        DetteService detteService = new DetteService(detteRepository, articleService, clientService);
        DetailsService detailsService = new DetailsService(detailsRepository);
        // Views yi
        ArticleView articleView = new ArticleView(articleService);
        ClientView clientView = new ClientView(clientService);
        UserView userView = new UserView(userService);
        RoleView roleView = new RoleView();
        DetteView detteView = new DetteView();

        Role role = null;
        boolean continuer = true;

        User userConnected = userView.connexion();
        while (continuer) {
            int choix = 0;
            if (userConnected.getRole().getNom().compareTo("admin") == 0) {
                // Partie Admin
                userView.menuAdmin();
                choix = userView.obtenirChoixUtilisateur(1, 8);
                try {
                    switch (choix) {
                        // Création compte utilisateur pour un client
                        case 1 -> {
                            // Choix du client
                            List<Client> clients = clientService.list();
                            Client clientAffilie = clientView.choisirClient(clients);

                            // Saisie des infos du compte utilisateur
                            String login = userView.saisieLogin();
                            String password = userView.saisiePassword();

                            // Assignation du rôle au client
                            Role roleUser = roleService.findRoleByName("client");

                            // Création du user
                            User user = new User(login, password, roleUser);
                            userService.create(user);
                            clientAffilie.setUser(user);
                            clientService.update(clientAffilie);
                            System.out.println("Compte utilisateur créé avec succès !");
                        }
                        // Création compte utilisateur avec rôle admin ou boutiquier
                        case 2 -> {
                            // Saisie des infos du compte utilisateur
                            String login = userView.saisieLogin();
                            String password = userView.saisiePassword();

                            User user = new User(login, password);

                            // Choix du rôle
                            String phraseRole = "Quel role voulez-vous assigner à cet utilisateur (1-Boutiquier | 2-Admin)";
                            int choixRole = roleView.choix(phraseRole, 1, 2);
                            if (choixRole == 1) {
                                user.setRole(roleService.findRoleByName("boutiquier"));
                            } else {
                                user.setRole(roleService.findRoleByName("admin"));
                            }

                            // Création du user
                            userService.create(user);
                            System.out.println("Compte utilisateur créé avec succès !");
                        }
                        // Liste users avec option filtrer par rôle ou statut
                        case 3 -> {
                            List<User> listUsers = userService.list();
                            userService.afficherListeUsers(listUsers);

                            String filtrer = "Voulez-vous filtrer la liste ? (1-Oui | 2-Non)";
                            int choixfiltrer = userView.choix(filtrer, 1, 2);

                            if (choixfiltrer == 1) {
                                String phraseChoixFiltre = "1- par role | 2- par statut)";
                                int choixFiltre = roleView.choix(phraseChoixFiltre, 1, 2);

                                // Filtre en fonction du rôle
                                if (choixFiltre == 1) {
                                    String phraseChoixRole = "Par quel role voulez-vous filtrer ? (1- Admin | 2- Boutiquier | 3- Client)";
                                    int choixRole = roleView.choix(phraseChoixRole, 1, 3);
                                    switch (choixRole) {
                                        case 1 -> {
                                            List<User> listAdmins = userService.listParRole("admin");
                                            if (!listAdmins.isEmpty()) {
                                                System.out.println("------Administrateurs------");
                                                userService.afficherListeUsers(listAdmins);
                                            } else {
                                                System.out.println("Aucun administrateur trouvé.");
                                            }
                                        }
                                        case 2 -> {
                                            List<User> listBoutiquiers = userService.listParRole("boutiquier");
                                            if (!listBoutiquiers.isEmpty()) {
                                                System.out.println("------Boutiquiers------");
                                                userService.afficherListeUsers(listBoutiquiers);
                                            } else {
                                                System.out.println("Aucun boutiquier trouvé.");
                                            }
                                        }
                                        case 3 -> {
                                            List<User> listClients = userService.listParRole("client");
                                            if (!listClients.isEmpty()) {
                                                System.out.println("------Clients------");
                                                userService.afficherListeUsers(listClients);
                                            } else {
                                                System.out.println("Aucun client trouvé.");
                                            }
                                        }
                                    }
                                    // Filtre en fonction du statut
                                } else {
                                    String phraseChoixStatut = "Par quel statut voulez-vous filtrer ? (1- Actif | 2- Inactifs)";
                                    int choixStatut = userView.choix(phraseChoixStatut, 1, 2);
                                    switch (choixStatut) {
                                        case 1 -> {
                                            List<User> listUsersActifs = userService.listUserActifs();
                                            if (!listUsersActifs.isEmpty()) {
                                                System.out.println("------Utilisateurs actifs------");
                                                userService.afficherListeUsers(listUsersActifs);
                                            } else {
                                                System.out.println("Aucun utilisateur actif trouvé.");
                                            }
                                        }
                                        case 2 -> {
                                            List<User> listUsersInactifs = userService.listUserInactifs();
                                            if (!listUsersInactifs.isEmpty()) {
                                                System.out.println("------Utilisateurs inactifs------");
                                                userService.afficherListeUsers(listUsersInactifs);
                                            } else {
                                                System.out.println("Aucun utilisateur inactif trouvé.");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // Lister articles et option filtrer par stock / out of stock
                        case 4 -> {
                            List<Article> listArticles = articleService.list();
                            articleService.afficherListe(listArticles);
                            String phraseFiltre = "Voulez-vous filtrer la liste ? (1- Oui | 2- Non)";
                            int choixFiltre1 = roleView.choix(phraseFiltre, 1, 2);
                            switch (choixFiltre1) {
                                case 1 -> {
                                    String phraseChoixFiltre = "Par quel type voulez-vous filtrer la liste ? (1- En Stock | 2- En rupture de Stock)";
                                    int choixFiltre = roleView.choix(phraseChoixFiltre, 1, 2);
                                    switch (choixFiltre) {
                                        case 1 -> {
                                            articleService.afficherArticlesDispo();
                                        }
                                        case 2 -> {
                                            articleService.afficherArticlesIndispo();
                                        }
                                    }
                                }
                                case 2 -> {
                                    break;
                                }
                            }

                        }
                        // Création d'article
                        case 5 -> {
                            Article article = articleView.saisieArticle();
                            articleService.create(article);
                            System.out.println("Article ajouté avec succès.");
                        }
                        // Mise à jour quantité stock d'un article
                        case 6 -> {
                            // Choix d'article
                            List<Article> listArticles = articleService.list();
                            Article articleAModifier = articleView.choisirArticle(listArticles);

                            // Nouvelle quantité en stock
                            int newQteStock = articleView.saisieNewQte();

                            // Mise à jour
                            articleAModifier.setQteStock(newQteStock);
                            articleAModifier.setUpdatedAt(LocalDateTime.now());
                            articleService.update(articleAModifier);
                        }
                        // Archiver les dettes soldées
                        case 7 -> {
                            List<Dette> listDettes = detteService.listeDettesSoldees();
                            detteService.archiverDettesSoldees(listDettes);
                        }

                        case 8 -> {
                            continuer = quitter();
                        }
                    }
                } catch (Exception e) {

                }
                // Partie Boutiquier
            } else if (userConnected.getRole().getNom().compareTo("boutiquier") == 0) {
                userView.menuBoutiquier();
                choix = userView.obtenirChoixUtilisateur(1, 7);
                try {
                    switch (choix) {
                        // Créer client
                        case 1 -> {
                            System.out.println("-----Création du client-----");
                            Client client = clientView.saisieClient();
                            clientService.create(client);
                            System.out.println("Client créé avec succès");
                        }
                        // Lister clients
                        case 2 -> {
                            System.out.println("-----Liste des clients-----");
                            List<Client> listClients = clientService.list();
                            clientService.afficherListe(listClients);
                        }
                        // Find client by telephone
                        case 3 -> {
                            System.out.println("-----Recherche d'un client avec son numéro de téléphone-----");
                            Client clientRecherche = clientView.saisieClientRecherche();
                            System.out.println(clientRecherche);
                        }
                        // Creer dette
                        case 4 -> {
                            System.out.println("-----Création de dette-----");
                            List<Client> clients = clientService.list();
                            Client clientDette = clientView.choisirClient(clients);

                            if (clientDette != null) {
                                List<Details> panierArticles = articleView.selectionArticles();
                                detailsService.createList(panierArticles);

                                // Assigner articles à leur détail
                                try {
                                    articleService.assignerArticlesAuDetail(panierArticles);
                                } catch (Exception e) {
                                    System.out.println("Erreur lors de l'assignation des articles aux détails : "
                                            + e.getMessage());
                                    e.printStackTrace();
                                }
                                // Créer une dette
                                if (!panierArticles.isEmpty()) {
                                    try {
                                        Dette dette = detteService.creerEtAffecterDette(clientDette, panierArticles,
                                                etatService, detailsService);
                                        // Affectation de la dette au client
                                        clientService.affecterDetteAuClient(clientDette, dette);
                                        System.out.println("Dette créée et assignée avec succès au client.");

                                    } catch (Exception e) {
                                        System.out
                                                .println("Erreur lors de la création de la dette : " + e.getMessage());
                                    }
                                } else {
                                    System.out.println("Aucun article sélectionné pour la dette.");
                                }
                            } else {
                                System.out.println("Création de dette impossible !");
                            }
                        }
                        // Enregistrer versement
                        case 5 -> {
                            System.out.println("----Enregistrement d'un paiement pour une dette");
                            // Choix dette
                            List<Dette> listDettes = detteService.listeDettesNonSoldees();

                            if (listDettes != null) {
                                // Choix dette
                                Dette detteChoisie = detteView.choisirDette(listDettes);
                                // Saisie montant versé
                                int montantPrecedent = detteChoisie.getMontantRestant();
                                int oldMontantVerse = detteChoisie.getMontantVerse();
                                int montantVerse = detteView.saisieVersement(montantPrecedent);
                                int montantRestant = montantPrecedent - montantVerse;
                                // MAJ dette
                                detteService.updateDette(detteChoisie, oldMontantVerse, montantVerse, montantRestant,
                                        etatService);

                                // MAJ client
                                Client clientDette = detteChoisie.getClient();
                                if (detteService.updateDetteClient(clientDette, detteChoisie, etatService)) {
                                    System.out.println("Dette et client mis à jour avec succès!");
                                    System.out.println("La dette du client: " + clientDette.getNom() + " "
                                            + clientDette.getPrenom() + " a été mise à jour.");
                                    System.out.println(
                                            "Montant versé : " + montantVerse + " MontantRestant: " + montantRestant);
                                } else {
                                    System.out.println("Aucune mise à jour effectuée.");
                                }

                            } else {
                                System.out.println("Il n'y a aucune dette enregistrée.");
                            }
                        }
                        // Lister dettes non soldées d'un client
                        case 6 -> {
                            System.out.println("----Lister les dettes non soldées d'un client----");
                            List<Client> clients = clientService.list();
                            Client clientChoisi = clientView.choisirClient(clients);

                            if (clientChoisi != null) {
                                List<Dette> listDettes = detteService.listeDettesNonSoldees(clientChoisi);
                                if (!listDettes.isEmpty()) {
                                    System.out.println(
                                            "Dettes de : " + clientChoisi.getPrenom() + " " + clientChoisi.getNom());
                                    detteService.afficherListe(listDettes);
                                } else {
                                    System.out.println("Aucune dette non soldee à ce jour pour : "
                                            + clientChoisi.getPrenom() + " " + clientChoisi.getNom());
                                }
                            }
                        }
                        // Liste demandes de dettes en cours
                        case 7 -> {
                            List<Dette> listDettes = detteService.listeDettesEnAttenteDeValidation();
                            System.out.println("----Lister les demandes de dettes----");
                            detteService.afficherDettesEnCours();
                            String filtrer = "Voulez-vous valider ou annuler une (des) dette(s) ? (1-Valider | 2-Annuler)";
                            int choixfiltrer = userView.choix(filtrer, 1, 2);

                            switch (choixfiltrer) {
                                case 1 -> {
                                    System.out.println("----Validation de dette----");
                                    Dette dette = detteView.choisirDette(listDettes);
                                    dette.setEtat(etatService.findByNom("EN_COURS"));
                                    detteService.update(dette);
                                    System.out.println("Dette validée avec succès!");
                                }
                                case 2 -> {
                                    System.out.println("----Annulation de dette----");
                                    Dette dette = detteView.choisirDette(listDettes);
                                    dette.setEtat(etatService.findByNom("ANNULEE"));
                                    detteService.update(dette);
                                    System.out.println("Dette annulée avec succès!");
                                }
                                default -> {
                                    System.out.println("Aucune action effectuée.");
                                }
                            }
                        }
                        // Quitter
                        case 8 -> {
                            continuer = quitter();
                        }
                    }
                } catch (Exception e) {

                }
            } else if (userConnected.getRole().getNom().compareTo("client") == 0) {
                userView.menuCLient();
                choix = userView.obtenirChoixUtilisateur(1, 5);
                try {
                    switch (choix) {
                        case 1 -> {
                            System.out.println("-----Mes dettes-----");

                            Client clientDette = clientService.findClientByUserId(userConnected.getId());
                            //Dettes du client
                            List<Dette> listClientsDette = detteService.listDette(clientDette);
                            if (!listClientsDette.isEmpty()) {
                                detteService.afficherListe(listClientsDette);
                            } else {
                                System.out.println("Aucune dette enregistrée à ce jour.");
                            }
                        }

                        case 2 -> {
                            System.out.println("-----Demande de dette-----");
                            // Récupération du client connecté et de ses dettes
                            Client clientDette = clientService.findClientByUserId(userConnected.getId());

                            // Sélection des articles par l'utilisateur
                            List<Details> panierArticles = articleView.selectionArticles();
                            articleService.assignerArticlesAuDetail(panierArticles);

                            // Définition de l'état initial de la dette
                            Etat etatEnAttente = etatService.findByNom("EN_ATTENTE_DE_VALIDATION");
                            // Création de la dette
                            Dette dette = detteService.defarDette(clientDette, panierArticles);

                            dette.setEtat(etatEnAttente);
                            detteService.create(dette);
                            detailsService.assignerDette(panierArticles, dette);

                            // Assignation de la dette au client
                            clientDette.getDettes().add(dette);
                            clientDette.setUpdatedAt(LocalDateTime.now());

                            // Mise à jour des informations du client dans la base de données
                            clientService.update(clientDette);

                            System.out.println("Demande de dette effectuée avec succès !");
                        }

                        case 3 -> {
                            System.out.println("----Mes demandes de dette----");
                            Client clientDette = clientService.findClientByUserId(userConnected.getId());

                            List<Dette> listDettes = detteService.listeDettesEnAttenteDeValidation(clientDette);
                            if (!listDettes.isEmpty()) {
                                detteService.afficherListe(listDettes);

                                String filtrer = "Voulez-vous filtrer la liste ? (1-En attente de validation | 2-Annulees)";
                                int choixfiltrer = userView.choix(filtrer, 1, 2);

                                // Liste dettes en cours
                                if (choixfiltrer == 1) {
                                    detteService.afficherDettesEnCours();
                                } else {
                                    detteService.afficherDettesAnnulees();
                                }
                            } else {
                                System.out.println("Aucune dette à ce jour .");
                            }

                        }

                        case 4 -> {
                            System.out.println("----Relancement d'une demande de dette annulée ");
                            List<Dette> listDettesEnCours = detteService.listeDettesAnnulees();
                            Dette dette = detteView.choisirDette(listDettesEnCours);
                            dette.setEtat(etatService.findByNom("EN_ATTENTE_DE_VALIDATION"));
                            detteService.update(dette);
                            System.out.println("Demande de dette relancée avec succès!");
                        }

                        case 5 -> {
                            continuer = quitter();;
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
    }

    public static boolean quitter() {
        System.out.println("Au revoir.");
        return false; // Retourne directement false pour indiquer que le programme doit s'arrêter.
    }

}