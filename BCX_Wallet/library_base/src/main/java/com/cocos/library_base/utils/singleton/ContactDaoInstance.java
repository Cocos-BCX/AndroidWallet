package com.cocos.library_base.utils.singleton;

import com.cocos.library_base.sql.dao.ContactDao;

/**
 * BclSDK单列对象类
 *
 * @author ningkang
 */

public class ContactDaoInstance {

    private static class ContactDaoInstanceHolder {
        static final ContactDao INSTANCE = new ContactDao();
    }

    public static ContactDao getContactDaoInstance() {
        return ContactDaoInstanceHolder.INSTANCE;
    }

}
