package com.lzg.test.sftp;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

/**
 * Secure File Transfer Protocol
 * 安全文件传送协议
 * @author lzg
 * @date 2018年4月15日 下午9:49:32
 *
 */
public class Sftp {
    
    /** 日志记录器  */
    private Logger logger = Logger.getLogger(Sftp.class);
    /** Session */
    private Session session = null;
    /** Channel */
    private ChannelSftp channel = null;
    /** SFTP服务器IP地址 */
    private String host;
    /** SFTP服务器端口 */
    private int port;
    /** 连接超时时间，单位毫秒  */
    private int timeout;
    
    /** 用户名 */
    private String username;
    /** 密码 */
    private String password;
    
    /**
     * SFTP 安全文件传送协议
     * @param host SFTP服务器IP地址
     * @param port SFTP服务器端口
     * @param timeout 连接超时时间，单位毫秒 
     * @param username 用户名
     * @param password 密码
     */
    public Sftp(String host,int port,int timeout,String username,String password){
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.username = username;
        this.password = password;
    }
    
    /**
     * 登陆SFTP服务器
     * @return boolean
     */
    public boolean login() {
        
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            if(password != null){
                session.setPassword(password);
            }
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setTimeout(timeout);
            session.connect();
            logger.debug("sftp session connected");
            
            logger.debug("opening channel");
            channel = (ChannelSftp)session.openChannel("sftp");
            channel.connect();
            
            logger.debug("connected successfully");
            return true;
        } catch (JSchException e) {
            logger.error("sftp login failed",e);
            return false;
        }
    }
    
    /**
     * 上传文件
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr><td>当前目录</td><td>方法</td><td>参数：绝对路径/相对路径</td><td>上传后</td></tr>
     * <tr><td>/</td><td>uploadFile("testA","upload.txt",new FileInputStream(new File("up.txt")))</td><td>相对路径</td><td>/testA/upload.txt</td></tr>
     * <tr><td>/</td><td>uploadFile("testA/testA_B","upload.txt",new FileInputStream(new File("up.txt")))</td><td>相对路径</td><td>/testA/testA_B/upload.txt</td></tr>
     * <tr><td>/</td><td>uploadFile("/testA/testA_B","upload.txt",new FileInputStream(new File("up.txt")))</td><td>绝对路径</td><td>/testA/testA_B/upload.txt</td></tr>
     * </table>
     * </p>
     * @param pathName SFTP服务器目录
     * @param fileName 服务器上保存的文件名
     * @param input 输入文件流
     * @return boolean
     */
    public boolean uploadFile(String pathName,String fileName,InputStream input){
        
        String currentDir = currentDir();
        if(!changeDir(pathName)){
            return false;
        }
        
        try {
            channel.put(input,fileName,ChannelSftp.OVERWRITE);
            if(!existFile(fileName)){
                logger.debug("upload failed");
                return false;
            }
            logger.debug("upload successful");
            return true;
        } catch (SftpException e) {
            logger.error("upload failed",e);
            return false;
        } finally {
            changeDir(currentDir);
        }
    }
    
    /**
     * 上传文件
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr><td>当前目录</td><td>方法</td><td>参数：绝对路径/相对路径</td><td>上传后</td></tr>
     * <tr><td>/</td><td>uploadFile("testA","upload.txt","up.txt")</td><td>相对路径</td><td>/testA/upload.txt</td></tr>
     * <tr><td>/</td><td>uploadFile("testA/testA_B","upload.txt","up.txt")</td><td>相对路径</td><td>/testA/testA_B/upload.txt</td></tr>
     * <tr><td>/</td><td>uploadFile("/testA/testA_B","upload.txt","up.txt")</td><td>绝对路径</td><td>/testA/testA_B/upload.txt</td></tr>
     * </table>
     * </p>
     * @param pathName SFTP服务器目录
     * @param fileName 服务器上保存的文件名
     * @param localFile 本地文件
     * @return boolean
     */
    public boolean uploadFile(String pathName,String fileName,String localFile){
        
        String currentDir = currentDir();
        if(!changeDir(pathName)){
            return false;
        }
        
        try {
            channel.put(localFile,fileName,ChannelSftp.OVERWRITE);
            if(!existFile(fileName)){
                logger.debug("upload failed");
                return false;
            }
            logger.debug("upload successful");
            return true;
        } catch (SftpException e) {
            logger.error("upload failed",e);
            return false;
        } finally {
            changeDir(currentDir);
        }
    }
    
    /**
     * 下载文件
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr><td>当前目录</td><td>方法</td><td>参数：绝对路径/相对路径</td><td>下载后</td></tr>
     * <tr><td>/</td><td>downloadFile("testA","down.txt","D:\\downDir")</td><td>相对路径</td><td>D:\\downDir\\down.txt</td></tr>
     * <tr><td>/</td><td>downloadFile("testA/testA_B","down.txt","D:\\downDir")</td><td>相对路径</td><td>D:\\downDir\\down.txt</td></tr>
     * <tr><td>/</td><td>downloadFile("/testA/testA_B","down.txt","D:\\downDir")</td><td>绝对路径</td><td>D:\\downDir\\down.txt</td></tr>
     * </table>
     * </p>
     * @param remotePath SFTP服务器目录
     * @param fileName 服务器上需要下载的文件名
     * @param localPath 本地保存路径
     * @return boolean
     */
    public boolean downloadFile(String remotePath,String fileName,String localPath){
        
        String currentDir = currentDir();
        if(!changeDir(remotePath)){
            return false;
        }
        
        try {
            String localFilePath = localPath + File.separator + fileName;
            channel.get(fileName,localFilePath);
            
            File localFile = new File(localFilePath);
            if(!localFile.exists()){
                logger.debug("download file failed");
                return false;
            }
            logger.debug("download successful");
            return true;
        } catch (SftpException e) {
            logger.error("download file failed",e);
            return false;
        } finally {
            changeDir(currentDir);
        }
    }
    
    /**
     * 切换工作目录
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr><td>当前目录</td><td>方法</td><td>参数(绝对路径/相对路径)</td><td>切换后的目录</td></tr>
     * <tr><td>/</td><td>changeDir("testA")</td><td>相对路径</td><td>/testA/</td></tr>
     * <tr><td>/</td><td>changeDir("testA/testA_B")</td><td>相对路径</td><td>/testA/testA_B/</td></tr>
     * <tr><td>/</td><td>changeDir("/testA")</td><td>绝对路径</td><td>/testA/</td></tr>
     * <tr><td>/testA/testA_B/</td><td>changeDir("/testA")</td><td>绝对路径</td><td>/testA/</td></tr>
     * </table>
     * </p>
     * @param pathName 路径
     * @return boolean
     */
    public boolean changeDir(String pathName){
        if(pathName == null || pathName.trim().equals("")){
            logger.debug("invalid pathName");
            return false;
        }
        
        try {
            channel.cd(pathName.replaceAll("\\\\", "/"));
            logger.debug("directory successfully changed,current dir=" + channel.pwd());
            return true;
        } catch (SftpException e) {
            logger.error("failed to change directory",e);
            return false;
        }
    }
    
    /**
     * 切换到上一级目录
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr><td>当前目录</td><td>方法</td><td>切换后的目录</td></tr>
     * <tr><td>/testA/</td><td>changeToParentDir()</td><td>/</td></tr>
     * <tr><td>/testA/testA_B/</td><td>changeToParentDir()</td><td>/testA/</td></tr>
     * </table>
     * </p>
     * @return boolean
     */
    public boolean changeToParentDir(){
        return changeDir("..");
    }
    
    /**
     * 切换到根目录
     * @return boolean
     */
    public boolean changeToHomeDir(){
        String homeDir = null;
        try {
            homeDir = channel.getHome();
        } catch (SftpException e) {
            logger.error("can not get home directory",e);
            return false;
        }
        return changeDir(homeDir);
    }
    
    /**
     * 创建目录
     * <p>
     * 使用示例，SFTP服务器上的目录结构如下：/testA/testA_B/
     * <table border="1">
     * <tr><td>当前目录</td><td>方法</td><td>参数(绝对路径/相对路径)</td><td>创建成功后的目录</td></tr>
     * <tr><td>/testA/testA_B/</td><td>makeDir("testA_B_C")</td><td>相对路径</td><td>/testA/testA_B/testA_B_C/</td></tr>
     * <tr><td>/</td><td>makeDir("/testA/testA_B/testA_B_D")</td><td>绝对路径</td><td>/testA/testA_B/testA_B_D/</td></tr>
     * </table>
     * <br/>
     * <b>注意</b>，当<b>中间目录不存在</b>的情况下，不能够使用绝对路径的方式期望创建中间目录及目标目录。
     * 例如makeDir("/testNOEXIST1/testNOEXIST2/testNOEXIST3")，这是错误的。
     * </p>
     * @param dirName 目录
     * @return boolean
     */
    public boolean makeDir(String dirName){
        try {
            channel.mkdir(dirName);
            logger.debug("directory successfully created,dir=" + dirName);
            return true;
        } catch (SftpException e) {
            logger.error("failed to create directory", e);
            return false;
        }
    }
    
    /**
     * 删除文件夹
     * @param dirName
     * @return boolean
     */
    @SuppressWarnings("unchecked")
    public boolean delDir(String dirName){
        if(!changeDir(dirName)){
            return false;
        }
        
        Vector<LsEntry> list = null;
        try {
            list = channel.ls(channel.pwd());
        } catch (SftpException e) {
            logger.error("can not list directory",e);
            return false;
        }
        
        for(LsEntry entry : list){
            String fileName = entry.getFilename();
            if(!fileName.equals(".") && !fileName.equals("..")){
                if(entry.getAttrs().isDir()){
                    delDir(fileName);
                } else {
                    delFile(fileName);
                }
            }
        }
        
        if(!changeToParentDir()){
            return false;
        }
        
        try {
            channel.rmdir(dirName);
            logger.debug("directory " + dirName + " successfully deleted");
            return true;
        } catch (SftpException e) {
            logger.error("failed to delete directory " + dirName,e);
            return false;
        }
    }
    
    /**
     * 删除文件
     * @param fileName 文件名
     * @return boolean
     */
    public boolean delFile(String fileName){
        if(fileName == null || fileName.trim().equals("")){
            logger.debug("invalid filename");
            return false;
        }
        
        try {
            channel.rm(fileName);
            logger.debug("file " + fileName + " successfully deleted");
            return true;
        } catch (SftpException e) {
            logger.error("failed to delete file " + fileName,e);
            return false;
        }
    }
    
    /**
     * 当前目录下文件及文件夹名称列表
     * @return String[]
     */
    public String[] ls(){
        return list(Filter.ALL);
    }
    
    /**
     * 指定目录下文件及文件夹名称列表
     * @return String[]
     */
    public String[] ls(String pathName){
        String currentDir = currentDir();
        if(!changeDir(pathName)){
            return new String[0];
        };
        String[] result = list(Filter.ALL);
        if(!changeDir(currentDir)){
            return new String[0];
        }
        return result;
    }
    
    /**
     * 当前目录下文件名称列表
     * @return String[]
     */
    public String[] lsFiles(){
        return list(Filter.FILE);
    }
    
    /**
     * 指定目录下文件名称列表
     * @return String[]
     */
    public String[] lsFiles(String pathName){
        String currentDir = currentDir();
        if(!changeDir(pathName)){
            return new String[0];
        };
        String[] result = list(Filter.FILE);
        if(!changeDir(currentDir)){
            return new String[0];
        }
        return result;
    }
    
    /**
     * 当前目录下文件夹名称列表
     * @return String[]
     */
    public String[] lsDirs(){
        return list(Filter.DIR);
    }
    
    /**
     * 指定目录下文件夹名称列表
     * @return String[]
     */
    public String[] lsDirs(String pathName){
        String currentDir = currentDir();
        if(!changeDir(pathName)){
            return new String[0];
        };
        String[] result = list(Filter.DIR);
        if(!changeDir(currentDir)){
            return new String[0];
        }
        return result;
    }
    
    /**
     * 当前目录是否存在文件或文件夹
     * @param name 名称
     * @return boolean
     */
    public boolean exist(String name){
        return exist(ls(), name);
    }
    
    /**
     * 指定目录下，是否存在文件或文件夹
     * @param path 目录
     * @param name 名称
     * @return boolean
     */
    public boolean exist(String path,String name){
        return exist(ls(path),name);
    }
    
    /**
     * 当前目录是否存在文件
     * @param name 文件名
     * @return boolean
     */
    public boolean existFile(String name){
        return exist(lsFiles(),name);
    }
    
    /**
     * 指定目录下，是否存在文件
     * @param path 目录
     * @param name 文件名
     * @return boolean
     */
    public boolean existFile(String path,String name){
        return exist(lsFiles(path), name);
    }
    
    /**
     * 当前目录是否存在文件夹
     * @param name 文件夹名称
     * @return boolean
     */
    public boolean existDir(String name){
        return exist(lsDirs(), name);
    }
    
    /**
     * 指定目录下，是否存在文件夹
     * @param path 目录
     * @param name 文家夹名称
     * @return boolean
     */
    public boolean existDir(String path,String name){
        return exist(lsDirs(path), name);
    }
    
    /**
     * 当前工作目录
     * @return String
     */
    public String currentDir(){
        try {
            return channel.pwd();
        } catch (SftpException e) {
            logger.error("failed to get current dir",e);
            return homeDir();
        }
    }
    
    /**
     * 登出
     */
    public void logout(){
        if(channel != null){
            channel.quit();
            channel.disconnect();
        }
        if(session != null){
            session.disconnect();
        }
        logger.debug("logout successfully");
    }
    
    
    //------private method ------
    
    /** 枚举，用于过滤文件和文件夹  */
    private enum Filter {/** 文件及文件夹 */ ALL ,/** 文件 */ FILE ,/** 文件夹 */ DIR };
    
    /**
     * 列出当前目录下的文件及文件夹
     * @param filter 过滤参数
     * @return String[] 
     */
    @SuppressWarnings("unchecked")
    private String[] list(Filter filter){
        Vector<LsEntry> list = null;
        try {
            //ls方法会返回两个特殊的目录，当前目录(.)和父目录(..)
            list = channel.ls(channel.pwd());
        } catch (SftpException e) {
            logger.error("can not list directory",e);
            return new String[0];
        }
        
        List<String> resultList = new ArrayList<String>();
        for(LsEntry entry : list){
            if(filter(entry, filter)){
                resultList.add(entry.getFilename());
            }
        }
        return resultList.toArray(new String[0]);
    }
    
    /**
     * 判断是否是否过滤条件
     * @param entry LsEntry
     * @param f 过滤参数
     * @return boolean
     */
    private boolean filter(LsEntry entry,Filter f){
        if(f.equals(Filter.ALL)){
            return !entry.getFilename().equals(".") && !entry.getFilename().equals("..");
        } else if(f.equals(Filter.FILE)){
            return !entry.getFilename().equals(".") && !entry.getFilename().equals("..") && !entry.getAttrs().isDir();
        } else if(f.equals(Filter.DIR)){
            return !entry.getFilename().equals(".") && !entry.getFilename().equals("..") && entry.getAttrs().isDir();
        }
        return false;
    }
    
    /**
     * 根目录
     * @return String
     */
    private String homeDir(){
        try {
            return channel.getHome();
        } catch (SftpException e) {
            return "/";
        }
    }
    
    /**
     * 判断字符串是否存在于数组中
     * @param strArr 字符串数组
     * @param str 字符串
     * @return boolean
     */
    private boolean exist(String[] strArr,String str){
        if(strArr == null || strArr.length == 0){
            return false;
        }
        if(str == null || str.trim().equals("")){
            return false;
        }
        for(String s : strArr){
            if(s.equalsIgnoreCase(str)){
                return true;
            }
        }
        return false;
    }

    
    //------private method ------
}