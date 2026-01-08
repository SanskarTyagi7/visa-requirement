import java.util.*;

public class VisaRuleEvaluator {

    private final RuleRepository repository;

    public VisaRuleEvaluator(RuleRepository repository) {
        this.repository = repository;
    }

    public VisaDecision evaluate(
            Country destination,
            Country passport,
            TravelPurpose purpose,
            int stayDays) {

        List<String> messages = new ArrayList<>();

        List<VisaRule> candidateRules =
                repository.findMatchingRules(destination, passport, purpose, stayDays);

        if (candidateRules.isEmpty()) {
            messages.add("No visa rule found for this route");
            return new VisaDecision(
                    true,
                    VisaCategory.EMBASSY_VISA,
                    VisaType.NONE,
                    Collections.emptyList(),
                    0,
                    messages
            );
        }

        if (candidateRules.size() > 1) {
            messages.add("Multiple rules matched, using first");
        }

        VisaRule selectedRule = candidateRules.get(0);

        return new VisaDecision(
                selectedRule.isVisaRequired(),
                selectedRule.getVisaCategory(),
                selectedRule.getVisaType(),
                selectedRule.getDocuments(),
                selectedRule.getProcessingDays(),
                messages
        );
    }
}
