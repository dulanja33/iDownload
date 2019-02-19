package com.example.downloader.service;


import com.example.downloader.Downloader;
import com.example.downloader.dto.ArgsDataParseDTO;
import org.apache.commons.cli.*;
import org.springframework.stereotype.Service;

@Service
public class DownloaderServiceImpl implements DownloaderService {

    @Override
    public void download(String[] args) {
        ArgsDataParseDTO argsDataParseDTO = cliOptionHandle(args);
        if (argsDataParseDTO != null) {
            Downloader downloader = new Downloader();
            downloader.init(argsDataParseDTO);
            downloader.download();
        }
    }

    private ArgsDataParseDTO cliOptionHandle(String[] args) {
        Options options = new Options();

        Option url = Option.builder()
                .longOpt("url")
                .argName("url")
                .hasArg()
                .desc("url of the file")
                .build();

        Option localPath = Option.builder()
                .longOpt("path")
                .argName("path")
                .hasArg()
                .desc("local path for file to save")
                .build();

        Option username = Option.builder()
                .longOpt("user")
                .argName("user")
                .hasArg()
                .desc("Username for connection")
                .build();

        Option password = Option.builder()
                .longOpt("pass")
                .argName("password")
                .hasArg()
                .desc("Password for connection")
                .build();

        Option numConnections = Option.builder()
                .longOpt("con")
                .argName("con")
                .hasArg()
                .desc("Number of connections for download(multiple connection may not support in some servers)")
                .type(Integer.class)
                .build();

        Option helper = Option.builder()
                .longOpt("help")
                .argName("help")
                .desc("help")
                .build();


        options.addOption(url);
        options.addOption(localPath);
        options.addOption(username);
        options.addOption(password);
        options.addOption(numConnections);
        options.addOption(helper);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            HelpFormatter formatter = new HelpFormatter();

            ArgsDataParseDTO dto = new ArgsDataParseDTO();

            if (cmd.hasOption("help")) {
                formatter.printHelp("iDownloader", options);
                return null;
            }

            if (cmd.hasOption("url")) {
                dto.setUrl(cmd.getOptionValue("url"));
            } else {
                formatter.printHelp("iDownloader", options);
                return null;
            }

            if (cmd.hasOption("path")) {
                dto.setLocalPath(cmd.getOptionValue("path"));
            } else {
                formatter.printHelp("iDownloader", options);
                return null;
            }

            if (cmd.hasOption("user")) {
                dto.setUsername(cmd.getOptionValue("user"));
            }

            if (cmd.hasOption("pass")) {
                dto.setPassword(cmd.getOptionValue("pass"));
            }

            if (cmd.hasOption("con")) {
                try {
                    dto.setNumOfConnections(Integer.parseInt(cmd.getOptionValue("con")));
                } catch (NumberFormatException e) {
                    formatter.printHelp("iDownloader", options);
                    return null;
                }
            }

            return dto;

        } catch (ParseException e) {
            System.out.println("command error, " + e.getMessage());
        }

        return null;
    }
}
