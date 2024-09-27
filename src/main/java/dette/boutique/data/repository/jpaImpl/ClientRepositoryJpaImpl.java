package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.database.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ClientRepositoryJpaImpl extends RepositoryJpaImpl<Client> implements ClientRepository {
    UserRepository userRepository;
    EntityManager em;

    public ClientRepositoryJpaImpl(EntityManager em, Class<Client> type, UserRepository userRepository) {
        super(em, type);
        this.userRepository = userRepository;
    }

    @Override
    public Client findByTel(String tel) {
        Client client = null;
        try {

            TypedQuery<Client> query = this.em.createQuery("SELECT c FROM Client c WHERE c.telephone = :tel",
                    Client.class);
            query.setParameter("tel", tel);

            client = query.getSingleResult();

        } catch (Exception e) {
            System.out.println("Erreur lors de la recherche du client par téléphone : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }

        return client;
    }

    @Override
    public void updateUserForClient(Client client) {

        try {
            em.getTransaction().begin();

            Client existingClient = em.find(Client.class, client.getId());

            if (existingClient != null) {
                existingClient.setUser(client.getUser());

                em.merge(existingClient);
            } else {
                System.out.println("Client non trouvé avec l'ID : " + client.getId());
            }
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Erreur lors de la mise à jour de l'utilisateur du client : " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

}
