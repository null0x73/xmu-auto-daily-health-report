package org.demo.prototype.util;

import java.net.URISyntaxException;

public class EnvironmentUtil {

    /**
     * 用途：返回程序当前位置。
     * 当从 IDE 直接执行时，获取工程根目录。
     * 当从 .jar 打包文件执行时，获取 jar 包所在路径。
     * @return
     * @throws URISyntaxException
     */
    public static String getWorkspacePath() {
        String path = EnvironmentUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(path);
        if(path.contains(".jar!/")){      // in jar
            path = path.substring(0, path.indexOf(".jar!/"));
            System.out.println(path);
            int i1 = path.lastIndexOf("/");
            System.out.println(i1);
            path = path.substring(6, i1);
            System.out.println(path);
        } else {        // 直接通过代码在 IDE 中执行
            path = path.substring(1, path.indexOf("/target"));
        }
        return path;
    }

}