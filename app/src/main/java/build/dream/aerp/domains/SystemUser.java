package build.dream.aerp.domains;

public class SystemUser extends BasicDomain {
    public static final String TABLE_NAME = "system_user";
    /**
     * 员工姓名
     */
    private String name;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 员工类型，1-商户主账号，2-商户员工，3-代理商
     */
    private Integer userType;
    /**
     * 密码
     */
    private String password;
    /**
     * 微信公众平台open id
     */
    private String weiXinPublicPlatformOpenId;
    /**
     * 微信开放平台open id
     */
    private String weiXinOpenPlatformOpenId;
    /**
     * 商户ID
     */
    private Long tenantId;
    /**
     * 代理商ID
     */
    private Long agentId;
    /**
     * 账户是否没有过期，1-没有过期，0-已经过期
     */
    private boolean accountNonExpired;
    /**
     * 账户是否没有锁定，1-没有锁定，0-已经锁定
     */
    private boolean accountNonLocked;
    /**
     * 账户凭证是否没有过期，1-没有过期，0-已经过期
     */
    private boolean credentialsNonExpired;
    /**
     * 账户是否启用，1-启用，0-禁用
     */
    private boolean enabled;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeiXinPublicPlatformOpenId() {
        return weiXinPublicPlatformOpenId;
    }

    public void setWeiXinPublicPlatformOpenId(String weiXinPublicPlatformOpenId) {
        this.weiXinPublicPlatformOpenId = weiXinPublicPlatformOpenId;
    }

    public String getWeiXinOpenPlatformOpenId() {
        return weiXinOpenPlatformOpenId;
    }

    public void setWeiXinOpenPlatformOpenId(String weiXinOpenPlatformOpenId) {
        this.weiXinOpenPlatformOpenId = weiXinOpenPlatformOpenId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
