import contact.ContactController;
import contact.ContactManager;

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

    
}
