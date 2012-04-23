package index;

import org.elasticsearch.action.admin.indices.exists.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import play.Logger;

import java.io.IOException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * User: mguillermin
 * Date: 11/04/12
 */
public class IndexUtils {

  private static IndexUtils instance = null;
  public Node node = null;
  public Client client = null;

  private IndexUtils(Node node, Client client) {
    this.node = node;
    this.client = client;
  }

  private void close() {
    if (client != null) {
      client.close();
    }
    if (node != null) {
      node.close();
    }
  }

  public static void initClient(boolean embedded) {
    initClient(embedded, false);
  }

  public static void initClient(boolean embedded, boolean force) {
    if (instance == null || force) {
      Logger.info(String.format("Elastic search client initialisation. [embedded] : %s", embedded));
      Node node = NodeBuilder.nodeBuilder().client(!embedded).node();
      Client client = node.client();
      instance = new IndexUtils(node, client);
      Logger.info("Elastic search client initialisation done.");
    }
  }

  public static void initClient() {
    initClient(false);
  }

  public static void closeClient() {
    if (instance != null) {
      instance.close();
    }
  }
  
  public static IndexUtils get() {
    return instance;
  }

  public void refresh() {
    IndexUtils.get().client.admin().indices().prepareRefresh().execute().actionGet();
  }

  public String index(Document document) throws IOException {
    IndexResponse response = client.prepareIndex(Document.INDEX_NAME, Document.TYPE, document.id)
      .setSource(jsonBuilder()
         .startObject()
           .field("name", document.name)
           .field("category", document.category)
           .field("date", document.date)
         .endObject()
      )
      .execute()
      .actionGet();
    return response.id();
  }

  public void delete(String indexName, String type, String docId) {
    IndexUtils.get().client.prepareDelete(indexName, type, docId).execute().actionGet();
  }

  public boolean clearIndex(String indexName) {
    IndicesExistsResponse existsResponse = client.admin().indices().prepareExists(indexName).execute().actionGet();
    if (existsResponse.exists()) {
      client.admin().indices().prepareDelete(indexName).execute().actionGet();
      return true;
    } else {
      return false;
    }
  }

}
