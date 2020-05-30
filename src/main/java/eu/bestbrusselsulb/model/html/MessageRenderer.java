package eu.bestbrusselsulb.model.html;

import eu.bestbrusselsulb.model.SlackMonitor;

import java.net.URL;
import java.nio.charset.Charset;

/**
 * This class takes care of rendering in html code a slack formatted message
 * in order to be rendered next with WebKit.
 *
 * @author GriffinBabe
 */
public class MessageRenderer {

    private static URL MESSAGE_BASE_PATH = SlackMonitor.class.getResource("/Message.html");

    private static Charset charset = Charset.forName("UTF-8");

    private String message;

    private String htmlCode = null;

    public MessageRenderer(String message) {
        this.message = message;
    }

    /**
     * Generates the HTML code by calling all the necessary
     * sub-functions and returns it.
     * @return the generated html code.
     */
    public String generate() {
        String html;
        // first replaces the common tags
        html = findPairsAndReplace(message, "*", "<b>", "</b>");
        html = findPairsAndReplace(html, "_", "<i>", "</i>");
        html = findPairsAndReplace(html, "~", "<strike>", "</strike>");
        html = findPairsAndReplace(html, "```", "<div class=\"block-code\">", "</div>");
        html = findPairsAndReplace(html, "`", "<div class=\"code\">", "</div>");

        this.htmlCode = html;
        return this.htmlCode;
    }

    public String getHtmlCode() {
        return this.htmlCode;
    }

    /**
     * Similarly to {@link MessageRenderer#findPairsAndReplace(String, String, String, String)}, this will
     * find for occurences of a certain pattern in the source and replace them. Only this is used to
     * format lists and quotes, working per line.
     * @param source where to find and replace the occurences.
     * @param pattern the pattern to find
     * @param topTag the header tag of the list/quote
     * @param bottomTag the end tag
     * @return
     */
    static String findListsAndQuotes(String source, String pattern, String topTag, String bottomTag) {
        StringBuilder builder = new StringBuilder(source);
        String lines[] = builder.toString().split("\\n");
        // looks for a block of lines between two matches
        // a <div> html tag will be applied there.
        boolean matched = false;
        int firstMatchLine = 0;
        for (int l = 0; l < lines.length; l++) {
            if (lines[l].matches(pattern)) {
                if (!matched) firstMatchLine = l;
                matched = true;
            }
            if ((!lines[l].matches(pattern) && l == lines.length - 1) || matched) {
                // no match but previous line was matched.
                // this index is at the end of the last line
                int indexEnd = findNthLineIndex(builder.toString(), l - 1) - 2;
                // this index is at the beginning of the first line.
                int indexBegin = findNthLineIndex(builder.toString(), firstMatchLine);
                builder.insert(indexEnd, bottomTag);
                builder.insert(indexBegin, topTag);

                matched = false;
            }
        }
        return builder.toString();
    }

    /**
     * Finds the index of the beginning of the {@code line}'th line.
     * @param str the String to where to find.
     * @param line the line number we are interested in.
     * @return the found index.
     */
    private static int findNthLineIndex(String str, int line) {
        int lineCount = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '\n') {
                if (++lineCount == line) {
                    return i + 1;
                }
            }
        }
        return -1;
    }

    /**
     * Looks for pairs of pattern occurrences in source
     * and replaces the first one by the leftTag and the second one by
     * the rightTag.
     *
     * This is used to convert a slack formatted message into html code.
     *
     * @param source where to find and replace occurrences.
     * @param pattern the pattern to find
     * @param leftTag the first occurrence replacement
     * @param rightTag the second occurrence replacement
     * @return the string with the replace occurrence pairs.
     */
    static String findPairsAndReplace(String source, String pattern, String leftTag, String rightTag) {
        StringBuilder builder = new StringBuilder(source);
        int indexFrom = 0; // cursor indicating where to start looking
        while (true) {
            int firstOccurrence = builder.indexOf(pattern, indexFrom); // first occurrence
            if (firstOccurrence == -1) { // no first occurrence found. STOP
                break;
            }
            indexFrom = firstOccurrence + pattern.length(); // changes the starting point
            int secondOccurrence = builder.indexOf(pattern, indexFrom);
            if (secondOccurrence == -1 && (secondOccurrence - firstOccurrence) > 1) { // no second occurrence found. STOP
                break;
            }
            builder.delete(secondOccurrence, secondOccurrence + pattern.length()); // replaces the patterns by the html tags
            builder.delete(firstOccurrence, firstOccurrence + pattern.length());
            builder.insert(firstOccurrence, leftTag);
            builder.insert(secondOccurrence + leftTag.length() - pattern.length(), rightTag);
            indexFrom = secondOccurrence;
        }
        return builder.toString();
    }
}
