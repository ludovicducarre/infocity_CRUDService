package crud;

import java.util.HashMap;
import java.util.Map;

public class QueryParameter {
    private Map parameters = new HashMap();

    private QueryParameter(String name,Object value){
        this.parameters.put(name, value);
    }
    public static QueryParameter with(String name,Object value){
        return new QueryParameter(name, value);
    }
    public QueryParameter and(String name,Object value){
        this.parameters.put(name, value);
        return this;
    }
    public Map parameters(){
        return this.parameters;
    }
}
