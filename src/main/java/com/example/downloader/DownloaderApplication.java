package com.example.downloader;

import com.example.downloader.service.DownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.lang.System.exit;

@SpringBootApplication
public class DownloaderApplication implements CommandLineRunner {

    @Autowired
    private DownloaderService downloaderService;

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DownloaderApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        downloaderService.download(args);
        exit(0);
    }

}

