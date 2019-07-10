package com.hy.wf.admin.common.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @program: hy-wf
 * @description:
 * @author: jt
 * @create: 2019-03-27 11:02
 **/
@Component
public class FtpPlugin {

    private String host = "api.qtb.hongyunapp.com";
    private Integer port = 21;
    private String username = "qtb";
    private String password = "qtb@2018";

    public boolean upload(String path, InputStream inputStream, String contentType) {
        boolean flag = false;
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            //int  reply = ftpClient.getReplyCode();
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                String directory = StringUtils.substringBeforeLast(path, "/");
                String filename = StringUtils.substringAfterLast(path, "/");
                if (!ftpClient.changeWorkingDirectory(directory)) {
                    String[] paths = StringUtils.split(directory, "/");
                    String p = "/";
                    ftpClient.changeWorkingDirectory(p);
                    for (String s : paths) {
                        p += s + "/";
                        if (!ftpClient.changeWorkingDirectory(p)) {
                            ftpClient.makeDirectory(s);
                            ftpClient.changeWorkingDirectory(p);
                        }
                    }
                }
                flag = ftpClient.storeFile(filename, inputStream);
                ftpClient.logout();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {

                }
            }
        }
        return flag;
    }

    public void delete(String fileName) {
        FTPClient ftpClient = new FTPClient();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            ftpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            ftpClient.dele(fileName);
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputStream);
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
        }
    }
}
