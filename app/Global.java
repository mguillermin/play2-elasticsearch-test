import index.IndexUtils;
import play.Application;
import play.GlobalSettings;
import play.Logger;

/**
 * Global class for the application
 */
public class Global extends GlobalSettings {
  /**
   * Executed when the application starts.
   * @param application
   */
  @Override
  public void onStart(Application application) {
    Logger.info("Application starting...");
    IndexUtils.initClient();
    Logger.info("Application has started");
  }

  /**
   * Executed when the application stops.
   */
  @Override
  public void onStop(Application application) {
    Logger.info("Application shutdown...");
    IndexUtils.closeClient();
    Logger.info("Application has been shutdown");
  }
}
