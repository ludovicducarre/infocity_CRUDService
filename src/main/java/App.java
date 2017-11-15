import business.Town;
import business.User;
import crud.CrudServiceBean;

public class App {


    public static void main(String[] args) {
        /*User user = new User.Builder().setId(156).setEmail("user1resu@user.com")
                .setLastName("user1").setFirstName("resu").setPassword("pass").build();
*/

        Town town = new Town();
        town.setId(125);
        town.setName("Toulon");

       // System.out.println(user.toString());

        System.out.println(town.toString());



        CrudServiceBean csb = new CrudServiceBean();
            csb.newTransaction();
            csb.create(town);
            csb.commit();
            csb.close();

    }


}
