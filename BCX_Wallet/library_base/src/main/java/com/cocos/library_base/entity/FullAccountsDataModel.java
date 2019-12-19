package com.cocos.library_base.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author ningkang.guo
 * @Date 2019/12/19
 */
@Setter
@Getter
public class FullAccountsDataModel extends BaseResult {

    private String data;

    @Setter
    @Getter
    public static class AssetModel {
        public String amount;
        public String asset_id;
        public int precision;
    }

    @Setter
    @Getter
    public class FullAccountModel {

        private AccountBean account;

        @Setter
        @Getter
        public class AccountBean {

            private String id;
            private String membership_expiration_date;
            private String registrar;
            private String name;
            private AssetLockedBean asset_locked;


            @Setter
            @Getter
            public class AssetLockedBean {

                private List<List<String>> locked_total;


            }
        }
    }

}
