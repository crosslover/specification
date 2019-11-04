package com.flamingo.comeon.spec.user;

import com.flamingo.comeon.spec.AbstractSpecification;
import com.flamingo.comeon.spec.MessageSpecification;
import com.flamingo.comeon.spec.Specification;
import com.flamingo.comeon.spec.user.constant.CustomerStatusEnum;
import com.flamingo.comeon.spec.user.constant.CustomerTypeEnum;
import com.flamingo.comeon.spec.user.constant.PlatformEnum;
import com.flamingo.comeon.spec.user.constant.UserTypeEnum;

import java.util.Objects;

import static com.flamingo.comeon.spec.Specifications.*;

public class UserBo {
    /**
     * 客户id
     */
    private Long customerId;
    /**
     * ePassport中账号Id
     */
    private Long acctId;
    private UserTypeEnum userType;
    private CustomerTypeEnum customerType;
    private CustomerStatusEnum customerStatus;
    /**
     * 使用平台
     */
    private PlatformEnum platformInUse;
    /**
     * 使用的APP版本(外卖、h5端应该没有)
     */
    private Integer appVersion;
    private Long subAcctId;
    /**
     * 账号电话
     */
    private String mobile;
    /**
     * 账号电话Token
     */
    private String mobileToken;
    /**
     * 客户联系人电话
     */
    private String contactPhone;
    /**
     * 联系人电话Token
     */
    private String contactPhoneToken;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getAcctId() {
        return acctId;
    }

    public void setAcctId(Long acctId) {
        this.acctId = acctId;
    }

    public UserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(UserTypeEnum userType) {
        this.userType = userType;
    }

    public CustomerTypeEnum getCustomerType() {
        return customerType;
    }

    public void setCustomerType(CustomerTypeEnum customerType) {
        this.customerType = customerType;
    }

    public CustomerStatusEnum getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(CustomerStatusEnum customerStatus) {
        this.customerStatus = customerStatus;
    }

    public PlatformEnum getPlatformInUse() {
        return platformInUse;
    }

    public void setPlatformInUse(PlatformEnum platformInUse) {
        this.platformInUse = platformInUse;
    }

    public Integer getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(Integer appVersion) {
        this.appVersion = appVersion;
    }

    public Long getSubAcctId() {
        return subAcctId;
    }

