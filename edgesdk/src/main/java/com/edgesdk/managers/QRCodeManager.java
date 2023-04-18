package com.edgesdk.managers;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.edgesdk.EdgeSdk;
import com.edgesdk.Utils.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCodeManager {
    private static EdgeSdk edgeSdk;

    public QRCodeManager(EdgeSdk edgeSdk) {
        this.edgeSdk = edgeSdk;
    }
    public Bitmap getSecondScreenQRCode(){
        String screenId = this.edgeSdk.getLocalStorageManager().getStringValue(Constants.SCREEN_ID);
        String walletAddress = this.edgeSdk.getLocalStorageManager().getStringValue(Constants.WALLET_ADDRESS);
        String qrCodeUrl ="https://livesearch.edgevideo.com/qrRemote/?id="+screenId+"wallet="+walletAddress;
        Bitmap qrCode = generateQRCode(qrCodeUrl,200,200);
        return qrCode;
    }
    public Bitmap getGamifiedTvQRCode(){
        String screenId = this.edgeSdk.getLocalStorageManager().getStringValue(Constants.SCREEN_ID);
        String walletAddress = this.edgeSdk.getLocalStorageManager().getStringValue(Constants.WALLET_ADDRESS);
        String qrCodeUrl ="https://livesearch.edgevideo.com/qrRemote/?id="+screenId+"wallet="+walletAddress;
        Bitmap qrCode = generateQRCode(qrCodeUrl,200,200);
        return qrCode;
    }

    public static Bitmap generateQRCode(String data, int width, int height) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, null);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int matrixWidth = bitMatrix.getWidth();
        int matrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[matrixWidth * matrixHeight];
        for (int y = 0; y < matrixHeight; y++) {
            int offset = y * matrixWidth;
            for (int x = 0; x < matrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(matrixWidth, matrixHeight, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, matrixWidth, 0, 0, matrixWidth, matrixHeight);
        return bitmap;
    }
}
