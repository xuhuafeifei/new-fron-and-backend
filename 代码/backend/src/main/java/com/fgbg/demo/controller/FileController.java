package com.fgbg.demo.controller;

import com.fgbg.common.utils.R;
import com.fgbg.demo.service.FileService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/common/file")
@CrossOrigin
public class FileController {
    @Autowired
    @Qualifier("localFileService")
    private FileService fileService;

    /**
     * 文件访问域名(请求下载的接口)
     */
    private static final String DOMAIN = "http://localhost:9999/upload/getImage";

    /**
     * 文件物理存储位置
     */
    private static final String STORE_DIR = "D:\\CODE\\vue3-big-event-admin\\src\\assets";

    @RequestMapping("/image2")
    public R upload2(@RequestParam("image") MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString().substring(0, 10)
                + "-" + file.getOriginalFilename();
        // 保存到本地
        // 获取文件流
        InputStream is = file.getInputStream();
        // 在服务器中存储文件
        FileUtils.copyInputStreamToFile(is, new File(STORE_DIR + fileName));
        // 返回图片url
        String url = DOMAIN + fileName;
        System.out.println("文件url: " + url);
        return R.ok().put("data", url);
    }

    @RequestMapping("/getImage/{fileName")
    public void getImage(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        // 获取真实的文件路径
        String filePath = STORE_DIR + fileName;
        System.out.println("++++完整路径为："+filePath);

        try {
            // 下载文件
            // 设置响应头
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);

            // 读取文件内容并写入输出流
            Files.copy(Paths.get(filePath), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            response.setStatus(404);
        }
    }

    /**
     * 批量上传
     */
    @RequestMapping("/uploadList")
    public R uploadList(@RequestParam("imageList") List<MultipartFile> fileList) throws IOException {
        List<String> urlList = new ArrayList<>(fileList.size());
        for (MultipartFile file : fileList) {
            String url = fileService.uploadFile(file, UUID.randomUUID().toString().substring(0, 10)
                    + "-" + file.getOriginalFilename());
            urlList.add(url);
        }
        return R.ok().put("data", urlList);
    }

    /**
     * 上传接口
     */
    @RequestMapping("/upload")
    public R upload(@RequestParam("file") MultipartFile file) throws IOException {
        String url = fileService.uploadFile(file, UUID.randomUUID().toString().substring(0, 10)
                + "-" + file.getOriginalFilename());
        return R.ok().put("data", url);
    }

    @PostMapping("/image")
    public RestBean<String> uploadImg(@RequestParam(value = "file" ,required = false) MultipartFile file){

        System.out.println("file: "+file.getOriginalFilename());
        String filename =file.getOriginalFilename(); //随机数 + 获取上传文件原来的名称
        String filePath = "E:\\B站视频创作\\前后端项目构建-小功能实现\\代码\\backend\\src\\main\\resources\\file\\";
        File temp = new File(filePath);
        if (!temp.exists()) { //没有这个文件就创建这个文件
            temp.mkdirs();
        }

        try {
            //File.separator 用于在不同的操作系统下，分隔符有的为/,有的为\的问题
            file.transferTo(new File(temp.getAbsolutePath() + File.separator + filename));
        } catch (IOException e) {
            e.printStackTrace();
            return RestBean.success(filename);
        }


        System.out.println("filename:"+filename);

//        return R.ok(filename);
        RestBean<String> success = RestBean.success(filename);

        System.out.println(success);
        return success;
    }

    /**
     * 下载接口
     */
    @RequestMapping("/download/{fileName}")
    public void download(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) {
        fileService.downloadFile(fileName, request, response);
    }

    /**
     * 删除接口
     */
    @RequestMapping("/delete")
    public R deleteFile(@RequestParam String fileName) {
        boolean flag = fileService.deleteFile(fileName);
        return R.ok().put("data", flag);
    }
}
