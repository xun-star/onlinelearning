package media;
//测试minio的sdk

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;


public class MinioTest {
    MinioClient minioClient = MinioClient.builder().endpoint("https://192.168.101.65:9000")
            .credentials("minioadmin","minioadmin").build();
    @Test
    public void test_upload() throws Exception {
        //通过扩展名得到媒体资源类型 mimeType
        //根据扩展名取出mimeType
        ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(".mp4");
        String mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;//通用mimeType，字节流
        if(extensionMatch!=null){
            mimeType = extensionMatch.getMimeType();
        }
        //上传文件的参数信息
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket("testbucket")
                .filename("E:\\test.jpg")
                .object("test.jpg")
                .contentType(mimeType)
                .build();
        //上传文件
        minioClient.uploadObject(uploadObjectArgs);
    }
    @Test
    public void test_delete() throws Exception {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder().bucket("testbucket").object("test.jpg").build();
        //删除文件文件
        minioClient.removeObject(removeObjectArgs);
    }
    @Test
    public void test_getFile() throws Exception {
        GetObjectArgs getObjectArgs = GetObjectArgs.builder().bucket("testbucket").object("test.jpg").build();
        //查询远程服务获取到一个流对象
        FilterInputStream inputStream = minioClient.getObject(getObjectArgs);
        //指定输出流
        FileOutputStream outputStream = new FileOutputStream(new File("E:\\test.jpg"));
        IOUtils.copy(inputStream,outputStream);
        //校验文件的完整性，对文件内容md5
        String source_md5 = DigestUtils.md5Hex(inputStream);//minio中文件的md5
        String local_md5 = DigestUtils.md5Hex(new FileInputStream("E:\\test.jpg"));
        if(source_md5.equals(local_md5)){
            System.out.println("下载成功");
        }



    }
}
