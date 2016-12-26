package cn.hhd.portal.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvUtil {
    public static final String EVN_TYPE_TEST = "test";
    public static final String EVN_TYPE_STRESSTEST = "stresstest";
    public static final String EVN_TYPE_OFFICIAL = "official";
    private static String envType;

    public EnvUtil() {
        InputStream in = EnvUtil.class.getResourceAsStream("/config/environment-config.properties");
        if (in != null) {
            Properties props = new Properties();
            try {
                props.load(in);
                envType = props.getProperty("environment.type");
                System.out.println("==== $$$$ ==== -_- Current System is [[[" + envType + "]]] environment, Please check is right -_- ==== $$$$ ====");
                System.setProperty("environment_type", envType);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new RuntimeException("config.properties not found. Please make sure the file is in the classpath");
        }
    }

    /**
     * 是否是测试环境
     *
     * @return
     */
    public static boolean isTestEnv() {
        return !EVN_TYPE_OFFICIAL.equalsIgnoreCase(envType);
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        EnvUtil.envType = envType;
        System.setProperty("environment_type", envType);
        System.out.println(System.getProperty("environment_type"));
    }
}
