package com.plan.data;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by snow on 15-6-12.
 */
@Entity
@javax.persistence.Table(name = "Friend", schema = "", catalog = "Plan")
@javax.persistence.IdClass(com.plan.data.FriendEntityPK.class)
public class FriendEntity {
    private String friendAccount;

    @Id
    @javax.persistence.Column(name = "friendAccount")
    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }

    private String userAccount;

    @Id
    @javax.persistence.Column(name = "userAccount")
    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FriendEntity that = (FriendEntity) o;

        if (friendAccount != null ? !friendAccount.equals(that.friendAccount) : that.friendAccount != null)
            return false;
        if (userAccount != null ? !userAccount.equals(that.userAccount) : that.userAccount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = friendAccount != null ? friendAccount.hashCode() : 0;
        result = 31 * result + (userAccount != null ? userAccount.hashCode() : 0);
        return result;
    }
}
