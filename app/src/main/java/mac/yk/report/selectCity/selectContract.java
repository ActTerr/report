package mac.yk.report.selectCity;

import java.util.List;

import mac.yk.report.base.BasePresenter;
import mac.yk.report.base.BaseView;

/**
 * Created by mac-yk on 2017/2/3.
 */

public interface selectContract {
    interface View extends BaseView<Presenter> {

        void closeProgressDialog();

        void showProgressDialog();

        void showError(Exception e);

        void setSelection(int i);

        void refresh(List<String> list);

        void setText(String s);

        void gotoWeatherDetail(String s);

    }

    interface Presenter extends BasePresenter {
        void queryCities();

        void queryCounties();

        void queryProvinces();

        void query(int position);

        int getCurrentLevel();
    }

}
