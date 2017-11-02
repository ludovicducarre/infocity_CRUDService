package crud;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CrudServiceBean {

    /**
     * Created by ludovic on 11/05/17.
     * Classe qui  sert à rendre toute classe persistente
     *
     * S'utilise de la manière suivante :
     * CrudServiceBean csb = new CrudServiceBean();
     *       csb.newTransaction();
     *       csb.create(MON_OBJET_QUE_JE_VEUX_RENDRE_PERSISTANT);
     *       csb.commit();
     *       csb.close();
     */
    /**
     * L'entityManager est l'objet qui gère nos entités et l'EntityTransaction l'enregistrement en BD
     * jpa est le persistence-unit utilisé
     */
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa");

    private EntityManager em = entityManagerFactory.createEntityManager();
    private EntityTransaction transaction = em.getTransaction();

    /**
     * Fonction a utilise pour pouvoir déplacer l'objet en BD
     * Creer une nouvelle transaction
     */
    public void newTransaction() {
        if (!transaction.isActive())
            transaction.begin();
    }

    /**
     * Fonction qui permet de valider la transaction.
     * A faire après avoir créé les entités
     */
    public void commit() {
        transaction.commit();
    }

    /**
     * Fonction générique qui permet de créé une entité
     *
     * @param t   : Entite a creer
     * @param <T>
     * @return Retourne l'entite creer
     */
    public <T> T create(T t) {
        this.em.persist(t);
        this.em.flush();
        return t;
    }

    /**
     * Fonction qui permet de fermet l'entityManager
     * A utiliser a la fin lors d'une action sur la base de données
     */
    public void close() {
        if (!transaction.isActive()) {
            em.close();
            entityManagerFactory.close();
        }
    }

    /**
     * Fonction qui permet de recuperer une entité en fonction de son
     * identifiant passe en parametre
     *
     * @param type : Type de l'objet a recuperer dans la base de données
     * @param id   : identifiant de l'objet a recuperer
     * @param <T>  : Type de l'objet retourne
     * @return Retourne un objet generique
     */
    public <T> T find(Class<T> type, Object id) {
        T data = (T) this.em.find(type, id);
        return data;
    }

    /**
     * Fonction qui supprime une entite en fonction de l'id passé en
     * paramètre
     *
     * @param type : Type de l'object a supprime
     * @param id   : identifiant de l'entite a supprime
     */
    public void delete(Class type, Object id) {
        Object ref = this.em.getReference(type, id);
        this.em.remove(ref);
    }

    /**
     * Fonction qui permet de mettre a jour une entite
     *
     * @param t   : Entite a mettre a jour
     * @param <T> : Type de l'entte retourne
     * @return Retourne une entite de type generique
     */
    public <T> T update(T t) {
        return (T) this.em.merge(t);
    }

    /**
     * Fonction qui permet de recuperer une liste d'entites en fonction
     * d'une requete nommée realise sur la base de données
     *
     * @param type           : type de la classe sur laquel la fonction
     *                       va realise l'operation
     * @param namedQueryName : Requete nomme
     * @param <T>            : Type generique que retourne la fonction
     * @return List : liste d'entites
     */
    public <T> List findWithNamedQuery(Class<T> type, String namedQueryName) {
        return this.em.createNamedQuery(namedQueryName).getResultList();
    }

    /**
     * Fonction qui permet de recuperer une liste d'entite en fonction
     * d'une requete nomme avec plusieurs parametre sur la base de données
     *
     * @param type           : type de la classe sur laquel la fonction
     *                       va realise l'operaton
     * @param namedQueryName : requete nomme
     * @param parameters     : parametre de la requete nomme
     * @param <T>            : type generique que retourne la fonction
     * @return List : une liste d'entites
     */
    public <T> List findWithNamedQuery(Class<T> type, String namedQueryName, Map parameters) {
        return findWithNamedQuery(type, namedQueryName, parameters, 0);
    }

    /**
     * Fonction qui permet de recuperer une liste d'entite en fonction
     * d'une requete nomme ainsi qu'une limite de resultat
     *
     * @param type        : type de la classe sur laquel la fonction
     *                    va realise l'operation
     * @param queryName   : requete nomme
     * @param resultLimit : le nombre de resultat
     * @param <T>         : type generique que retourne la fonction
     * @return List : une liste d'entites
     */
    public <T> List findWithNamedQuery(Class<T> type, String queryName, int resultLimit) {
        return this.em.createNamedQuery(queryName).
                setMaxResults(resultLimit).
                getResultList();
    }

    /**
     * Fonction qui permet de recuperer une liste d'entite en fonction
     * d'une requete native en SQL
     *
     * @param sql  : requete native
     * @param type : type de la classe sur laquel la fonction
     *             va realise l'operation
     * @return List : liste d'entites
     */
    public List findByNativeQuery(String sql, Class type) {
        return this.em.createNativeQuery(sql, type).getResultList();
    }

    /**
     * Fonction qui permet de recuperer une liste d'entite en fonction
     * d'une requete nommme avec différents parametre et une limite
     * de resultats
     *
     * @param type           : type de la classe sur laquel la fonction
     *                       va realise l'operation
     * @param namedQueryName : requete nomme
     * @param parameters     : parametre de la requete nomme
     * @param resultLimit    : resultat limite
     * @param <T>            : type generique que retourne la fonction
     * @return List : liste d'entites
     */
    public <T> List findWithNamedQuery(Class<T> type, String namedQueryName, Map parameters, int resultLimit) {
        Set rawParameters = parameters.keySet();
        TypedQuery<T> query = this.em.createNamedQuery(namedQueryName, type);
        if (resultLimit > 0)
            query.setMaxResults(resultLimit);
        for (Object entry : rawParameters) {
            query.setParameter(entry.toString(), parameters.get(entry.toString()));
        }
        return query.getResultList();
    }

    /**
     * Fonction qui permet de retourne tout les entites contenu
     * dans table cible
     *
     * @param clazz : Table sur laquel la fonction va realise
     *              l'operation
     * @param <T>   : type generique que retourne la fonction
     * @return List : liste d'entites
     */
    public <T> List findAll(Class<T> clazz) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        Root<T> rootEntry = cq.from(clazz);
        CriteriaQuery<T> all = cq.select(rootEntry);
        TypedQuery<T> allQuery = em.createQuery(all);
        return allQuery.getResultList();
    }

    /**
     * Fonction qui permet de tester si une entite existe deja
     * dans la table cible
     *
     * @param type : Table sur laquel la fonction va realise
     *             l'operation
     * @param id   : identifiant de l'entite
     * @return boolean true si l'entite existe sinon false
     */
    public boolean exist(Class type, Object id) {
        return find(type, id) != null;
    }

}
