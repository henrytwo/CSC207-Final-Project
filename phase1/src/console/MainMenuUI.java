package console;

public class MainMenuUI {

    public void run() {
        String[] options = new String[]{
                "asd",
                "asdasdas",
                "asdsad"
        };

        int selection = ConsoleUtilities.singleSelectMenu("Welcome to our boi", "Cool system man", options);

        switch (selection) {
            case 1:
                ConsoleUtilities.confirmBoxClear("u made the wrong choice");
                break;
            default:
                ConsoleUtilities.confirmBoxClear("hi there stranger");
        }
    }
}
