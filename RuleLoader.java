import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleLoader {

    private static final String DEFAULT_CONFIG_PATH = "rules.json";

    public List<VisaRule> loadRules() {
        return loadRules(DEFAULT_CONFIG_PATH);
    }

    public List<VisaRule> loadRules(String configPath) {
        try {
            String json = new String(
                    Files.readAllBytes(Paths.get(configPath)),
                    StandardCharsets.UTF_8
            ).trim();

            if (json.isEmpty()) {
                return Collections.emptyList();
            }

            List<String> objectStrings = splitTopLevelObjects(json);
            List<VisaRule> parsedRules = new ArrayList<>();

            for (String obj : objectStrings) {
                VisaRule rule = parseRule(obj);
                if (rule != null) {
                    parsedRules.add(rule);
                }
            }

            return parsedRules;
        } catch (IOException e) {
            System.err.println("Failed to read rules config: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private List<String> splitTopLevelObjects(String json) {
        String trimmed = json.trim();
        if (trimmed.startsWith("[")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("]")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }

        List<String> result = new ArrayList<>();
        int depth = 0;
        int start = -1;

        for (int i = 0; i < trimmed.length(); i++) {
            char c = trimmed.charAt(i);
            if (c == '{') {
                if (depth == 0) {
                    start = i;
                }
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && start >= 0) {
                    result.add(trimmed.substring(start, i + 1));
                    start = -1;
                }
            }
        }

        return result;
    }

    private VisaRule parseRule(String obj) {
        try {
            String destinationStr = extractString(obj, "destination");
            String passportStr = extractString(obj, "passportCountry");
            String purposeStr = extractString(obj, "purpose");

            String maxStayStr = extractNumber(obj, "maxStayDays");
            String visaRequiredStr = extractBoolean(obj, "visaRequired");
            String visaCategoryStr = extractString(obj, "visaCategory");
            String visaTypeStr = extractString(obj, "visaType");
            String processingDaysStr = extractNumber(obj, "processingDays");

            List<String> docsStr = extractStringArray(obj, "documents");

            if (destinationStr == null || passportStr == null || purposeStr == null ||
                    maxStayStr == null || visaRequiredStr == null ||
                    visaCategoryStr == null || visaTypeStr == null ||
                    processingDaysStr == null) {
                System.err.println("Skipping rule with missing required fields: " + obj);
                return null;
            }

            Country destination = Country.valueOf(destinationStr);
            Country passportCountry = Country.valueOf(passportStr);
            TravelPurpose purpose = TravelPurpose.valueOf(purposeStr);

            int maxStayDays = Integer.parseInt(maxStayStr);
            boolean visaRequired = Boolean.parseBoolean(visaRequiredStr);
            VisaCategory visaCategory = VisaCategory.valueOf(visaCategoryStr);
            VisaType visaType = VisaType.valueOf(visaTypeStr);
            int processingDays = Integer.parseInt(processingDaysStr);

            List<DocumentType> documents = new ArrayList<>();
            for (String d : docsStr) {
                if (d != null && !d.isEmpty()) {
                    documents.add(DocumentType.valueOf(d));
                }
            }

            return new VisaRule(
                    destination,
                    passportCountry,
                    purpose,
                    maxStayDays,
                    visaRequired,
                    visaCategory,
                    visaType,
                    documents,
                    processingDays
            );
        } catch (IllegalArgumentException e) {
            System.err.println("Skipping invalid rule entry: " + e.getMessage());
            return null;
        }
    }

    private String extractString(String obj, String field) {
        String pattern = "\"" + field + "\"";
        int idx = obj.indexOf(pattern);
        if (idx < 0) return null;

        int colon = obj.indexOf(":", idx + pattern.length());
        if (colon < 0) return null;

        int firstQuote = obj.indexOf("\"", colon + 1);
        if (firstQuote < 0) return null;
        int secondQuote = obj.indexOf("\"", firstQuote + 1);
        if (secondQuote < 0) return null;

        return obj.substring(firstQuote + 1, secondQuote).trim();
    }

    private String extractNumber(String obj, String field) {
        String pattern = "\"" + field + "\"";
        int idx = obj.indexOf(pattern);
        if (idx < 0) return null;

        int colon = obj.indexOf(":", idx + pattern.length());
        if (colon < 0) return null;

        int start = colon + 1;
        while (start < obj.length() && Character.isWhitespace(obj.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < obj.length() &&
                (Character.isDigit(obj.charAt(end)) || obj.charAt(end) == '-')) {
            end++;
        }
        if (start == end) return null;
        return obj.substring(start, end).trim();
    }

    private String extractBoolean(String obj, String field) {
        String pattern = "\"" + field + "\"";
        int idx = obj.indexOf(pattern);
        if (idx < 0) return null;

        int colon = obj.indexOf(":", idx + pattern.length());
        if (colon < 0) return null;

        int start = colon + 1;
        while (start < obj.length() && Character.isWhitespace(obj.charAt(start))) {
            start++;
        }
        int end = start;
        while (end < obj.length() &&
                (Character.isLetter(obj.charAt(end)))) {
            end++;
        }
        if (start == end) return null;
        return obj.substring(start, end).trim();
    }

    private List<String> extractStringArray(String obj, String field) {
        String pattern = "\"" + field + "\"";
        int idx = obj.indexOf(pattern);
        if (idx < 0) return Collections.emptyList();

        int colon = obj.indexOf(":", idx + pattern.length());
        if (colon < 0) return Collections.emptyList();

        int openBracket = obj.indexOf("[", colon + 1);
        if (openBracket < 0) return Collections.emptyList();

        int closeBracket = obj.indexOf("]", openBracket + 1);
        if (closeBracket < 0) return Collections.emptyList();

        String inside = obj.substring(openBracket + 1, closeBracket).trim();
        if (inside.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        String[] parts = inside.split(",");
        for (String p : parts) {
            String s = p.trim();
            if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
                s = s.substring(1, s.length() - 1);
            }
            if (!s.isEmpty()) {
                result.add(s);
            }
        }
        return result;
    }
}

