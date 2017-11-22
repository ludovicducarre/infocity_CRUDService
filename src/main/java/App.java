
import crud.CrudServiceBean;
import fr.univtln.lducarre365.infoCity.business.Advert;
import fr.univtln.lducarre365.infoCity.business.Town;
import fr.univtln.lducarre365.infoCity.business.User;

/**
 * @author ludovic
 * Cette classe sert au developpeur à tester la persistence
 * et à verifier la bonne construction des classes métiers
 */

public class App {


    public static void main(String[] args) {

        Town town = new Town();
        town.setId(125);
        town.setName("Toulon");
        town.setCountry("PACA");
        town.setState("France");



        User user2 = new User(456,"paul","martin","paulmartin@mail.fr","pass");

        //town.addUser(user2);
        User user3 = new User.Builder().setId(65).setFirstName("test").setLastName("retest").build();

        Advert advert = new Advert.Builder().setId(1).setMessage("Oye! Oye!").setLocation("rue bidon")
        .setType("sport").build();
        advert.setTown(town);

        town.addUser(user2);




        // System.out.println(user.toString());

        System.out.println(town.toString());
        System.out.println(user2.toString());
        System.out.println(user3.toString());
        System.out.println(advert.toString());



        CrudServiceBean csb = new CrudServiceBean();
            csb.newTransaction();
            csb.create(town);
            csb.create(user2);
            csb.create(advert);
            csb.commit();
            csb.close();

    }


}
