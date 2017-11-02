package crud;

import java.util.List;
import java.util.Map;

public interface CrudService {
    <T> T create(T t);
    <T> T find(Class<T> type, Object id);
    <T> T update(T t);
    void delete(Class type, Object id);
    <T> List findWithNamedQuery(Class<T> type, String queryName);
    <T> List findWithNamedQuery(Class<T> type, String queryName, int resultLimit);
    <T> List findWithNamedQuery(Class<T> type, String namedQueryName, Map parameters);
    <T> List findWithNamedQuery(Class<T> type, String namedQueryName, Map parameters, int resultLimit);
}
