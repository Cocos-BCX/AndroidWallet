package com.cocos.bcx_sdk.bcx_wallet.chain;

import com.cocos.bcx_sdk.bcx_log.LogUtils;
import com.cocos.bcx_sdk.bcx_wallet.authority1;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.base_encoder;
import com.cocos.bcx_sdk.bcx_wallet.fc.io.raw_type;
import com.google.common.primitives.UnsignedInteger;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * operations
 */
public class operations {

    public static final int ID_TRANSFER_OPERATION = 0;

    public static final int ID_VOTE_MEMBER = 6;

    public static final int ID_CALCULATE_INVOKING_CONTRACT_OPERATION = 35;

    public static final int ID_REGISTOR_CREATOR_OPERATION = 37;

    public static final int ID_CREATE_WORLDVIEW_OPERATION = 38;

    public static final int ID_CREATE_NH_ASSET_OPERATION = 40;

    public static final int ID_DELETE_NH_ASSET_OPERATION = 41;

    public static final int ID_TRANSFER_NH_ASSET_OPERATION = 42;

    public static final int ID_CREATE_NH_ASSET_ORDER_OPERATION = 43;

    public static final int ID_CANCEL_NH_ASSET_ORDER_OPERATION = 44;

    public static final int ID_BUY_NH_ASSET_OPERATION = 45;

    public static final int ID_UPGRADE_TO_LIFETIME_MEMBER_OPERATION = 7;

    public static final int ID_CREATE_CHILD_ACCOUNT_OPERATION = 5;

    public static final int ID_CREATE_LIMIT_ORDER = 1;

    public static final int ID_CANCEL_LIMIT_ORDER = 2;

    public static final int ID_UPDATE_FEED_PRODUCT = 12;

    public static final int ID_PUBLISH_FEED = 18;

    public static final int ID_ASSET_SETTLE = 16;

    public static final int ID_GLOBAL_ASSET_SETTLE = 17;

    public static final int ID_CREATE_WITNESS = 18;

    public static final int ID_UPDATE_COLLATERAL_FOR_GAS = 54;

    public static final int ID_RECEIVE_VESTING_BALANCES = 27;

    public static final int ID_CREATE_COMMITTEE_MEMBER = 23;

    public static operation_id_map operations_map = new operation_id_map();

    public static class operation_id_map {

        public static HashMap<Integer, Type> mHashId2Operation = new HashMap<>();

        public operation_id_map() {
            mHashId2Operation.put(ID_TRANSFER_OPERATION, transfer_operation.class);
            mHashId2Operation.put(ID_CALCULATE_INVOKING_CONTRACT_OPERATION, invoking_contract_operation.class);
            mHashId2Operation.put(ID_TRANSFER_NH_ASSET_OPERATION, transfer_nhasset_operation.class);
            mHashId2Operation.put(ID_BUY_NH_ASSET_OPERATION, buy_nhasset_operation.class);
            mHashId2Operation.put(ID_UPGRADE_TO_LIFETIME_MEMBER_OPERATION, upgrade_to_lifetime_member_operation.class);
            mHashId2Operation.put(ID_CREATE_CHILD_ACCOUNT_OPERATION, create_child_account_operation.class);
            mHashId2Operation.put(ID_CREATE_NH_ASSET_ORDER_OPERATION, create_nhasset_order_operation.class);
            mHashId2Operation.put(ID_DELETE_NH_ASSET_OPERATION, delete_nhasset_operation.class);
            mHashId2Operation.put(ID_CANCEL_NH_ASSET_ORDER_OPERATION, cancel_nhasset_order_operation.class);
            mHashId2Operation.put(ID_CREATE_LIMIT_ORDER, create_limit_order_operation.class);
            mHashId2Operation.put(ID_CANCEL_LIMIT_ORDER, cancel_limit_order_operation.class);
            mHashId2Operation.put(ID_UPDATE_FEED_PRODUCT, update_feed_product_operation.class);
            mHashId2Operation.put(ID_PUBLISH_FEED, publish_feed_operation.class);
            mHashId2Operation.put(ID_ASSET_SETTLE, asset_settle_operation.class);
            mHashId2Operation.put(ID_GLOBAL_ASSET_SETTLE, global_asset_settle_operation.class);
            mHashId2Operation.put(ID_UPDATE_COLLATERAL_FOR_GAS, update_collateral_for_gas_operation.class);
            mHashId2Operation.put(ID_CREATE_NH_ASSET_OPERATION, create_nhasset_operation.class);
            mHashId2Operation.put(ID_REGISTOR_CREATOR_OPERATION, register_creator_operation.class);
            mHashId2Operation.put(ID_CREATE_WORLDVIEW_OPERATION, create_worldview_operation.class);
            mHashId2Operation.put(ID_RECEIVE_VESTING_BALANCES, receive_vesting_balances_operation.class);
            mHashId2Operation.put(ID_CREATE_COMMITTEE_MEMBER, create_committee_member_operation.class);
            mHashId2Operation.put(ID_CREATE_WITNESS, create_witness_operation.class);

        }

