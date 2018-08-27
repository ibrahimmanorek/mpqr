package com.paypro.qrcode.service;

import com.paypro.qrcode.csvmodel.Emvco;
import com.paypro.qrcode.csvmodel.QrModel;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface QrService {

    List<Emvco> uploadEmvcoQrHandler(MultipartFile multipartFile, File rootPath);

    void downloadHandler(HttpServletResponse response,
                         File rootPath, String type) throws FileNotFoundException;

    void downloadQrImageHandler(HttpServletRequest request, HttpServletResponse response,
                                File rootPath, List<QrModel> qrModels) throws IOException;
}
