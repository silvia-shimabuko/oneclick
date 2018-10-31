package com.liferay.insistencelayer.junit;

import com.liferay.insistencelayer.utils.InsistenceLayerSwitch;
import com.liferay.osgi.util.ServiceTrackerFactory;
import com.liferay.portal.kernel.cache.CacheRegistryUtil;
import com.liferay.portal.kernel.cache.MultiVMPoolUtil;
import com.liferay.portal.kernel.cache.thread.local.Lifecycle;
import com.liferay.portal.kernel.cache.thread.local.ThreadLocalCacheManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.webcache.WebCachePoolUtil;
import java.io.IOException;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.osgi.util.tracker.ServiceTracker;

public class InsistenceLayerRule implements MethodRule {

  public static final String INSISTENCE_LAYER_BACKUP_NAME = "insistencelayerbackup";

  public InsistenceLayerRule(String host, int port) {
    this._insistenceLayerClient = new InsistenceLayerClient(host, port);
  }

  public InsistenceLayerRule(int port) {
    this._insistenceLayerClient = new InsistenceLayerClient(port);
  }

  public InsistenceLayerRule() {
    this._insistenceLayerClient = new InsistenceLayerClient();
  }

  @Override
  public Statement apply(final Statement base, FrameworkMethod method, Object target) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        if (!InsistenceLayerSwitch.INSISTENCE_LAYER_ENABLE) {
          base.evaluate();
          return;
        }

        backupSearchEngineIfFirstRun();
        backupDocumentsLibrary();
        int levelMethod = _insistenceLayerClient.increaseLevel();
        clearMemoryCaches();
        try {
          base.evaluate();
        } catch (Throwable throwable) {
          throwAsUnchecked(throwable);
        } finally {
          _insistenceLayerClient.decreaseToLevel(levelMethod);
          clearMemoryCaches();
          restoreSearchEngine();
          restoreDocumentsLibray();
        }
      }
    };
  }

  public <E extends Throwable> void throwAsUnchecked(Throwable exception) throws E {
    throw (E) exception;
  }

  private void backupDocumentsLibrary() throws PortalException, IOException {
    FileUtil.deltree(backupDocumentsLibraryFolder());
    FileUtil.copyDirectory(documentsLibraryFolder(), backupDocumentsLibraryFolder());
  }

  private String backupDocumentsLibraryFolder() {
    return PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/data/document_library_bkp";
  }

  private void backupSearchEngineIfFirstRun() throws PortalException {
    if (_searchEngine != null) {
      return;
    }
    _searchEngine = getService(SearchEngine.class);
    _searchEngine.removeBackup(TestPropsValues.getCompanyId(), INSISTENCE_LAYER_BACKUP_NAME);
    _searchEngine.backup(TestPropsValues.getCompanyId(), INSISTENCE_LAYER_BACKUP_NAME);
  }

  private void clearMemoryCaches() {
    CacheRegistryUtil.clear();
    MultiVMPoolUtil.clear();
    WebCachePoolUtil.clear();
    ThreadLocalCacheManager.clearAll(Lifecycle.REQUEST);
  }

  private String documentsLibraryFolder() {
    return PropsUtil.get(PropsKeys.LIFERAY_HOME) + "/data/document_library";
  }

  private static <T> T getService(Class<T> klass) {
    ServiceTracker<T, T> serviceTracker = ServiceTrackerFactory.open(klass);

    T service = (T) serviceTracker.getService();

    return service;
  }

  private void restoreDocumentsLibray() throws IOException {
    FileUtil.deltree(documentsLibraryFolder());
    FileUtil.copyDirectory(backupDocumentsLibraryFolder(), documentsLibraryFolder());

  }

  private void restoreSearchEngine() throws PortalException {
    _searchEngine.restore(TestPropsValues.getCompanyId(), INSISTENCE_LAYER_BACKUP_NAME);
  }

  private final InsistenceLayerClient _insistenceLayerClient;

  private SearchEngine _searchEngine;

}
