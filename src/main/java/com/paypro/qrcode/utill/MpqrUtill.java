package com.paypro.qrcode.utill;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.paypro.qrcode.csvmodel.Emvco;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MpqrUtill {

    private final static Logger log = Logger.getLogger(MpqrUtill.class);

    public static List<Emvco> csvFileEmvcoReader(String csvFile) {
        log.info("=== Csv File Reader ===");
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get(csvFile));
        } catch (IOException e) {
            log.debug(e.getMessage());
        }

        CsvToBean<Emvco> csvToBean = new CsvToBeanBuilder(reader)
                .withType(Emvco.class)
                .withSeparator(',')
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        Iterator<Emvco> csvUserIterator = csvToBean.iterator();
        List<Emvco> emvcos = new ArrayList<>();

        while (csvUserIterator.hasNext()) {
            emvcos.add(csvUserIterator.next());
        }
        return emvcos;
    }

    public static void encodeAsBitmap(String qrcontent, int width, int height, int index,
                                      String imgPath, String merchantName, String qrFilePath, String identifierValue)
            throws WriterException, IOException {
        log.info("=== Encode As Bitmap ===");
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrcontent, BarcodeFormat.QR_CODE, width, height);

        Path path = FileSystems.getDefault().getPath(qrFilePath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
        String identifierFileMerchantCode = convertTextToImageMerchantCode(index, merchantName, identifierValue, imgPath);
        String identifierFileMerchantName = convertTextToImageMerchantName(index, merchantName, identifierValue, imgPath);
        combineImage(index, imgPath, merchantName, identifierFileMerchantCode, identifierFileMerchantName, qrFilePath);
    }

    public static String getQrFilePth(String merchantName, File rootPath) {
        log.info("=== Get QR File Path ===");
        String path = rootPath + File.separator + "img";
        File dir = new File(path);
        if (!dir.exists())
            dir.mkdirs();
        return path + File.separator + merchantName + ".png";
    }

    public static String exportXlsToCsv(String filePath, String xlsxFile) {
        log.info("=== Export XLS To Csv ===");
        Workbook wb = null;
        String csvFile = "emvcoXlsToCsv.csv";
        try {
            wb = new XSSFWorkbook(new File(filePath + xlsxFile));
            DataFormatter formatter = new DataFormatter();
            PrintStream out = new PrintStream(new FileOutputStream(filePath + File.separator + csvFile),
                    true, "UTF-8");
            for (Sheet sheet : wb) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        out.print(',');
                        String text = formatter.formatCellValue(cell);
                        out.print(text);
                    }
                    out.println();
                }
            }
        } catch (IOException e) {
            log.debug(e.getMessage());
        } catch (InvalidFormatException e) {
            log.debug(e.getMessage());
        }
        return filePath + File.separator + csvFile;
    }

    private static void combineImage(int index, String imgPath, String merchantName, String identifierFileMerchantCode, String identifierFileMerchantName, String filePath) {
        log.info("=== Combine Image ===");
        String qrFrame = "qrframe.jpeg";
        try {
            BufferedImage imgFrame = ImageIO.read(new File(imgPath + File.separator + "img" + File.separator + qrFrame));
            BufferedImage imgQr = ImageIO.read(new File(filePath));

            int w = Math.max(imgQr.getWidth(), imgFrame.getWidth());
            int h = Math.max(imgQr.getHeight(), imgFrame.getHeight());
            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            Graphics g = combined.getGraphics();
            g.drawImage(imgFrame, 0, 0, null);
            g.drawImage(imgQr, 180, 290, null);

            String fileCombinePath = imgPath + File.separator + "img" + File.separator + index + "_" + merchantName + "_frame.png";

            ImageIO.write(combined, "PNG", new File(fileCombinePath));
            deleteQrImageFile(filePath);
            combineImage(imgPath, fileCombinePath, identifierFileMerchantCode, identifierFileMerchantName, index, merchantName);
        } catch (IOException ie) {
            log.debug(ie.getMessage());
        }
    }

    private static void combineImage(String imgPath, String frameFile, String identifierFileMerchantCode, String identifierFileMerchantName, int index, String merchantName) {
        try {
            BufferedImage imgFrame = ImageIO.read(new File(frameFile));
            BufferedImage imgIdentifierValue = ImageIO.read(new File(imgPath + File.separator + "img" + File.separator + identifierFileMerchantCode));

            int w = Math.max(imgIdentifierValue.getWidth(), imgFrame.getWidth());
            int h = Math.max(imgIdentifierValue.getHeight(), imgFrame.getHeight());

            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

            Graphics g = combined.getGraphics();
            g.drawImage(imgFrame, 0, 0, null);
            g.drawImage(imgIdentifierValue, 270, 1114, null);

            ImageIO.write(combined, "PNG", new File(imgPath, File.separator + "img" + File.separator + index + "_" + merchantName + "_" + "temp.png"));

            BufferedImage imgFrame2 = ImageIO.read(new File(imgPath, File.separator + "img" + File.separator + index + "_" + merchantName + "_" + "temp.png"));
            BufferedImage imgIdentifierValue2 = ImageIO.read(new File(imgPath + File.separator + "img" + File.separator + identifierFileMerchantName));

            int w2 = Math.max(imgIdentifierValue2.getWidth(), imgFrame2.getWidth());
            int h2 = Math.max(imgIdentifierValue2.getHeight(), imgFrame2.getHeight());

            BufferedImage combined2 = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);

            Graphics g2 = combined2.getGraphics();
            g2.drawImage(imgFrame2, 0, 0, null);
            g2.drawImage(imgIdentifierValue2, 270, 1185, null);

            ImageIO.write(combined2, "PNG", new File(imgPath, File.separator + "img" + File.separator + index + "_" + merchantName + ".png"));

            deleteQrImageFile(imgPath + File.separator + "img" + File.separator + index + "_" + merchantName + "_" + "temp.png");
            deleteQrImageFile(frameFile);
            deleteQrImageFile(imgPath + File.separator + "img" + File.separator + identifierFileMerchantCode);
            deleteQrImageFile(imgPath + File.separator + "img" + File.separator + identifierFileMerchantName);
        } catch (IOException ie) {
            log.debug(ie.getMessage());
        }
    }

    private static void deleteQrImageFile(String filePath) {
        log.info("=== Delete Qr File With No Frame ===");
        File file = new File(filePath);
        if (file.delete()) {
            log.info(file.getName() + " is deleted!");
        } else {
            log.info(("Delete operation is failed."));
        }
    }

    private static String convertTextToImageMerchantCode(int index, String merchantName, String identifierValue, String imgPath) {
        log.info("==== Convert Merchant Code To Image ====");
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Titilium Web", Font.PLAIN, 40);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(identifierValue);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(identifierValue, 0, fm.getAscent());
        g2d.dispose();
        try {
            ImageIO.write(img, "png", new File(imgPath + File.separator + "img" + File.separator + index + "_" + merchantName + "_merchantcode.png"));
        } catch (IOException ie) {
            log.debug(ie.getMessage());
        }
        return index + "_" + merchantName + "_merchantcode.png";
    }

    private static String convertTextToImageMerchantName(int index, String merchantName, String identifierValue, String imgPath) {
        log.info("==== Convert Merchant Code To Image ====");
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Titilium Web", Font.PLAIN, 40);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(merchantName);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(merchantName, 0, fm.getAscent());
        g2d.dispose();
        try {
            ImageIO.write(img, "png", new File(imgPath + File.separator + "img" + File.separator + index + "_" + merchantName + "_merchantname.png"));
        } catch (IOException ie) {
            log.debug(ie.getMessage());
        }
        return index + "_" + merchantName + "_merchantname.png";
    }
}

