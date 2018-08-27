package com.paypro.qrcode.service;

import com.mastercard.mpqr.pushpayment.model.PushPaymentData;
import com.paypro.qrcode.csvmodel.Emvco;
import com.paypro.qrcode.csvmodel.QrModel;
import com.paypro.qrcode.utill.MpqrUtill;
import com.paypro.qrcode.utill.EmVcoQrGenerator;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class QrServiceImpl implements QrService {

    private final static Logger log = Logger.getLogger(QrServiceImpl.class);

    @Override
    public List<Emvco> uploadEmvcoQrHandler(MultipartFile file, File rootPath) {
        log.info("===Upload QR Handler===");
        List<Emvco> emvcos = new ArrayList<>();
        try {
            InputStream in = file.getInputStream();
            File currDir = new File(rootPath + File.separator + "fileupload" + File.separator + ".");
            String path = currDir.getAbsolutePath();
            File dir = new File(path);

            if (!dir.exists())
                dir.mkdirs();

            String fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
            FileOutputStream f = new FileOutputStream(fileLocation);
            int ch = 0;
            while ((ch = in.read()) != -1) {
                f.write(ch);
            }
            f.flush();
            f.close();
            fileLocation = MpqrUtill.exportXlsToCsv(rootPath + File.separator + "fileupload" + File.separator, file.getOriginalFilename());
            emvcos = MpqrUtill.csvFileEmvcoReader(fileLocation);
            EmVcoQrGenerator emVcoQrGenerator = new EmVcoQrGenerator(new PushPaymentData());
            emVcoQrGenerator.generateQrCode(emvcos, rootPath);
        } catch (Exception e) {
            log.debug(e.getMessage());
        }
        return emvcos;
    }

    @Override
    public void downloadHandler(HttpServletResponse response, File rootPath, String qrType) throws FileNotFoundException {
        log.info("===Download Handler===");
        String path = rootPath + File.separator + "download";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();

        File file = new File(dir.getAbsolutePath()
                + File.separator + qrType);

        String mimeType = URLConnection.guessContentTypeFromName(file.getName());
        if (mimeType == null) {
            log.info("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }

        response.setContentType(mimeType);
        response.setHeader("Content-Disposition", String.format("inline; filename=\"" + file.getName() + "\""));

        response.setContentLength((int) file.length());

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

        try {
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException ie) {
            log.debug(ie.getMessage());
        }

    }

    @Override
    public void downloadQrImageHandler(HttpServletRequest request, HttpServletResponse response,
                                       File rootPath, List<QrModel> qrModels) throws IOException {
        log.info("===Download QR Image Handler===");
        File dir = new File(rootPath.toString());
        if (!dir.exists())
            dir.mkdirs();

        response.setContentType("Content-type: text/zip");
        response.setHeader("Content-Disposition",
                "attachment; filename=qrImages.zip");

        List<File> imageFiles = setImageFiles(qrModels);
        ServletOutputStream out = response.getOutputStream();
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));

        for (File file : imageFiles) {
            zos.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(dir + File.separator + file);
            } catch (FileNotFoundException fnfe) {
                log.debug(fnfe.getMessage());
                zos.closeEntry();
                continue;
            }

            BufferedInputStream fif = new BufferedInputStream(fis);
            int data = 0;
            while ((data = fif.read()) != -1) {
                zos.write(data);
            }
            fif.close();

            zos.closeEntry();
        }

        zos.close();
    }

    private List<File> setImageFiles(List<QrModel> qrModels) {
        List<File> imageFiles = new ArrayList<>();
        for (QrModel qrModel : qrModels) {
            imageFiles.add(new File(qrModel.getImagePath()));
        }
        return imageFiles;
    }
}
