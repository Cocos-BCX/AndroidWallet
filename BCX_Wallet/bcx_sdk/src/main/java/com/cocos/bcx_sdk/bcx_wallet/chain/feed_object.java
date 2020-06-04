package com.cocos.bcx_sdk.bcx_wallet.chain;

import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;

import java.math.BigDecimal;

/**
 * @author ningkang.guo
 * @Date 2019/8/29
 */
public class feed_object {

    public SettlementPriceBean settlement_price;
    public BigDecimal maintenance_collateral_ratio;
    public BigDecimal maximum_short_squeeze_ratio;
    public CoreExchangeRateBean core_exchange_rate;


    public static class SettlementPriceBean {
        public asset base;
        public asset quote;
    }

    public static class CoreExchangeRateBean {
        public asset base;
        public asset quote;
    }


    public void write_to_encoder(base_encoder baseEncoder, raw_type rawObject) {
        settlement_price.base.write_to_encoder(baseEncoder);
        settlement_price.quote.write_to_encoder(baseEncoder);
        baseEncoder.write(rawObject.get_byte_array(maintenance_collateral_ratio.shortValue()));
        baseEncoder.write(rawObject.get_byte_array(maximum_short_squeeze_ratio.shortValue()));
        core_exchange_rate.base.write_to_encoder(baseEncoder);
        core_exchange_rate.quote.write_to_encoder(baseEncoder);
    }
}
