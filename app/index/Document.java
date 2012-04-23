package index;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;

/**
 * User: mguillermin
 * Date: 11/04/12
 */
public class Document {
  public static final String INDEX_NAME = "documents";
  public static final String TYPE = "document";
  
  public String id;
  public String name;
  public Category category;
  public Date date;

  public static List<Category> categories = Arrays.asList(Category.values());

  public static Document randomDocument() {
    Document document = new Document();
    document.id = UUID.randomUUID().toString();
    document.name = RandomStringUtils.randomAlphabetic(RandomUtils.nextInt(30));
    document.category = categories.get(RandomUtils.nextInt(categories.size()));
    Date date = new Date();
    date = DateUtils.setDays(date, RandomUtils.nextInt(29) + 1);
    date = DateUtils.setMonths(date, RandomUtils.nextInt(11) + 1);
    date = DateUtils.setYears(date, 2000 + RandomUtils.nextInt(12) + 1);
    date = DateUtils.ceiling(date, Calendar.DATE);
    document.date = date;
    return document;
  }
  
  public void index() {

  }
}
