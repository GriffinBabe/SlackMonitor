package eu.bestbrusselsulb.model.html;

import com.sun.javafx.scene.shape.MeshHelper;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class MessageRendererTest extends TestCase {

    private String testMessage = "Hello I'm *Julia* from _BESTorino_! :smile: :+1: :flag-it:";
    private String testMessage2 = "Hello I'm *Dimitri* from *Mother Russia*! :flag-ru:";
    private String testMassage3 = "Hello I'm *Yannick* from ~BEST Brussels~ *BEST Brussels ULB*!";

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
        // numbered lists lines are like
        // 1. element one
        // 2. element two
        // 3. element three
        // dotted lists lines are the same with dots instead of numbers
    }

    @Test
    public void testHyperLinks() {
        //TODO implement this test
        // links are like this <link address|message>
    }

    @Test
    public void testQuotes() {
        // TODO implement this test
        // quotes lines starts with &gt (or >) at the beginning of each line;
    }

    @Test
    public void testMentions() {
        // TODO implement this test
        // <ID> are mentions to other users.
    }
}