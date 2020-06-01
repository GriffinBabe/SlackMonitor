package eu.bestbrusselsulb.model.html;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class EmojiDatabaseTest extends TestCase {

    @Test
    public void testDataLoading() {
        EmojiDatabase database = EmojiDatabase.getInstance();
        // good if no error is throwed
        // test some emojis.
        Assert.assertEquals("&#128527", database.getDecimalEmoji(":smirk:"));
        Assert.assertEquals("&#128512", database.getDecimalEmoji(":grinning:"));
    }


}