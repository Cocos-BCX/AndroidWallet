package com.cocos.module_mine.asset_overview;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.base.ItemViewModel;
import com.cocos.library_base.utils.Utils;
import com.cocos.module_mine.R;
import com.cocos.module_mine.entity.PropAssetModel;

/**
 * @author ningkang.guo
 * @Date 2019/2/25
 */
public class PropAssetItemViewModel extends ItemViewModel {


    public PropAssetItemViewModel(@NonNull BaseViewModel viewModel) {
        super(viewModel);
    }

    public ObservableField<PropAssetModel.PropAssetModelBean> entity = new ObservableField<>();
    public ObservableField<String> assetId = new ObservableField<>(Utils.getString(R.string.module_mine_prop_asset_id));
    public ObservableField<String> worldView = new ObservableField<>(Utils.getString(R.string.module_mine_prop_asset_word_view));
    public ObservableField<String> assetQualifier = new ObservableField<>(Utils.getString(R.string.module_mine_prop_asset_asset_qualifier));

    public PropAssetItemViewModel(@NonNull PropAssetViewModel viewModel, PropAssetModel.PropAssetModelBean entity) {
        super(viewModel);
        this.entity.set(entity);

    }

}
