package com.example.idownload;

import com.example.idownload.Utils.DownloaderUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicHeader;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class DownloadUtilTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testMergePartialFiles() throws IOException {
        File file1 = folder.newFile("myfile.txt");
        File file2 = folder.newFile("myfile.txt1");
        File file3 = folder.newFile("myfile.txt2");

        FileUtils.writeByteArrayToFile(file2, "1".getBytes());
        FileUtils.writeByteArrayToFile(file3, "2".getBytes());
        DownloaderUtil.mergePartialFiles(file1, new File[]{file2, file3});
        String result = FileUtils.readFileToString(file1, StandardCharsets.UTF_8);
        assertTrue(file1.exists());
        assertEquals("merge done", "12", result);
    }


    @Test
    public void testDeletePartialFiles() throws IOException {
        File file2 = folder.newFile("myfile.txt1");
        File file3 = folder.newFile("myfile.txt2");

        FileUtils.writeByteArrayToFile(file2, "1".getBytes());
        FileUtils.writeByteArrayToFile(file3, "2".getBytes());
        DownloaderUtil.deletePartialFiles(new File[]{file2, file3});
        assertTrue(!file2.exists());
        assertTrue(!file3.exists());
    }

    @Test
    public void testExtractFileName() {
        CloseableHttpResponse response = mock(CloseableHttpResponse.class);
        when(response.getFirstHeader("Content-Disposition")).thenReturn(null);
        String fileName1 = DownloaderUtil.extractFileName(response, "http://localhost/test.txt");
        assertEquals("test.txt", fileName1);
        when(response.getFirstHeader("Content-Disposition")).thenReturn(new BasicHeader("Content-Disposition", "attachment; filename=\"test.txt\""));
        String fileName2 = DownloaderUtil.extractFileName(response, "");
        assertEquals("test.txt", fileName2);

    }
}
