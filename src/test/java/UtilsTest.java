import Functionalities.Utils;
import database.entities.Author;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UtilsTest {
    @Test
    public void testListToString() {
        List<Author> list = new ArrayList<>();
        Author author1 = new Author();
        author1.setAuthorName("one");
        list.add(author1);
        Author author2 = new Author();
        author2.setAuthorName("two");
        list.add(author2);
        String str = Utils.listToString(list, ',');
        Assert.assertEquals("one,two", str);
    }
}
