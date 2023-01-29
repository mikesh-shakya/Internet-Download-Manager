package org.example;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.config.AppConfig;
import org.example.model.FileInfo;

import java.io.File;
import java.text.DecimalFormat;

public class DownloadManager {
    @FXML
    private TableView<FileInfo> tableView;
    @FXML
    private TextField URLText;

    private int index = 0;
    @FXML
    void downloadButtonClicked(ActionEvent event) {
        String url = URLText.getText().trim();
        System.out.println("The URL is: " + url);
        // Let the url is http://www.spring.in/spring.exe
        String fileName = url.substring(url.lastIndexOf("/")+1);
        String status = "STARTING";
        String action = "OPEN";
        String path = AppConfig.DOWNLOAD_PATH + File.separator+fileName;
        FileInfo fileInfo = new FileInfo((index + 1) + "",fileName, url, status,action,path, "0" );
        this.index = this.index+1;
        DownloadThread thread = new DownloadThread(fileInfo, this);
        this.tableView.getItems().add(Integer.parseInt(fileInfo.getIndex())-1,fileInfo);
        thread.start();
        this.URLText.setText("");
    }

    public void updateUI(FileInfo metaFile) {
        System.out.println(metaFile);
        FileInfo info = this.tableView.getItems().get(Integer.parseInt(metaFile.getIndex())-1);
        info.setStatus(metaFile.getStatus());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        info.setPercentage(decimalFormat.format(Double.parseDouble(metaFile.getPercentage())));
        this.tableView.refresh();
        System.out.println("--------------------------------------------");
    }

    @FXML
    public void initialize(){
        System.out.println("View Initialized...");

        TableColumn<FileInfo, String> sn = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(0);
        sn.setCellValueFactory(p->{
            return p.getValue().indexProperty();
        });

        TableColumn<FileInfo, String> fileName = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(1);
        fileName.setCellValueFactory(p->{
            return p.getValue().nameProperty();
        });

        TableColumn<FileInfo, String> url = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(2);
        url.setCellValueFactory(p->{
            return p.getValue().urlProperty();
        });

        TableColumn<FileInfo, String> status = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(3);
        status.setCellValueFactory(p->{
            return p.getValue().statusProperty();
        });

        TableColumn<FileInfo, String> percentage = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(4);
        percentage.setCellValueFactory(p->{
            SimpleStringProperty simpleStringProperty = new SimpleStringProperty();
            simpleStringProperty.set(p.getValue().getPercentage() + " %");
            return simpleStringProperty;
        });

        TableColumn<FileInfo, String> action = (TableColumn<FileInfo, String>) this.tableView.getColumns().get(5);
        action.setCellValueFactory(p->{
            return p.getValue().actionProperty();
        });
    }
}
