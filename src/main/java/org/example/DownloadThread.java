package org.example;

import org.example.model.FileInfo;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DownloadThread extends Thread{

    private FileInfo fileInfo;
    DownloadManager downloadManager;

    public DownloadThread(FileInfo fileInfo, DownloadManager downloadManager) {
        this.fileInfo = fileInfo;
        this.downloadManager = downloadManager;
    }

    @Override
    public void run() {

        this.fileInfo.setStatus("DOWNLOADING");
        this.downloadManager.updateUI(this.fileInfo);

        try {
            // Download Logic
//            Files.copy(new URL(this.fileInfo.getUrl()).openStream(), Paths.get(this.fileInfo.getPath()));

            URL url = new URL(this.fileInfo.getUrl());
            URLConnection urlConnection = url.openConnection();
            int fileSize = urlConnection.getContentLength();

            int countByte = 0;
            double per = 0.0;
            double byteSum = 0.0;

            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            FileOutputStream fos = new FileOutputStream(this.fileInfo.getPath());
            byte data[] = new byte[1024];

            while(true){
                countByte = bufferedInputStream.read(data,0,1024);
                if(countByte==-1) break;
                fos.write(data,0,countByte);
                byteSum += countByte;

                if(fileSize>0){
                    per = (byteSum/fileSize)*100;
                    System.out.println(per);
                    this.fileInfo.setPercentage(per + "");
                    this.downloadManager.updateUI(fileInfo);
                }
            }

            fos.close();
            bufferedInputStream.close();
            this.setName(100 +"");
            this.fileInfo.setStatus("DOWNLOADED");
        } catch (IOException e) {
            this.fileInfo.setStatus("FAILED");
            System.out.println("Downloading Error");
            e.printStackTrace();
        }
        this.downloadManager.updateUI(this.fileInfo);

    }
}
