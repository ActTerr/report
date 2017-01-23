package mac.yk.report.model.util;

/**
 * Created by mac-yk on 2017/1/23.
 */

public interface HttpCallbackListener {
    void finish(String response);
    void error(Exception e);
}
