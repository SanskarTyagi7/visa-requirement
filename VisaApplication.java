import java.util.*;

public class VisaApplication {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {
            System.out.print("Destination country: ");
            Country destination = Country.valueOf(sc.nextLine().trim().toUpperCase());

            System.out.print("Passport country: ");
            Country passport = Country.valueOf(sc.nextLine().trim().toUpperCase());

            System.out.print("Travel purpose (TOURISM, BUSINESS, STUDY) ");
            TravelPurpose purpose = TravelPurpose.valueOf(sc.nextLine().trim().toUpperCase());

            System.out.print("Stay duration (days): ");
            int stayDurationDays = sc.nextInt();

            RuleLoader loader = new RuleLoader();
            RuleRepository repository = new RuleRepository(loader.loadRules());

            VisaRuleEvaluator evaluator = new VisaRuleEvaluator(repository);

            VisaDecision decision = evaluator.evaluate(destination, passport, purpose, stayDurationDays);

            if (decision.getWarnings().contains("No visa rule found for this route")) {
                System.out.println("\nNo visa rule found for this route");
            } else {
                System.out.println("\n--- VISA DECISION ---");
                System.out.println("Visa Required: " + decision.isVisaRequired());
                System.out.println("Visa Type: " + decision.getVisaType());
                System.out.println("Documents: " + decision.getDocuments());
                System.out.println("Processing Days: " + decision.getProcessingDays());
                if (!decision.getWarnings().isEmpty()) {
                    System.out.println("Warnings: " + decision.getWarnings());
                }
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input provided.");
        }
    }
}
