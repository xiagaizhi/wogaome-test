package ${packageName};

import android.view.View;

import ${PackageId}.R;
import com.yufan.library.base.BaseVu;
import com.yufan.library.inject.FindLayout;
import com.yufan.library.inject.FindView;
import com.yufan.library.inject.Title;
import com.yufan.library.widget.StateLayout;
import com.yufan.library.widget.AppToolbar;
/**
* Created by mengfantao on 18/8/2.
*
*/
@FindLayout(layout = R.layout.${fragment_layout})
@Title("${TitleName}")
public class ${VuName} extends BaseVu <${ContractName}.Presenter>implements ${ContractName}.View{
@Override
public void initView(View view) {

}
@Override
public boolean initTitle(AppToolbar appToolbar) {
return super.initTitle(appToolbar);
}

@Override
public void initStatusLayout(StateLayout stateLayout) {
super.initStatusLayout(stateLayout);
}
}
