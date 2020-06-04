package com.cocos.bcx_sdk.bcx_wallet.chain;


public class asset_dynamic_data_object {

    long current_supply;
    long confidential_supply; ///< total asset held in confidential balances
    long accumulated_fees; ///< fees accumulate to be paid out over time
    long fee_pool;         ///< in core asset
}
