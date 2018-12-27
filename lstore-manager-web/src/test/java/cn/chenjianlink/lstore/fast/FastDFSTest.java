package cn.chenjianlink.lstore.fast;

import cn.chenjianlink.lstore.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class FastDFSTest {
    @Test
    public void testUpload() throws Exception {
        //创建一个配置文件
        //创建全局对象加载配置文件
        ClientGlobal.init("/home/chenjian/GIT/lstore/lstore-manager-web/src/main/resources/conf/client.conf");
        //创建TrackerClient对象
        TrackerClient trackerClient = new TrackerClient();
        //通过TrackerClient获得Trackerserver
        TrackerServer trackerServer = trackerClient.getConnection();

        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        String[] uploadFile = storageClient.upload_file("/home/chenjian/1H.png", "png", null);
        for (String upload : uploadFile) {
            System.out.println(upload);
        }
    }

    @Test
    public void testFastDfsClient() throws Exception {
        FastDFSClient fastDFSClient = new FastDFSClient("/home/chenjian/GIT/lstore/lstore-manager-web/src/main/resources/conf/client.conf");
        String s = fastDFSClient.uploadFile("/home/chenjian/53I.jpg");
        System.out.println(s);
    }
}
