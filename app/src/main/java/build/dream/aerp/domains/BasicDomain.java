package build.dream.aerp.domains;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class BasicDomain implements Serializable, Cloneable {
    private BigInteger id;
    private Date createdTime;
    private BigInteger createdUserId;
    private Date updatedTime;
    private BigInteger updatedUserId;
    private String updatedRemark;
    private Date deletedTime;
    private boolean deleted;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public BigInteger getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(BigInteger createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public BigInteger getUpdatedUserId() {
        return updatedUserId;
    }

    public void setUpdatedUserId(BigInteger updatedUserId) {
        this.updatedUserId = updatedUserId;
    }

    public String getUpdatedRemark() {
        return updatedRemark;
    }

    public void setUpdatedRemark(String updatedRemark) {
        this.updatedRemark = updatedRemark;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
