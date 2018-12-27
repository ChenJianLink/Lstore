package cn.chenjianlink.lstore.controller;

import cn.chenjianlink.lstore.common.utils.FastDFSClient;
import cn.chenjianlink.lstore.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传处理
 */
@Controller
public class PictureController {
    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String uploadFile(MultipartFile uploadFile) {
        try {
            //上传图片到服务器中
            FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
            //获取文件扩展名
            String originalFilename = uploadFile.getOriginalFilename();
            String exName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            //得到图片地址和路径
            String url = fastDFSClient.uploadFile(uploadFile.getBytes(), exName);
            //补充完整url
            url = IMAGE_SERVER_URL + url;
            //封装到map
            Map result = new HashMap();
            result.put("error", 0);
            result.put("url", url);

            return JsonUtils.objectToJson(result);
        } catch (Exception e) {
            e.printStackTrace();
            Map result = new HashMap();
            result.put("error", 1);
            result.put("message", "图片上传失败");
            return JsonUtils.objectToJson(result);
        }
    }
}
