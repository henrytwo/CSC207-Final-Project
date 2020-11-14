public class Main {

    /**
     * UI Components to build
     * - n-column table with numbered rows (Shubhra)
     * - messaging thing (Mahak)
     */

    /**
     * TODO: Don't pass around Message objects at the top level
     *
     * Meeting notes: - We're basically set for the phase 2 mandatory extensions
     *                - Optional extensions
     *                  - Users can pick which conference they want to participate in (already implemented)
     *                  - Database should be possible
     *                  - GUI? (would count as two features)
     *                - Still waiting on Messaging + Contact UI to be built
     *                - Messaging has higher priority than Contact, since contact technically isn't a Phase 1 requirement
     *                - Generic table UI thing should be done' (Ellie is working on it)
     */

    /**
     * Things to check before submission
     * - Java docs
     * - UML diagram
     * - Testing
     */

    public static void main(String[] args) {
        ConventionSystem cs = new ConventionSystem();
        cs.run();
    }
}