        public static void add_operate(int type) {
            if (type == 1) {
                mHashId2Operation.put(ID_VOTE_MEMBER, vote_members_operation.class);
            } else {
                mHashId2Operation.put(ID_VOTE_MEMBER, modify_password_operation.class);
            }
        }


        public Type getOperationObjectById(int nId) {
            return mHashId2Operation.get(nId);
        }
    }

    public static class operation_type {
        public int nOperationType;
        public Object operationContent;

        public static class operation_type_deserializer implements JsonDeserializer<operation_type> {
            @Override
            public operation_type deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                operation_type operationType = new operation_type();
                JsonArray jsonArray = json.getAsJsonArray();

                operationType.nOperationType = jsonArray.get(0).getAsInt();
                Type type = operations_map.getOperationObjectById(operationType.nOperationType);

                if (type != null) {
                    operationType.operationContent = context.deserialize(jsonArray.get(1), type);
                } else {
                    operationType.operationContent = context.deserialize(jsonArray.get(1), Object.class);
                }

                return operationType;
            }
        }

        public static class operation_type_serializer implements JsonSerializer<operation_type> {
            @Override
            public JsonElement serialize(operation_type src, Type typeOfSrc, JsonSerializationContext context) {
                JsonArray jsonArray = new JsonArray();
                jsonArray.add(src.nOperationType);
                Type type = operations_map.getOperationObjectById(src.nOperationType);
                assert (type != null);
                jsonArray.add(context.serialize(src.operationContent, type));
                return jsonArray;
            }
        }
    }


    public interface base_operation {
        void write_to_encoder(base_encoder baseEncoder);
    }


    /**
     * transfer operation
     */
    public static class transfer_operation implements base_operation {

        public object_id<account_object> from;
        public object_id<account_object> to;
        public asset amount;
        public List<Object> memo;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array((long) from.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedLong.fromLongBits(from.get_instance()));
            baseEncoder.write(rawObject.get_byte_array((long) to.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(to.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(amount.amount));
            baseEncoder.write(rawObject.get_byte_array((long) amount.asset_id.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(amount.asset_id.get_instance()));
            baseEncoder.write(rawObject.get_byte(memo != null));

            if (memo != null) {
//                rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(memo.size()));
                Integer isCrapt = (Integer) memo.get(0);
                rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(isCrapt));
                if (isCrapt == 1) {
                    memo_data memo_data = (memo_data) memo.get(1);
                    baseEncoder.write(memo_data.from.key_data);
                    baseEncoder.write(memo_data.to.key_data);
                    baseEncoder.write(rawObject.get_byte_array(memo_data.nonce));
                    byte[] byteMessage = memo_data.message.array();
                    rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(byteMessage.length));
                    baseEncoder.write(byteMessage);
                } else {
                    String memo_data = (String) memo.get(1);
                    rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(memo_data.getBytes().length));
                    baseEncoder.write(memo_data.getBytes());
                }
            }
//            baseEncoder.write(rawObject.get_byte_array(extensions.size()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }

    }


    /**
     * invoking contract operation
     */
    public static class invoking_contract_operation implements base_operation {


        public static class v {
            public Object v;
        }

        public object_id<account_object> caller;
        public object_id<contract_object> contract_id;
        public String function_name;
        public List<List<Object>> value_list;
        public Set<types.void_t> extensions;


        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(caller.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(contract_id.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(caller.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(contract_id.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(function_name.getBytes().length));
            baseEncoder.write(function_name.getBytes());
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(value_list.size()));
            for (List<Object> value : value_list) {
                rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits((Integer) value.get(0)));
                v paramValue = (invoking_contract_operation.v) value.get(1);
                String type = paramValue.v.getClass().toString();
                switch (type) {
                    case "class java.lang.Integer":
                        Integer integerValue = (Integer) paramValue.v;
                        Long longValue1 = integerValue.longValue();
                        baseEncoder.write(rawObject.get_byte_array(longValue1));
                        break;
                    case "class java.lang.Double":
                        Double doubleValue1 = (Double) paramValue.v;
                        baseEncoder.write(rawObject.get_byte_array(doubleValue1));
                        break;
                    case "class java.lang.Float":
                        Float floatValue = (Float) paramValue.v;
                        Double doubleValue2 = floatValue.doubleValue();
                        baseEncoder.write(rawObject.get_byte_array(doubleValue2));
                        break;
                    case "class java.lang.Boolean":
                        Boolean booleanValues = (Boolean) paramValue.v;
                        baseEncoder.write(rawObject.get_byte(booleanValues));
                        break;
                    case "class java.lang.String":
                        String stringValues = (String) paramValue.v;
                        rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(stringValues.length()));
                        baseEncoder.write(stringValues.getBytes());
                        break;
                    case "class java.lang.Long":
                        Long longValue2 = (Long) paramValue.v;
                        baseEncoder.write(rawObject.get_byte_array(longValue2));
                        break;
                    default:
                        break;
                }
            }
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * register creator operation
     */
    public static class register_creator_operation implements base_operation {

        public object_id<account_object> fee_paying_account;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
        }

    }


    /**
     * register creator operation
     */
    public static class create_worldview_operation implements base_operation {

        public object_id<account_object> fee_paying_account;
        public String world_view;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(world_view.getBytes().length));
            baseEncoder.write(world_view.getBytes());
        }

    }


    /**
     * create_nhasset_operation
     */
    public static class create_nhasset_operation implements base_operation {

        public object_id<account_object> fee_paying_account;
        public object_id<account_object> owner;
        public String asset_id;
        public String world_view;
        public String base_describe;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(owner.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(owner.get_instance()));

            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(asset_id.getBytes().length));
            baseEncoder.write(asset_id.getBytes());
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(world_view.getBytes().length));
            baseEncoder.write(world_view.getBytes());
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(base_describe.getBytes().length));
            baseEncoder.write(base_describe.getBytes());
        }

    }

    /**
     * transfer nhasset operation
     */
    public static class transfer_nhasset_operation implements base_operation {

        public object_id<account_object> from;
        public object_id<account_object> to;
        public object_id<nhasset_object> nh_asset;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(from.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(to.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(nh_asset.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(from.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(to.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(nh_asset.get_instance()));
        }

    }


    /**
     * transfer nhasset operation
     */
    public static class buy_nhasset_operation implements base_operation {

        public object_id<nh_asset_order_object> order;
        public object_id<account_object> fee_paying_account;
        public object_id<account_object> seller;
        public object_id<nhasset_object> nh_asset;
        public String price_amount;
        public object_id<asset_object> price_asset_id;
        public String price_asset_symbol;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(order.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(seller.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(nh_asset.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(order.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(seller.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(nh_asset.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(price_amount.getBytes().length));
            baseEncoder.write(price_amount.getBytes());
            baseEncoder.write(rawObject.get_byte_array(price_asset_id.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(price_asset_id.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(price_asset_symbol.getBytes().length));
            baseEncoder.write(price_asset_symbol.getBytes());
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }

    }


    /**
     * upgrade to lifetime member operation
     */
    public static class upgrade_to_lifetime_member_operation implements base_operation {

        public object_id<account_object> account_to_upgrade;
        public boolean upgrade_to_lifetime_member;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(account_to_upgrade.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(account_to_upgrade.get_instance()));
            baseEncoder.write(rawObject.get_byte(upgrade_to_lifetime_member));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }

    }


    /**
     * create child account operation
     */
    public static class create_child_account_operation implements base_operation {

        public object_id<account_object> registrar;
        public String name;
        public authority1 owner;
        public authority1 active;
        public types.account_options options;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(registrar.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(registrar.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(referrer.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(name.getBytes().length));
            baseEncoder.write(name.getBytes());
            owner.write_to_encode(baseEncoder);
            active.write_to_encode(baseEncoder);
            options.write_to_encode(baseEncoder);
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }

    }


    /**
     * create nhasset order operation
     */
    public static class create_nhasset_order_operation implements base_operation {

        public object_id<account_object> seller;
        public object_id<account_object> otcaccount;
        public asset pending_orders_fee;
        public object_id<nhasset_object> nh_asset;
        public String memo;
        public asset price;
        public Date expiration;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array(seller.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(otcaccount.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(seller.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(otcaccount.get_instance()));
            pending_orders_fee.write_to_encoder(baseEncoder);
            baseEncoder.write(rawObject.get_byte_array(nh_asset.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(nh_asset.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(memo.getBytes().length));
            baseEncoder.write(memo.getBytes());
            LogUtils.i("memoBytes", Arrays.toString(memo.getBytes()));
            price.write_to_encoder(baseEncoder);
            baseEncoder.write(rawObject.get_byte_array(expiration));
        }
    }

    /**
     * delete nhasset operation
     */
    public static class delete_nhasset_operation implements base_operation {

        public object_id<account_object> fee_paying_account;
        public object_id<nhasset_object> nh_asset;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(nh_asset.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(nh_asset.get_instance()));
        }

    }


    /**
     * cancel nhasset order operation
     */
    public static class cancel_nhasset_order_operation implements base_operation {

        public object_id<nh_asset_order_object> order;
        public object_id<account_object> fee_paying_account;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array(order.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(order.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * create limit order operation
     */
    public static class create_limit_order_operation implements base_operation {

        public object_id<account_object> seller;
        public asset amount_to_sell;
        public asset min_to_receive;
        public Date expiration;
        public boolean fill_or_kill;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(seller.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(seller.get_instance()));
            amount_to_sell.write_to_encoder(baseEncoder);
            min_to_receive.write_to_encoder(baseEncoder);
            baseEncoder.write(rawObject.get_byte_array(expiration));
            baseEncoder.write(rawObject.get_byte(fill_or_kill));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * cancel limit order operation
     */
    public static class cancel_limit_order_operation implements base_operation {

        public object_id<account_object> fee_paying_account;
        public object_id<limit_orders_object> order;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array(fee_paying_account.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(order.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(fee_paying_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(order.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * update feed_object product operation
     */
    public static class update_feed_product_operation implements base_operation {

        public object_id<account_object> issuer;
        public object_id<asset_object> asset_to_update;
        public List<object_id<account_object>> new_feed_producers;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(issuer.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(asset_to_update.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(issuer.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(asset_to_update.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(new_feed_producers.size()));
            for (object_id<account_object> produceIds : new_feed_producers) {

                baseEncoder.write(rawObject.get_byte_array(produceIds.get_instance()));
//                rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(produceIds.get_instance()));
            }
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * publish feed operation
     */
    public static class publish_feed_operation implements base_operation {

        public object_id<account_object> publisher;
        public object_id<asset_object> asset_id;
        public feed_object feed;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(publisher.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(asset_id.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(publisher.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(asset_id.get_instance()));
            feed.write_to_encoder(baseEncoder, rawObject);
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }

    /**
     * asset settle operation
     */
    public static class asset_settle_operation implements base_operation {

        public object_id<account_object> account;
        public asset amount;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(account.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(account.get_instance()));
            amount.write_to_encoder(baseEncoder);
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * global asset settle operation
     */
    public static class global_asset_settle_operation implements base_operation {

        public object_id<account_object> issuer;
        public object_id<asset_object> asset_to_settle;
        public settle_price_object settle_price;
        public Set<types.void_t> extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(issuer.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(asset_to_settle.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(issuer.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(asset_to_settle.get_instance()));
            settle_price.write_to_encoder(baseEncoder);
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }

    /**
     * update_collateral_for_gas
     */
    public static class update_collateral_for_gas_operation implements base_operation {

        public object_id<account_object> mortgager;
        public object_id<account_object> beneficiary;
        public long collateral;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array(mortgager.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(beneficiary.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(mortgager.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(beneficiary.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(collateral));
        }
    }

    /**
     * receive_vesting_balances_operation
     */
    public static class receive_vesting_balances_operation implements base_operation {

        public object_id<vesting_balances_object> vesting_balance;
        public object_id<account_object> owner;
        public asset amount;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(vesting_balance.get_instance()));
            baseEncoder.write(rawObject.get_byte_array(owner.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(vesting_balance.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(owner.get_instance()));
            amount.write_to_encoder(baseEncoder);
        }
    }

    /**
     * vote_members_operation
     */
    public static class vote_members_operation implements base_operation {

        public List<Object> lock_with_vote;
        public object_id<account_object> account;
        public authority owner;
        public authority active;
        public types.account_options new_options;
        public HashMap extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte(lock_with_vote.size() > 0));
            baseEncoder.write(rawObject.get_byte_array((Integer) lock_with_vote.get(0)));
            ((asset) lock_with_vote.get(1)).write_to_encoder(baseEncoder);
            baseEncoder.write(rawObject.get_byte_array(account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(account.get_instance()));
            baseEncoder.write(rawObject.get_byte(owner != null));
            if (owner != null) {
                owner.write_to_encode(baseEncoder);
            }
            baseEncoder.write(rawObject.get_byte(active != null));
            if (active != null) {
                active.write_to_encode(baseEncoder);
            }
            baseEncoder.write(rawObject.get_byte(new_options != null));
            if (new_options != null) {
                new_options.write_to_encode(baseEncoder);
            }
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }


    /**
     * create_committee_member_operation
     */
    public static class create_committee_member_operation implements base_operation {

        public object_id<account_object> committee_member_account;
        public String url;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();

            baseEncoder.write(rawObject.get_byte_array(committee_member_account.get_instance()));

//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(committee_member_account.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(url.getBytes().length));
            baseEncoder.write(url.getBytes());
        }
    }


    /**
     * create_committee_member_operation
     */
    public static class create_witness_operation implements base_operation {

        public object_id<account_object> witness_account;
        public String url;
        public types.public_key_type block_signing_key;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte_array(witness_account.get_instance()));
//            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(witness_account.get_instance()));
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(url.getBytes().length));
            baseEncoder.write(url.getBytes());
            baseEncoder.write(block_signing_key.key_data);
        }
    }


    /**
     * modify_password_operation
     */
    public static class modify_password_operation implements base_operation {
        public object_id<account_object> account;
        public authority1 owner;
        public authority1 active;
        public types.account_options new_options;
        public HashSet extensions;

        @Override
        public void write_to_encoder(base_encoder baseEncoder) {
            raw_type rawObject = new raw_type();
            baseEncoder.write(rawObject.get_byte(false));
            baseEncoder.write(rawObject.get_byte_array((long) account.get_instance()));
            LogUtils.i("modify_password---account", Arrays.toString(rawObject.get_byte_array((long) account.get_instance())));
            LogUtils.i("modify_password---owner", String.valueOf(rawObject.get_byte(owner != null)));
            baseEncoder.write(rawObject.get_byte(owner != null));
            owner.write_to_encode(baseEncoder);
            LogUtils.i("modify_password---active", String.valueOf(rawObject.get_byte(owner != null)));
            baseEncoder.write(rawObject.get_byte(active != null));
            active.write_to_encode(baseEncoder);
            baseEncoder.write(rawObject.get_byte(new_options != null));
            LogUtils.i("modify_password---new_options", String.valueOf(rawObject.get_byte(new_options != null)));
            if (new_options != null) {
                new_options.write_to_encode(baseEncoder);
            }
            rawObject.pack(baseEncoder, UnsignedInteger.fromIntBits(extensions.size()));
        }
    }

}
