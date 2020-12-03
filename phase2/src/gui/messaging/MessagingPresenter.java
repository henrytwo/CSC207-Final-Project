package gui.messaging;

import gui.login.ILoginView;
import gui.util.enums.PanelFactoryOptions;
import gui.util.interfaces.IFrame;
import gui.util.interfaces.IPanelFactory;
import messaging.ConversationController;
import user.UserController;
import util.ControllerBundle;

import java.util.List;
import java.util.UUID;

public class MessagingPresenter {
    private ConversationController convoController;
    private UUID signedInUserUUID;
    private List<UUID> conversationUUIDs;
    private int currentConversationIndex = -1;
    private IFrame mainFrame;
    private IMessagingView messagingView;
    private IPanelFactory panelFactory;

    public MessagingPresenter(IFrame mainFrame, IMessagingView messagingView) {

    }
    void createNewChat(){

    }

    void sendmessage(String content){
//    convoController.sendMessage(signedInUserUUID, , );
    }
}