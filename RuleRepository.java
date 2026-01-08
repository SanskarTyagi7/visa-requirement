import java.util.*;
import java.util.stream.Collectors;

public class RuleRepository {

    private final List<VisaRule> rules;

    public RuleRepository(List<VisaRule> rules) {
        this.rules = new ArrayList<>(rules);
    }

    public List<VisaRule> findMatchingRules(
            Country destination,
            Country passport,
            TravelPurpose purpose,
            int stayDays) {

        return rules.stream()
                .filter(r -> r.getDestination() == destination)
                .filter(r -> r.getPassportCountry() == passport)
                .filter(r -> r.getPurpose() == purpose)
                .filter(r -> stayDays <= r.getMaxStayDays())
                .collect(Collectors.toList());
    }
}

