package winter.flow.validation;

/**
 * Created by Tzachi on 04/01/2015.
 *
 */
public enum SimpleExpressionSplitters implements ExpressionToAssignmentTranslator {

    // format of : <variable> != null
    CONTAINS("!=", "=") {
        @Override
        public String translate(String input) {
            String[] split = input.split(getSplitter());
            if (!"null".equals(split[1].trim().toLowerCase())){
                throw new RuntimeException("Unknown Translation");
            }
            String assignmentValue = " 'testValue'";
            return split[0] + getTranslatesTo() + assignmentValue;
        }
    };

    private final String splitter;
    private final String translatesTo;

    SimpleExpressionSplitters(String s, String s1) {
        this.splitter = s;
        this.translatesTo = s1;
    }

    public String getSplitter() {
        return splitter;
    }

    public String getTranslatesTo() {
        return translatesTo;
    }
}
