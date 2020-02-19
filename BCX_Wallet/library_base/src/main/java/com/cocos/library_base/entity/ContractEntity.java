package com.cocos.library_base.entity;

public class ContractEntity extends BaseResult {

    public ContractBean data;

  public class ContractBean {
      public boolean check_contract_authority;
      public String contract_authority;
      public String current_version;
      public String name;
      public String owner;
      public String lua_code_b_id;
      public String id;
      public String creation_date;
   }

}
