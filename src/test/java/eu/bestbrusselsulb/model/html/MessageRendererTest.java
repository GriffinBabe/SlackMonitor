package eu.bestbrusselsulb.model.html;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class MessageRendererTest extends TestCase {

    /**
     * Some examples of html generated code obtained
     * by converting slack tags by html tags.
     */
    @Test
    public void testFindPairsAndReplace() {
        final String testMessage = "*a* _bac_ ~carotte~ `snakeCase`\n" +
                "```for i in range(3):\n" +
                "    print(\"hoho\")```";
        final String expected = "<b>a</b> <i>bac</i> <strike>carotte</strike> <div class=\"code\">snakeCase</div>\n" +
                "<div class=\"block-code\">for i in range(3):\n" +
                "    print(\"hoho\")</div>";
        try {
            String replacedBold = MessageRenderer.findPairsAndReplace(testMessage, "*", "<b>", "</b>");
            String replaceBoldAndItalic = MessageRenderer.findPairsAndReplace(replacedBold, "_", "<i>", "</i>");
            String replaceBoldAndItalicAndStrike = MessageRenderer.findPairsAndReplace(replaceBoldAndItalic, "~", "<strike>", "</strike>");
            String replaceBoldItalicStrikeBCode = MessageRenderer.findPairsAndReplace(replaceBoldAndItalicAndStrike, "```", "<div class=\"block-code\">", "</div>");
            String replaceBoldItalicStrikeBCodeCode = MessageRenderer.findPairsAndReplace(replaceBoldItalicStrikeBCode, "`", "<div class=\"code\">", "</div>");

            Assert.assertEquals(replaceBoldItalicStrikeBCodeCode, expected);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testLists() {
        //TODO implement this test
        // dotted lists lines are the same with dots instead of numbers
        final String testMessage =
                "This is a message with a list: \n" +
                        "1. Elements one\n" +
                        "2. Element two\n" +
                        "No element left, going to another list: \n" +
                        "1. Element one list two\n" +
                        "2. Element two list two\n";
        final String expected =
                "This is a message with a list: \n" +
                        "<div class=\"list\">1. Elements one\n" +
                        "2. Element two</div>\n" +
                        "No element left, going to another list: \n" +
                        "<div class=\"list\">1. Element one list two\n" +
                        "2. Element two list two</div>\n";
        try {
            String replaceList = MessageRenderer.findListsAndQuotes(testMessage, "^\\d{1,}\\.", "<div class=\"list\">", "</div>");

            Assert.assertEquals(replaceList, expected);
        } catch (StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testHyperLinks() {
        final String testMessage = "Hello man! Link <http://youtube.com|here>!";
        final String expectedMessage = "Hello man! Link <a href=\"http://youtube.com\">here</a>!";
        final String replaceMessage = MessageRenderer.formatLinks(testMessage);
        Assert.assertEquals(replaceMessage, expectedMessage);
    }

    @Test
    public void testEmojis() {
        final String testMessage = "Hello man! :smile: \n" +
                "Hello:smile:\n" +
                "Hello :smi le:\n" +
                "haha :smirk:";
        String formatted = MessageRenderer.formatEmojis(testMessage);
        System.out.println(formatted);
    }

}