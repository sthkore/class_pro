package com.sth.jvm;

import com.sth.jvm.util.Log;
import com.sth.jvm.util.StrUtils;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Base64;


/**
 * AgentTransformer
 * jvm加载class时回调
 *
 * @author roseboy
 */
public class AgentTransformer implements ClassFileTransformer {
    //密码
    private char[] pwd;

    private String jarFilePath;

    /**
     * 构造方法
     *
     * @param pwd 密码
     */
    public AgentTransformer(char[] pwd,String jarFilePath) {
        this.pwd = pwd;
        this.jarFilePath=jarFilePath;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain domain, byte[] classBuffer) {
        if (className == null || domain == null || loader == null) {
            return classBuffer;
        }

        //获取类所在的项目运行路径
        //String projectPath = domain.getCodeSource().getLocation().getPath();
       // Log.println("111projectPath="+projectPath);
       // projectPath = JarUtils.getRootPath(projectPath);

        //Log.println("projectPath="+projectPath);
        if (StrUtils.isEmpty(jarFilePath)) {
            return classBuffer;
        }

        className = className.replace("/", ".").replace("\\", ".");
            Log.debug("jarFilePath="+jarFilePath);
            try{
                byte[] bytes = JarDecryptor.getInstance().doDecrypt(jarFilePath, className, this.pwd);
                 Log.debug("解密结果:"+Base64.getEncoder().encodeToString(bytes));
                //CAFEBABE,表示解密成功
                if (bytes != null && bytes[0] == -54 && bytes[1] == -2 && bytes[2] == -70 && bytes[3] == -66) {
                  //  Log.println("成功解密:"+Base64.getEncoder().encodeToString(bytes));
                    return bytes;
                }
            }catch (Exception e){
                Log.debug("读取发生异常:"+e);
            }
        return classBuffer;

    }
}
