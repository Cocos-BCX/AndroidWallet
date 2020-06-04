package com.cocos.bcx_sdk.bcx_wallet.chain;

import java.util.List;

/**
 * @author ningkang.guo
 * @Date 2019/10/16
 */
public class vote_object {
    public String id;
    public int next_available_vote_id;
    public List<object_id<committee_object>> active_committee_members;
    public List<object_id<witnesses_object>> active_witnesses;
}
