import contact.ContactController;
import contact.ContactManager;
import contact.exception.RequestDeniedException;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class messagingtests {
    UUID myUser1 = UUID.randomUUID();
    UUID myUser2 = UUID.randomUUID();
    UUID myUser3 = UUID.randomUUID();
    UUID myUser4 = UUID.randomUUID();
    UUID myUser5 = UUID.randomUUID();

    ContactController contactController;
    ContactManager contactManager;

    @Before
    public void init() {
        contactManager = new ContactManager();
        contactController = new ContactController(contactManager);
    }

    @Test(timeout = 50)
    public void testSendRequests(){
        contactController.sendRequest(myUser1, myUser2);
        assert contactController.showRequests(myUser2).contains(myUser1) == true;
        assert contactController.showSentRequests(myUser1).contains(myUser2) == true;

    }

    @Test(timeout = 50, expected = RequestDeniedException.class)
    public void testSendRequests2(){
        contactController.sendRequest(myUser1, myUser2);
        contactController.sendRequest(myUser1, myUser2);
    }

    @Test(timeout = 50)
    public void testSendRequests3(){
        contactController.sendRequest(myUser1, myUser2);
        contactController.sendRequest(myUser1, myUser3);
        contactController.sendRequest(myUser2, myUser4);
        assert contactController.showRequests(myUser2).contains(myUser1) == true;
        assert contactController.showSentRequests(myUser1).contains(myUser2) == true;
        assert contactController.showRequests(myUser3).contains(myUser1) == true;
        assert contactController.showSentRequests(myUser1).contains(myUser3) == true;
        assert contactController.showRequests(myUser4).contains(myUser2) == true;
        assert contactController.showSentRequests(myUser2).contains(myUser4) == true;
    }

    @Test(timeout = 50)
    public void testAcceptRequests1(){
        contactController.sendRequest(myUser1, myUser2);
        contactController.acceptRequests(myUser2, myUser1);
        assert contactController.showContacts(myUser2).contains(myUser1);
        assert contactController.showRequests(myUser1).contains(myUser2) == false;
    }


}
