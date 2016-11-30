package condorcet.appinfo3.groupe4.enigwall.DAO;

import com.sun.jersey.api.client.WebResource;

public abstract class DAO<A> {
    protected WebResource service = ServiceApex.getInstance();

    public abstract A read(String id) throws Exception;

    public abstract int create(A obj) throws Exception;

    public abstract void update(A obj) throws Exception;
}