    public void setSubAcctId(Long subAcctId) {
        this.subAcctId = subAcctId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobileToken() {
        return mobileToken;
    }

    public void setMobileToken(String mobileToken) {
        this.mobileToken = mobileToken;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactPhoneToken() {
        return contactPhoneToken;
    }

    public void setContactPhoneToken(String contactPhoneToken) {
        this.contactPhoneToken = contactPhoneToken;
    }

    /**
     * 检查能否抽奖
     * https://km.sankuai.com/page/207369636#id-4.1.%E7%94%A8%E6%88%B7%E7%82%B9%E5%87%BB%E6%8A%BD%E5%A5%96%E6%8C%89%E9%92%AE%E5%9C%BA%E6%99%AF
     */
    public LotteryAllowableBo checkLotteryAllowable() {
        if (userType == null) {
            return new LotteryAllowableBo(false, null);
        }
        // BD用户
        if (userType == UserTypeEnum.BD) {
            return new LotteryAllowableBo(false, "BD用户不能参与活动");
        }
        // 内部用户
        if (userType == UserTypeEnum.INTERNAL && customerStatus == CustomerStatusEnum.ONLINE) {
            return new LotteryAllowableBo(true, "");
        }
        if (userType == UserTypeEnum.INTERNAL) {
            if (Objects.nonNull(appVersion) && appVersion < 30300) {
                return new LotteryAllowableBo(false, "您的APP版本过低，请先升级版本");
            }
            if (customerStatus == CustomerStatusEnum.OFFLINE) {
                return new LotteryAllowableBo(false, "账号已下线，无法参与，可联系销售或客服解决");
            }
            return new LotteryAllowableBo(false, "账号待合作，无法参与，可联系销售或客服解决");
        }
        // 外卖用户
        if (userType == UserTypeEnum.WAIMAI) {
            if (customerStatus == CustomerStatusEnum.ONLINE) {
                return new LotteryAllowableBo(false, "外卖APP用户暂不支持，您可以打开APP参与");
            }
            if (customerStatus == CustomerStatusEnum.OFFLINE) {
                return new LotteryAllowableBo(false, "账号已下线，无法参与，可联系销售或客服解决");
            }
            if (customerStatus == CustomerStatusEnum.TO_COOP) {
                return new LotteryAllowableBo(false, "账号待合作，无法参与，可联系销售或客服解决");
            }
            return new LotteryAllowableBo(false, null);
        }
        return new LotteryAllowableBo(false, null);

    }

    public LotteryAllowableBo checkLotteryAllowableNew() {
        Specification<UserBo> internal = new UserTypeSpecification(UserTypeEnum.INTERNAL);
        Specification<UserBo> waimai = new UserTypeSpecification(UserTypeEnum.WAIMAI);
        Specification<UserBo> bd = new UserTypeSpecification(UserTypeEnum.BD);

        Specification<UserBo> online = new CustomerStatusSpecification(CustomerStatusEnum.ONLINE);
        Specification<UserBo> offline = new CustomerStatusSpecification(CustomerStatusEnum.OFFLINE);
        Specification<UserBo> toCoop = new CustomerStatusSpecification(CustomerStatusEnum.TO_COOP);

        Specification<UserBo> ver = new AppVersionLessSpecification(30300);

        if (and(internal, online).isSatisfiedBy(this)) {
            return new LotteryAllowableBo(true);
        }

        MessageSpecification<String, UserBo> messageSpecification = or(ms(bd, "BD用户不能参与活动", null),
                msAnd(internal,
                        or(ms(ver, "您的APP版本过低，请先升级版本", null),
                        ms(offline, "账号已下线，无法参与，可联系销售或客服解决", null),
                        ms(toCoop, "账号待合作，无法参与，可联系销售或客服解决", null))),
                msAnd(waimai,
                        or(ms(online, "外卖APP用户暂不支持，您可以打开APP参与", null),
                                ms(offline, "账号已下线，无法参与，可联系销售或客服解决", null),
                                ms(toCoop, "账号待合作，无法参与，可联系销售或客服解决", null)))
                );

        System.out.println("状态" + this.toString());
        if (messageSpecification.isSatisfiedBy(this)) {
            return new LotteryAllowableBo(false, messageSpecification.getMessage());
        }
        System.out.println("未知状态" + this.toString());
        return new LotteryAllowableBo(false);
    }

    @Override
    public String toString() {
        return "type:" + userType + " status:" + customerStatus + " version:" + appVersion;
    }

    static class UserTypeSpecification extends AbstractSpecification<UserBo> {
        UserTypeEnum userType;

        UserTypeSpecification(UserTypeEnum userType) {
            this.userType = userType;
        }

        @Override
        public boolean isSatisfiedBy(UserBo target) {
            return target.userType == userType;
        }
    }

    static class CustomerStatusSpecification extends AbstractSpecification<UserBo> {
        CustomerStatusEnum customerStatus;

        CustomerStatusSpecification(CustomerStatusEnum customerStatus) {
            this.customerStatus = customerStatus;
        }

        @Override
        public boolean isSatisfiedBy(UserBo target) {
            return target.customerStatus == customerStatus;
        }
    }

    static class AppVersionLessSpecification extends AbstractSpecification<UserBo> {
        Integer appVersion;

        AppVersionLessSpecification(Integer appVersion) {
            this.appVersion = appVersion;
        }

        @Override
        public boolean isSatisfiedBy(UserBo target) {
            return Objects.nonNull(target.appVersion) && target.appVersion < appVersion;
        }
    }

    public static void main(String[] args) {

        UserBo userBo = new UserBo();
        LotteryAllowableBo lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setUserType(UserTypeEnum.BD);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setUserType(UserTypeEnum.INTERNAL);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.ONLINE);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.OFFLINE);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.TO_COOP);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.NOT_EXIST);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setAppVersion(200);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setUserType(UserTypeEnum.WAIMAI);
        userBo.setCustomerStatus(null);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.ONLINE);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.OFFLINE);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.TO_COOP);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);

        userBo.setCustomerStatus(CustomerStatusEnum.NOT_EXIST);
        lab = userBo.checkLotteryAllowableNew();
        System.out.println(lab);
    }
}