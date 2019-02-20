package com.example.idownload;

import com.example.idownload.dto.ArgsDataParseDTO;
import com.example.idownload.protocols.Connection;
import com.example.idownload.protocols.ConnectionFactory;
import com.example.idownload.protocols.FtpConnection;
import com.example.idownload.protocols.HttpConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PowerMockRunnerDelegate(SpringRunner.class)
@PrepareForTest(ConnectionFactory.class)
public class ConnectionFactoryTest {

    private HttpConnection httpConnection;
    private FtpConnection ftpConnection;
    private ArgsDataParseDTO dto;

    @Before
    public void setUp() throws Exception {
        httpConnection = mock(HttpConnection.class);
        ftpConnection = mock(FtpConnection.class);

        PowerMockito.whenNew(HttpConnection.class).withAnyArguments().thenReturn(httpConnection);
        PowerMockito.whenNew(FtpConnection.class).withAnyArguments().thenReturn(ftpConnection);

        dto = new ArgsDataParseDTO();
        dto.setUrl("http://localhost/test.txt");

    }

    @Test
    public void testHttpConnectionGet() {
        Connection connection = ConnectionFactory.getConnection(dto);
        Assert.assertEquals(connection.getClass().getSuperclass(), HttpConnection.class);

    }

    @Test
    public void testFtpConnectionGet() {
        dto.setUrl("ftp://localhost/test.txt");
        Connection connection = ConnectionFactory.getConnection(dto);
        Assert.assertEquals(connection.getClass().getSuperclass(), FtpConnection.class);

    }
}
