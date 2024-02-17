package com.akp.vtalkanoop.Home;

public class DashbordDatingAdapter {

    private String accountId,name,image;

    public DashbordDatingAdapter(String accountId, String name,String image) {
        this.accountId=accountId;
        this.name=name;
        this.image=image;
    }

    public String getAccountId()
    {
        return accountId;
    }
    public String getName()
    {
        return name;
    }
    public String getImage()
    {
        return image;
    }
}
