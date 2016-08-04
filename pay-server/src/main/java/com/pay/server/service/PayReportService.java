package com.pay.server.service;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.pay.server.entity.PackageEntity;
import com.pay.server.entity.PackageInstantEntity;
import com.pay.server.entity.PayReportEntity;
import com.pay.server.entity.SdkEntity;
import com.pay.server.repository.PayReportDao;
import com.pay.server.utils.FormatUtil;

@Component
@Transactional
public class PayReportService extends AbstractService<PayReportEntity> {
    @Autowired
    private PayReportDao dao;
    @Autowired
    private PackageService packagService;
    
    @Autowired
    private SdkService sdkService;
    
    @Override
    protected PayReportDao getDao() {
        return dao;
    }
    public void saveReport(PackageInstantEntity instant, int payIndex, String name, int price, String errorMessage, int payResult,String encodedSdk) {
        PackageEntity pkg = this.packagService.get(instant.getPackageId());
        SdkEntity sdk=sdkService.getByEncodedName(encodedSdk);
        PayReportEntity report = new PayReportEntity();
        report.setChannelId(pkg.getChannelId());
        report.setDeviceId(instant.getDeviceId());
        report.setCreateDate(FormatUtil.format(new Date()));
        report.setCreateTime(new Date());
        report.setErrorMessage(errorMessage);
        report.setPackageId(pkg.getId());
        report.setPackageInstantId(instant.getId());
        report.setPayIndex(payIndex);
        report.setPayName(name);
        report.setPrice(price);
        report.setProductId(pkg.getProductId());
        report.setResult(payResult);
        report.setSdkId(sdk.getId());
        save(report);
    }
}
