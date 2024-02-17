package com.akp.vtalkanoop.Home;

public class DashbordAdapter {

    private String accountId,name,ProfileImage;

    public DashbordAdapter(String accountId, String name,String ProfileImage) {
        this.accountId=accountId;
        this.name=name;
        this.ProfileImage=ProfileImage;
    }

    public String getAccountId()
    {
        return accountId;
    }
    public String getName()
    {
        return name;
    }
    public String getProfileImage()
    {
        return ProfileImage;
    }

}
