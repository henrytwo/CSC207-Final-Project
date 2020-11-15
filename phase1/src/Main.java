/**
 * Entry point of the application
 */
public class Main {

    /**
     * Meeting notes: - We're basically set for the phase 2 mandatory extensions
     *                - Optional extensions
     *                  - Users can pick which conference they want to participate in (already implemented)
     *                  - Database should be possible
     *                  - GUI? (would count as two features)
     *                - Generic table UI thing should be done' (Ellie is working on it)
     */

    /**
     * Things to check before submission
     * - Java docs
     * - UML diagram
     * - Testing
     * - Update README if contact is not added in this phase
     */

    public static void main(String[] args) {
        ConventionSystem cs = new ConventionSystem();
        cs.run();
    }
}
