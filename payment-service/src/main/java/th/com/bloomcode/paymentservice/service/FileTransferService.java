package th.com.bloomcode.paymentservice.service;

import com.jcraft.jsch.SftpException;
import th.com.bloomcode.paymentservice.model.response.ReturnLogResultResponse;

import java.io.InputStream;
import java.sql.Timestamp;

public interface FileTransferService {

    boolean uploadFile(String localFilePath, String remoteFilePath);

    boolean uploadFile(InputStream inputStream, String paymentDate, String fileName) throws SftpException;
    boolean uploadFileBackup(InputStream inputStream, String fileName, Timestamp created) throws SftpException;
    boolean uploadFileError(InputStream inputStream, String fileName) throws SftpException;
    boolean removeFile(String fileName) throws SftpException;

    ReturnLogResultResponse downloadFile();
    ReturnLogResultResponse downloadFileJob(String username);

}