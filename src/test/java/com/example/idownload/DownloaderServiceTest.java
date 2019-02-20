package com.example.idownload;

import com.example.idownload.dto.ArgsDataParseDTO;
import com.example.idownload.service.DownloaderServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest(DownloaderServiceImpl.class)
public class DownloaderServiceTest {

    private Downloader downloader;

    @Before
    public void setUp() {
        downloader = mock(Downloader.class);
        MockitoAnnotations.initMocks(this);
        PowerMockito.doNothing().when(downloader).init(any(ArgsDataParseDTO.class));
        PowerMockito.doNothing().when(downloader).download();
    }

    @Test
    public void testDownloaderServiceDownload() throws Exception {
        PowerMockito.whenNew(Downloader.class).withNoArguments().thenReturn(downloader);
        DownloaderServiceImpl service = new DownloaderServiceImpl();
        service.download(new String[]{"--url", "http://localhost/test.txt", "--path", "C:User/"});
        verify(downloader, atLeastOnce()).init(any());
        verify(downloader, atLeastOnce()).download();


    }

    @Test
    public void testDownloadServiceNoDownload() throws Exception {
        PowerMockito.whenNew(Downloader.class).withNoArguments().thenReturn(downloader);
        DownloaderServiceImpl service2 = new DownloaderServiceImpl();
        service2.download(new String[]{});
        verify(downloader, times(0)).init(any());
        verify(downloader, times(0)).download();
    }
}
