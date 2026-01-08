import java.util.*;

public final class VisaDecision {

    private final boolean visaRequired;
    private final VisaCategory visaCategory;
    private final VisaType visaType;
    private final List<DocumentType> documents;
    private final int processingDays;
    private final List<String> warnings;

    public VisaDecision(
            boolean visaRequired,
            VisaCategory visaCategory,
            VisaType visaType,
            List<DocumentType> documents,
            int processingDays,
            List<String> warnings) {

        this.visaRequired = visaRequired;
        this.visaCategory = visaCategory;
        this.visaType = visaType;
        this.documents = Collections.unmodifiableList(new ArrayList<>(documents));
        this.processingDays = processingDays;
        this.warnings = Collections.unmodifiableList(new ArrayList<>(warnings));
    }

    public boolean isVisaRequired() { return visaRequired; }
    public VisaCategory getVisaCategory() { return visaCategory; }
    public VisaType getVisaType() { return visaType; }
    public List<DocumentType> getDocuments() { return documents; }
    public int getProcessingDays() { return processingDays; }
    public List<String> getWarnings() { return warnings; }
}
