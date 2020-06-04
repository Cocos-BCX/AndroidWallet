package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/6/6
 */
public class contract_callback {

    public int code;
    public TrxDataBean trx_data;
    public List<DataBean> data;

    public static class TrxDataBean {
        public String trx_id;
        public long block_num;
    }

    public static class DataBean {
        public object_id<contract_object> contract_id;
        public Long real_running_time;
        public boolean existed_pv;
        public String process_value;
        public asset_fee_object additional_cost;
        public List<ContractAffectedsBean> contract_affecteds;

        public static class ContractAffectedsBean {
            public Long block_num;
            public String type;
            public RawDataBean raw_data;
            public String type_name;
            public ResultBean result;
            public String result_text;

            public String getTypeName(double contract_type, Double action) {
                if (contract_type == 0) {
                    return "资产";
                } else if (contract_type == 1) {
                    if (action == 0) {
                        return "NH资产转出";
                    } else if (action == 1) {
                        return "NH资产转入";
                    } else if (action == 2) {
                        return "NH资产数据修改";
                    } else if (action == 3) {
                        return "NH资产创建人";
                    } else if (action == 4) {
                        return "NH资产创建给";
                    }
                } else if (contract_type == 2) {
                    return "备注";
                } else if (contract_type == 3) {
                    return "日志";
                }
                return "";
            }

            public String getType(double contract_type, Double action) {
                if (contract_type == 0) {
                    return "contract_affecteds_asset";
                } else if (contract_type == 1) {
                    if (action == 0) {
                        return "contract_affecteds_nh_transfer_from";
                    } else if (action == 1) {
                        return "contract_affecteds_nh_transfer_to";
                    } else if (action == 2) {
                        return "contract_affecteds_nh_modifined";
                    } else if (action == 3) {
                        return "contract_affecteds_nh_create_by";
                    } else if (action == 4) {
                        return "contract_affecteds_nh_create_for";
                    }
                } else if (contract_type == 2) {
                    return "contract_affecteds_memo";
                } else if (contract_type == 3) {
                    return "contract_affecteds_log";
                }
                return "";
            }

            public ResultBean getResult() {
                return result;
            }

            public void setResult(ResultBean result) {
                this.result = result;
            }

            public String getResult_text() {
                return result.affected_account + " " + result.aseet_amount;
            }


            public String getResult_message() {
                return result.affected_account + " " + result.message;
            }


            public static class RawDataBean {
                public String affected_account;
                public AffectedAssetBean affected_asset;
                public String affected_item;
                public Double action;
                public String message;
                public List<String> modified;

                public static class AffectedAssetBean {
                    public Double amount;
                    public String asset_id;
                }
            }

            public static class ResultBean {
                public String affected_account;
                public String aseet_amount;
                public String message;
                public String affected_item;
                public String modified;
            }
        }
    }
}
