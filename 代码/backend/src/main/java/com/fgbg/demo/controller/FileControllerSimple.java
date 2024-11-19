package com.fgbg.demo.controller;

import com.fgbg.common.utils.R;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/common/file2")
public class FileControllerSimple {
    @RequestMapping("/uploadList")
    public R uploadList(@ModelAttribute List<MultipartFile> fileList) throws IOException {
        for (MultipartFile file : fileList) {
            InputStream inputStream = file.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream,
                    new File("E:\\B站视频创作\\前后端项目构建-小功能实现\\代码\\backend\\src\\main\\resources\\file\\" + RandomUtils.nextInt() + ".jpg"));
        }
        return R.ok();
    }
}
