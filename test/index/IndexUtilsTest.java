package index;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import java.io.IOException;

/**
 * User: mguillermin
 * Date: 23/04/12
 */
public class IndexUtilsTest {

  @Test
  public void riverListTest() throws IOException {
    IndexUtils.initClient(true);

    IndexUtils.get().clearIndex(Document.INDEX_NAME);

    Document document = Document.randomDocument();
    String docId = IndexUtils.get().index(document);
    IndexUtils.get().refresh();

    SearchResponse searchResponse = IndexUtils.get().client.prepareSearch(Document.INDEX_NAME)
        .setQuery(QueryBuilders.fieldQuery("category", "+" + document.category.toString()))
        .execute()
        .actionGet();
    assertThat(searchResponse.getHits().totalHits()).isEqualTo(1);

    IndexUtils.get().delete(Document.INDEX_NAME, Document.TYPE, docId);
    IndexUtils.get().refresh();

    SearchResponse searchResponse2 = IndexUtils.get().client.prepareSearch(Document.INDEX_NAME)
        .setQuery(QueryBuilders.fieldQuery("category", "+" + document.category.toString()))
        .execute()
        .actionGet();
    assertThat(searchResponse2.getHits().totalHits()).isEqualTo(0);

  }
}
