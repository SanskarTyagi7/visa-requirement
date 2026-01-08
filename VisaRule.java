import java.util.*;

public final class VisaRule {

    private final Country destination;
    private final Country passportCountry;
    private final TravelPurpose purpose;
    private final int maxStayDays;

    private final boolean visaRequired;
    private final VisaCategory visaCategory;
    private final VisaType visaType;

    private final List<DocumentType> documents;
    private final int processingDays;

    public VisaRule(
            Country destination,
            Country passportCountry,
            TravelPurpose purpose,
            int maxStayDays,
            boolean visaRequired,
            VisaCategory visaCategory,
            VisaType visaType,
            List<DocumentType> documents,
            int processingDays) {

        this.destination = destination;
        this.passportCountry = passportCountry;
        this.purpose = purpose;
        this.maxStayDays = maxStayDays;
        this.visaRequired = visaRequired;
        this.visaCategory = visaCategory;
        this.visaType = visaType;
        this.documents = Collections.unmodifiableList(new ArrayList<>(documents));
        this.processingDays = processingDays;
    }

    public Country getDestination() { return destination; }
    public Country getPassportCountry() { return passportCountry; }
    public TravelPurpose getPurpose() { return purpose; }
    public int getMaxStayDays() { return maxStayDays; }
    public boolean isVisaRequired() { return visaRequired; }
    public VisaCategory getVisaCategory() { return visaCategory; }
    public VisaType getVisaType() { return visaType; }
    public List<DocumentType> getDocuments() { return documents; }
    public int getProcessingDays() { return processingDays; }
}
