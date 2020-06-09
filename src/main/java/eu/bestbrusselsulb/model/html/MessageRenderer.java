package eu.bestbrusselsulb.model.html;

import eu.bestbrusselsulb.model.SlackMonitor;

import javax.swing.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        html = formatEmojis(message);
        html = findPairsAndReplace(html, "*", "<b>", "</b>");
        html = findPairsAndReplace(html, "_", "<i>", "</i>");
        html = findPairsAndReplace(html, "~", "<strike>", "</strike>");
        html = findPairsAndReplace(html, "```", "<div class=\"block-code\">", "</div>");
        html = findPairsAndReplace(html, "`", "<div class=\"code\">", "</div>");
        html = findListsAndQuotes(html, "^(\\d{1,}\\.|•)", "<div class=\"list\">", "</div>");
        html = findListsAndQuotes(html, "^(&gt|>).*", "<div class=\"quote\"", "</dib>");
        html = formatLinks(html);

        this.htmlCode = html;
        return this.htmlCode;
    }

    public String getHtmlCode() {
        return this.htmlCode;
    }

    /**
     * Uses regex to replace the slack formatted hyperlinks
     * to html formatted hyperlinks.
     * {@literal <http://youtube.com|my video!>} becomes {@literal <a href="http://youtube.com">my video!</a>}
     * @param source the string were we want to convert the link
     * @return the string with the converted link.
     */
    static String formatLinks(String source) {
        return source.replace("<(.*)\\|(.*)>", "<a href=\"$1\">$2</a>");
    }

    /**
     * Uses regex to find slack formatted emojis and
     * replaces them in the string with their respective
     * decimal value. Gathers the corresponsing name in
     * the {@link EmojiData} class.
     *
     * @param source
     * @return
     */
    static String formatEmojis(String source) {
        final String emojiRegex = ":([^ ,:]*):";
        Pattern pattern = Pattern.compile(emojiRegex);
        Matcher matcher = pattern.matcher(source);

        EmojiDatabase db = EmojiDatabase.getInstance();

        // TODO Must fix this
        matcher.replaceAll(matchResult ->{
            String group = matchResult.group().replace(":", "");
            String decimal = db.getDecimalEmoji(group); // get decimal value from name
            if (decimal == null) return  matchResult.group();
            else return String.format("&#%s", decimal);
        });
        return null;
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
            boolean regexMatch = regexContains(lines[l], pattern);
            if (regexMatch) {
                if (!matched) firstMatchLine = l;
                matched = true;
            }
            if ((!regexMatch && matched) || (regexMatch && l == lines.length - 1)) {
                // no match but previous line was matched.
                // this index is at the end of the last line
                int indexEnd = findNthLineIndex(builder.toString(), (l == lines.length - 1) ? l + 1: l) - 1;
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
     * Checks if there is at least one occurrence of the Regular
     * expression in the parameter text.
     * @param text the text where we want to know if there is a match or not
     * @param pattern the regular expression
     * @return true if there is a match or more, false otherwhise.
     */
    private static boolean regexContains(String text, String pattern) {
        return Pattern.compile(pattern).matcher(text).find();
    }

    /**
     * Finds the index of the beginning of the {@code line}'th line.
     * @param str the String to where to find.
     * @param line the line number we are interested in.
     * @return the found index.
     */
    private static int findNthLineIndex(String str, int line) {
        String lines[] = str.split("\n");
        if (line > lines.length || line < 0) {
            return -1;
        }
        int index = 0;
        for (int i = 0; i != line; i++) {
            index += lines[i].length() + 1; // +1 for the '\n' character
        }
        return index;
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
