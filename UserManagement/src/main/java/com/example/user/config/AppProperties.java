package com.example.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppProperties {

    /**
     * BYOE Configuration specifically for Axis
     */
    public static final String BYOE_ENABLED = "byoe.enable";
    public static final String BYOE_HSM_HOST = "byoe.hsmHost";
    public static final String BYOE_MASTERKEY_ALIAS = "byoe.masterKeyAlias";
    public static final String BYOE_REFRESH_TOKEN = "byoe.refreshToken";
    public static final String BYOE_ENCRYPTED_DEK = "byoe.encryptedDek";
    public static final String BYOE_IV = "byoe.iv";

    /**
     * CR Refresh configuration
     */
    public static final String CR_SERVICE_BASE_URL = "cr.service.url";
    public static final String TOKEN_SERVICE_BASE_URL = "token.service.url";
    public static final String TOKEN_SERVICE_ENABLED = "token.service.enabled";
    public static final String TOKEN_SERVICE_ENABLED_AA = "token.service.enabled.aa";
    public static final String SAHAMATI_PUBLIC_KEY = "sahamati.public.key";

    public static final String CR_ACCESS_TOKEN_NAMESPACE = "redis.namespace.key";

    /**
     * Google Mailer Configuration
     */
    public static final String MAIL_SERVICE_ENABLED = "mail.service.enabled";

    /**
     * Dir Config
     */
    public static final String TEMP_DIR_ROOT = "temp.dir";
    public static final String DATA_RETENTION_MAX_AGE_HOURS = "data.retention.age.hours";

    /**
     * Data Encryption key
     */
    public static final String DATA_ENCRYPTION_SECRET = "data.encryption.secret";

    /**
     * Spring Mailer Configuration
     */
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_USERNAME= "mail.smtp.username";
    public static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SENDER_ADDRESS = "mail.sender.address";

    public static final String FIU_ENVIRONMENT = "active.env";

    /**
     * FIU RSA Key for encryption, only for ICICI
     */
    public static final String FIU_RSA_KEY_PATH = "fiu.rsa.key.path";

    /**
     * Heartbeat API version for pager duty alerts
     */
    public static final String PAGERDUTY_HEARTBEAT_VERSION = "pagerduty.heartbeat.version";

    /**
     * A optional boolean property with default false. If true, then system will download all the web resources from s3 into local data volume.
     * If s3 not enabled, then the flag will be ignored
     */
    public static final String DOWNLOAD_STATIC_RESOURCES_ON_STARTUP = "fiu.web.downloadResources";

    /**
     * Data Logging Config
     */
    

    @Autowired
    private Environment environment;

    public String getStrProperty(String propertyName) {
        return propertyName != null ? environment.getProperty(propertyName) : null;
    }

    public int getIntProperty(String propertyName) {
        return propertyName != null ? Integer.parseInt(getStrProperty(propertyName)) : 0;
    }

    public Boolean getBooleanProperty(String propertyName) {
        return propertyName != null && Boolean.parseBoolean(environment.getProperty(propertyName));
    }

}
