package com.paypro.qrcode.test;


import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;

public class QrImage {
    public static void main(String[] args) throws WriterException, FileNotFoundException, IOException {
        Writer writer = new MultiFormatWriter();

        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");

        long now = System.currentTimeMillis();

        //String contents = gson.toJson(docPK);
        String contents = "12345-TESTE123";

        BitMatrix matrix = writer.encode(
                contents
                , BarcodeFormat.QR_CODE, 350, 350,hints);

        BufferedImage buffImage = MatrixToImageWriter.toBufferedImage(matrix);

        ImageIO.write(buffImage, "PNG", new File("abcdefgh.png"));
    }
}
