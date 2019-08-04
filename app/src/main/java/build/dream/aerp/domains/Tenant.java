package build.dream.aerp.domains;


import java.math.BigInteger;

public class Tenant extends BasicDomain {
    /**
     * 商户编码
     */
    private String code;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 业态，1-餐饮，2-零售
     */
    private String business;
    /**
     * 省编码
     */
    private String provinceCode;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 区编码
     */
    private String districtCode;
    /**
     * 区名称
     */
    private String districtName;
    /**
     * 门店详细地址
     */
    private String address;
    /**
     * 联系人
     */
    private String linkman;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 分区码
     */
    private String partitionCode;
    /**
     * 商户类型，1-标准版商户，2-单机版商户
     */
    private Integer tenantType;
    /**
     * 会员共享类型，1-全部共享，2-全部独立，3-分组共享
     */
    private Integer vipSharedType;
    /**
     * 代理商ID
     */
    private BigInteger agentId;
    /**
     * 商户使用的支付通道类型，0-原生支付，3-米雅，4-新大陆，5-联动
     */
    private Integer usedChannelType;
    /**
     * 达达商户ID
     */
    private BigInteger dadaSourceId;

    /**
     * 京东到家商家ID
     */
    private String jddjVenderId;

    /**
     * 京东到家授权应用app key
     */
    private String jddjAppKey;

    /**
     * 京东到家授权应用app secret
     */
    private String jddjAppSecret;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPartitionCode() {
        return partitionCode;
    }

    public void setPartitionCode(String partitionCode) {
        this.partitionCode = partitionCode;
    }

    public Integer getTenantType() {
        return tenantType;
    }

    public void setTenantType(Integer tenantType) {
        this.tenantType = tenantType;
    }

    public Integer getVipSharedType() {
        return vipSharedType;
    }

    public void setVipSharedType(Integer vipSharedType) {
        this.vipSharedType = vipSharedType;
    }

    public BigInteger getAgentId() {
        return agentId;
    }

    public void setAgentId(BigInteger agentId) {
        this.agentId = agentId;
    }

    public Integer getUsedChannelType() {
        return usedChannelType;
    }

    public void setUsedChannelType(Integer usedChannelType) {
        this.usedChannelType = usedChannelType;
    }

    public BigInteger getDadaSourceId() {
        return dadaSourceId;
    }

    public void setDadaSourceId(BigInteger dadaSourceId) {
        this.dadaSourceId = dadaSourceId;
    }

    public String getJddjVenderId() {
        return jddjVenderId;
    }

    public void setJddjVenderId(String jddjVenderId) {
        this.jddjVenderId = jddjVenderId;
    }

    public String getJddjAppKey() {
        return jddjAppKey;
    }

    public void setJddjAppKey(String jddjAppKey) {
        this.jddjAppKey = jddjAppKey;
    }

    public String getJddjAppSecret() {
        return jddjAppSecret;
    }

    public void setJddjAppSecret(String jddjAppSecret) {
        this.jddjAppSecret = jddjAppSecret;
    }
}
