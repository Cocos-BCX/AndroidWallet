package com.cocos.library_base.viewmodel;

import android.app.Application;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import com.cocos.bcx_sdk.bcx_api.CocosBcxApiWrapper;
import com.cocos.bcx_sdk.bcx_callback.IBcxCallBack;
import com.cocos.library_base.base.BaseViewModel;
import com.cocos.library_base.entity.ContractCodeEntity;
import com.cocos.library_base.entity.ContractDataEntity;
import com.cocos.library_base.entity.ContractEntity;
import com.cocos.library_base.invokedpages.model.Contract;
import com.cocos.library_base.utils.singleton.GsonSingleInstance;
import com.cocos.library_base.utils.singleton.MainHandler;

public class ContractDetailViewModel extends BaseViewModel {

    private Contract contract;

    public ContractDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public ObservableField<String> contractDetail = new ObservableField<>();

    public void getContract(){
        CocosBcxApiWrapper.getBcxInstance().get_contract(contract.getContractNameOrId(), new IBcxCallBack() {
            @Override
            public void onReceiveValue(String value) {
                ContractEntity contractEntity = GsonSingleInstance.getGsonInstance().fromJson(value, ContractEntity.class);
                Log.i("current_version",contractEntity.data.current_version);
                if (contractEntity.isSuccess()){
                    CocosBcxApiWrapper.getBcxInstance().get_transaction_by_id(contractEntity.data.current_version, new IBcxCallBack() {
                        @Override
                        public void onReceiveValue(String value) {
                            MainHandler.getInstance().post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Log.i("get_transaction_by_id",value);
                                        ContractDataEntity contractDataEntity = GsonSingleInstance.getGsonInstance().fromJson(value, ContractDataEntity.class);
                                        Object o = contractDataEntity.data.operations.get(0).get(1);
                                        ContractCodeEntity contractCodeEntity = GsonSingleInstance.getGsonInstance().fromJson(GsonSingleInstance.getGsonInstance().toJson(o), ContractCodeEntity.class);
                                        contractDetail.set(contractCodeEntity.data);
                                    }catch (Exception e){
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void setContractModel(Contract contract) {
        this.contract = contract;
    }
}
