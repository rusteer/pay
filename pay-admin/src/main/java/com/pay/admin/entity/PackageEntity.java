package com.pay.admin.entity;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.pay.admin.entity.framework.IdEntity;

@Entity
@Table(name = "t_package")
public class PackageEntity extends IdEntity {
    private Long productId;
    private Long channelId;
    private String name;
    private String packageName;
    private String versionName;
    private Integer versionCode;
    private String appName;
    private String paySdks;
    private String paySdk;
    private String loginName;
    private String loginPassword;
    private boolean showActivateCount;
    private boolean showPayCount;
    private boolean showPayEarning;
    private String promotionType;
    private int buildStatus;
    private String apkPath;
    private int payHintType;
    private String info;
    private Date createTime;
    private boolean autoSync;
    private int discountStartCount;//前多少个不扣量
    private int discountRate;
    private int marketPrice;
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public PackageEntity() {}
    public PackageEntity(Long id) {
        this.id = id;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public Long getChannelId() {
        return channelId;
    }
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getVersionName() {
        return versionName;
    }
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    public Integer getVersionCode() {
        return versionCode;
    }
    public void setVersionCode(Integer versionCode) {
        this.versionCode = versionCode;
    }
    public String getAppName() {
        return appName;
    }
    public void setAppName(String appName) {
        this.appName = appName;
    }
    public String getPaySdk() {
        return paySdk;
    }
    public void setPaySdk(String paySdk) {
        this.paySdk = paySdk;
    }
    public String getPaySdks() {
        return paySdks;
    }
    public void setPaySdks(String paySdks) {
        this.paySdks = paySdks;
    }
    public String getLoginName() {
        return loginName;
    }
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
    public String getLoginPassword() {
        return loginPassword;
    }
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
    public boolean isShowActivateCount() {
        return showActivateCount;
    }
    public void setShowActivateCount(boolean showActivateCount) {
        this.showActivateCount = showActivateCount;
    }
    public boolean isShowPayCount() {
        return showPayCount;
    }
    public void setShowPayCount(boolean showPayCount) {
        this.showPayCount = showPayCount;
    }
    public boolean isShowPayEarning() {
        return showPayEarning;
    }
    public boolean isAutoSync() {
        return autoSync;
    }
    public int getDiscountRate() {
        return discountRate;
    }
    public void setAutoSync(boolean autoSync) {
        this.autoSync = autoSync;
    }
    public void setDiscountRate(int discountRate) {
        this.discountRate = discountRate;
    }
    public void setShowPayEarning(boolean showPayEarning) {
        this.showPayEarning = showPayEarning;
    }
    public String getPromotionType() {
        return promotionType;
    }
    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
    public String getApkPath() {
        return apkPath;
    }
    public void setApkPath(String apkPath) {
        this.apkPath = apkPath;
    }
    public int getPayHintType() {
        return payHintType;
    }
    public void setPayHintType(int payHintType) {
        this.payHintType = payHintType;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getBuildStatus() {
        return buildStatus;
    }
    public void setBuildStatus(int buildStatus) {
        this.buildStatus = buildStatus;
    }
    public int getMarketPrice() {
        return marketPrice;
    }
    public void setMarketPrice(int marketPrice) {
        this.marketPrice = marketPrice;
    }
    public int getDiscountStartCount() {
        return discountStartCount;
    }
    public void setDiscountStartCount(int discountStartCount) {
        this.discountStartCount = discountStartCount;
    }
}