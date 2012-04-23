package index;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import java.io.IOException;

/**
 * IndexUtils class tests
 */
public class IndexUtilsTest {

  @BeforeClass
  public static void before() {
    IndexUtils.initClient(true);
  }

  @AfterClass
  public static void after() {
    IndexUtils.closeClient();
  }

  @Test
  public void initIndex() {
    assertThat(IndexUtils.get()).isNotNull();
    assertThat(IndexUtils.get().client).isNotNull();
  }

  @Test
  public void simpleIndexation() throws IOException {
    IndexUtils.get().clearIndex(Document.INDEX_NAME);

    Document document = Document.randomDocument();
    String docId = IndexUtils.get().index(document);
    IndexUtils.get().refresh();

    SearchResponse searchResponse = IndexUtils.get().client.prepareSearch(Document.INDEX_NAME)
        .setQuery(QueryBuilders.fieldQuery("category", "+" + document.category.toString()))
        .execute()
        .actionGet();
    assertThat(searchResponse.getHits().totalHits()).isEqualTo(1);
  }

  @Test
  public void deleteDocument() throws IOException {
    IndexUtils.get().clearIndex(Document.INDEX_NAME);

    Document document = Document.randomDocument();
    String docId = IndexUtils.get().index(document);
    IndexUtils.get().refresh();

    IndexUtils.get().delete(Document.INDEX_NAME, Document.TYPE, docId);
    IndexUtils.get().refresh();

    SearchResponse searchResponse2 = IndexUtils.get().client.prepareSearch(Document.INDEX_NAME)
        .setQuery(QueryBuilders.fieldQuery("category", "+" + document.category.toString()))
        .execute()
        .actionGet();
    assertThat(searchResponse2.getHits().totalHits()).isEqualTo(0);
  }
}
